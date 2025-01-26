package com.sample.serviceImpl;

import com.sample.auth.util.JwtUtil;
import com.sample.model.User;
import com.sample.repo.UserRepo;
import com.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    JwtUtil jwtUtil;
    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User isUserPresent(String userName, String password) {
        return userRepo.findByUserNameAndPassword(userName, password);
    }

    @Override
    public Boolean existsByEmailId(String emailId) {
        return userRepo.existsByEmailId(emailId);
    }

    @Override
    public String checkUserRoleAccess(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                if (jwtUtil.validateToken(jwtToken)) {
                    String userName = jwtUtil.extractUsername(jwtToken);
                    String role = jwtUtil.extractUserRole(jwtToken);
                    User user = userRepo.findByUserName(userName);
                    if (user.getUserRole().equals("admin") && role.contains("admin")) {
                        return "admin";
                    } if (user.getUserRole().equals("user") && role.contains("user")) {
                        return "user";
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return "No Access";
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepo.existsByUserName(username);
    }
}
