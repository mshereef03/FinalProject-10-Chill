package com.chill.user.factory;

import com.chill.user.models.UserModel;

public interface AppUser {
    UserModel buildUser(UserModel input); // Takes base model input, returns enriched user
}
