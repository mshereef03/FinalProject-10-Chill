package com.chill.catalog.publishing;

@Component
public class UserServiceObserver implements MysteryBagObserver {
    private final RestTemplate rest;
    private final String userServiceUrl;

    public UserServiceObserver(RestTemplate rest,
                               @Value("${seller.user-service.url}") String userServiceUrl) {
        this.rest = rest;
        this.userServiceUrl = userServiceUrl;
    }

    @Override
    public void onPublish(MysteryBag bag) {
        // You might have a dedicated endpoint in User service, e.g. /events/mystery-bag
        String endpoint = userServiceUrl + "/events/mystery-bag/published";
        rest.postForLocation(endpoint, bag);
        System.out.println("[USER OBSERVER] Notified User service of bag " + bag.getId());
    }
}