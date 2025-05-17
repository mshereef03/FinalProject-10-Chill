package com.chill.user.factory;

import com.chill.user.model.UserModel;
import java.util.List;

public class AdminUser implements AppUser {
    @Override
    public UserModel buildUser(UserModel input) {
        input.setRoles(List.of("ROLE_ADMIN", "ROLE_USER"));
        return input;
    }
}
