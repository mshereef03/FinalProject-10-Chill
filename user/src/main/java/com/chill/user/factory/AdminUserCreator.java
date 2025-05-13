package com.chill.user.factory;

public class AdminUserCreator extends UserCreator {
    @Override
    public AppUser createUserType() {
        return new AdminUser();
    }
}
