package com.sample.service;

import com.sample.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    User saveUser(User user);

    User isUserPresent(String username, String password);

    Boolean existsByUsername(String username);

    Boolean existsByEmailId(String emailId);

    String checkUserRoleAccess(String token);
}
