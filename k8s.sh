#!/usr/bin/env bash
set -euo pipefail

ALL_SERVICES=(api-gateway catalog feedback messaging order user)
K8S_DIR="./k8s"
NAMESPACE="chill"
PORT_FORWARD=false

usage() {
  cat <<EOF
Usage: $0 <up|down|restart> [-s svc1,svc2,...] [-p] [-h]

  up        Create namespace, deploy DB/cache then app, wait for rollouts, optionally port-forward
  down      Delete services, then namespace
  restart   down, then up

  -s  comma-list of services to include (default: all)
  -p  after rollouts, open port-forward to api-gateway-service on localhost:9091
  -h  show this help
EOF
  exit 1
}

[[ $# -lt 1 ]] && usage
COMMAND=$1; shift

SERVICES=("${ALL_SERVICES[@]}")
while getopts "s:ph" opt; do
  case $opt in
    s) IFS=',' read -r -a SERVICES <<< "$OPTARG" ;;
    p) PORT_FORWARD=true ;;
    h) usage ;;
    *) usage ;;
  esac
done
shift $((OPTIND-1))

# apply each existing file in $@
apply_files() {
  for f in "$@"; do
    [[ -e $f ]] || continue
    echo "    ▶ Applying $(basename "$f")"
    kubectl apply -n "$NAMESPACE" -f "$f" >/dev/null
  done
}

# for each file in $@, find any Deployment/StatefulSet it defines, wait on rollout
wait_rollouts() {
  for f in "$@"; do
    [[ -e $f ]] || continue
    resources=$(
      kubectl get -n "$NAMESPACE" -f "$f" -o name \
        | grep -E '^(deployment\.apps|statefulset\.apps)/' || true
    )
    for res in $resources; do
      echo "    ⏳ Waiting on $res"
      kubectl rollout status "$res" -n "$NAMESPACE" --timeout=120s >/dev/null
      echo "      ✔ $res ready"
    done
  done
}

case $COMMAND in
  up)
    echo "➤ Ensuring namespace exists…"
    kubectl apply -f "$K8S_DIR/namespace.yaml" >/dev/null

    for svc in "${SERVICES[@]}"; do
      dir="$K8S_DIR/$svc"
      echo
      echo "=== Service: $svc ==="

      # 1) split DB/cache vs app files
      db_files=()
      app_files=()
      for f in "$dir"/*.yaml; do
        bn=$(basename "$f")
        if [[ "$bn" == *postgres* ]] \
        || [[ "$bn" == *mongo* ]] \
        || [[ "$bn" == *redis* ]]; then
          db_files+=("$f")
        else
          app_files+=("$f")
        fi
      done

      # DB/cache
      if (( ${#db_files[@]} )); then
        echo "  • DB & cache:"
        apply_files "${db_files[@]}"
        wait_rollouts "${db_files[@]}"
      else
        echo "  • No DB/cache resources for $svc, skipping."
      fi

      # Application
      if (( ${#app_files[@]} )); then
        echo "  • Application:"
        apply_files "${app_files[@]}"
        wait_rollouts "${app_files[@]}"
      else
        echo "  • No application resources for $svc, skipping."
      fi


      echo "✅ $svc is fully up"
    done

    if $PORT_FORWARD; then
      echo
      echo "➤ Port-forwarding api-gateway → localhost:9091"
      kubectl port-forward svc/api-gateway-service 9091:9091 -n "$NAMESPACE"
    fi
    ;;

  down)
    for ((i=${#SERVICES[@]}-1; i>=0; i--)); do
      svc=${SERVICES[i]}
      echo "🗑️  Deleting service: $svc"
      kubectl delete -n "$NAMESPACE" --ignore-not-found -f "$K8S_DIR/$svc"
    done
    echo "🗑️  Deleting namespace 'chill'"
    kubectl delete -f "$K8S_DIR/namespace.yaml" --ignore-not-found
    ;;

  restart)
    "$0" down -s "$(IFS=,; echo "${SERVICES[*]}")"
    "$0" up   -s "$(IFS=,; echo "${SERVICES[*]}")" $([ "$PORT_FORWARD" = true ] && echo -p)
    ;;

  *)
    usage
    ;;
esac
