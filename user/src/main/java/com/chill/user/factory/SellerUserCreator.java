package com.chill.user.factory;

public class SellerUserCreator extends UserCreator {
    @Override
    public AppUser createUserType() {
        return new SellerUser();
    }
}
