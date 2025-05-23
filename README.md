# Too Good To Throw

This project, **Too Good to Throw**, is a microservices-based application aimed at reducing food waste by connecting vendors with shoppers through discounted "Mystery Bags" of surplus food.

## Project Idea

Each year, roughly one-third of all food produced for human consumption—about 1.3 billion tonnes—is lost or wasted globally, costing the world economy over \$1 trillion and driving 8–10% of greenhouse-gas emissions. _Too Good to Throw_ tackles this problem by enabling vendors to package extra food into surprise bags and sell them at steep discounts, turning surplus into opportunity.

Key features:

- **Surprise Mystery Bags**: Vendors pack excess items into mystery bags and list them at big discounts.
- **Scheduled Drops**: Deals go live at set times so everyone has a fair shot.
- **Nearby Offers**: Shoppers can filter deals within their chosen distance.
- **Instant Alerts**: Get notified the moment a deal starts.
- **Simple for Vendors**: Vendors just pack, pick a time, and publish.
- **Built to Scale**: Each feature runs in its own service, ensuring high availability and performance.

## Architecture

The application consists of the following microservices, each containerized with Docker and deployable to Kubernetes:

- **api-gateway**: Handles incoming requests and routes them to the appropriate service.
- **user**: Manages user accounts, authentication (login/logout), password reset, and email verification.
- **catalog**: Allows vendors to create, schedule, and publish Mystery Bag offers with dynamic pricing. Supports search and filtering.
- **order**: Handles order reservations, payment processing via Stripe, refunds, and credit card validation.
- **feedback**: Collects and manages different types of feedback (reviews, complaints, questions), supports vendor replies, sorting, and upvotes.

## Getting Started

The quickest way to get the application up and running is to use the `k8s.sh` script, which automates deployment to a Kubernetes cluster.

### Prerequisites

- Docker
- Kubernetes (e.g., Minikube, Kind, Docker Desktop Kubernetes)
- kubectl

### Usage

Ensure Docker and Minikube are running, then:

```bash
./k8s.sh up                 # Deploy all services
./k8s.sh up -s api-gateway,catalog -p  # Deploy specific services with port-forwarding
./k8s.sh down               # Delete all services
./k8s.sh restart -s feedback # Restart the feedback service
```

### Manual Deployment

You can also deploy services manually with Docker Compose for local development or by applying the YAML files in the `k8s/` directory.

## Services

Each service is a Spring Boot application with its own Dockerfile in the respective directory (e.g., `api_gateway/`, `catalog/`).

## Monitoring

Prometheus and Grafana configurations are provided in the `monitoring/` directory for metrics collection and visualization.

## Logs

Service-specific logs are stored under each service's `logs/` directory (e.g., `api_gateway/logs/`), and aggregated logs can be found in the top-level `logs/` directory.
