package com.chill.user.factory;

import com.chill.user.models.UserModel;

public abstract class UserCreator {
    public abstract AppUser createUserType();

    public UserModel create(UserModel input) {
        AppUser userType = createUserType();
        return userType.buildUser(input);
    }
}
