package com.chill.user.factory;

import com.chill.user.models.UserModel;
import java.util.List;

public class CustomerUser implements AppUser {
    @Override
    public UserModel buildUser(UserModel input) {
        input.setRoles(List.of("ROLE_CUSTOMER"));
        return input;
    }
}
