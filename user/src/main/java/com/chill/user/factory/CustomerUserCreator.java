package com.chill.user.factory;

public class CustomerUserCreator extends UserCreator {
    @Override
    public AppUser createUserType() {
        return new CustomerUser();
    }
}
