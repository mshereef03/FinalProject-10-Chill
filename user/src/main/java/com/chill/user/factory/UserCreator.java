package com.chill.user.factory;

import com.chill.user.model.UserModel;

public abstract class UserCreator {
    public abstract AppUser createUserType();

    public UserModel create(UserModel input) {
        AppUser userType = createUserType();
        return userType.buildUser(input);
    }
}
