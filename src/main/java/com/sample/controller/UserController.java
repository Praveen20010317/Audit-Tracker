package com.sample.controller;

import com.sample.auth.util.JwtUtil;
import com.sample.request.AuthRequest;
import com.sample.model.User;
import com.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserService userservice;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public User saveUser(@RequestBody User user) {
        return userservice.saveUser(user);
    }

    @GetMapping("/login/{userName}/{password}")
    public User isUserPresent(@PathVariable String userName, @PathVariable String password) {
        return userservice.isUserPresent(userName, password);
    }

    @GetMapping("/checkUsername/{username}")
    public boolean userValidation(@PathVariable String username) {
        return userservice.existsByUsername(username);
    }

    @GetMapping("/checkUserMailId/{emailId}")
    public boolean emailValidation(@PathVariable String emailId) {
        return userservice.existsByEmailId(emailId);
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        User user = userservice.isUserPresent(authRequest.getUserName(), authRequest.getPassword());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        } catch (Exception e) {
            throw new Exception("Invalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName(), user.getUserRole());
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkUserRole(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                if (jwtUtil.validateToken(jwtToken)) {
                    String userName = jwtUtil.extractUsername(jwtToken);
                    String role = jwtUtil.extractUserRole(jwtToken);
                    if (role.contains("admin")) {
                        return ResponseEntity.ok("Welcome " + userName +" - " + role);
                    } if (role.contains("user")) {
                        return ResponseEntity.ok("Welcome " + userName +" - " + role);
                    }
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid User");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User");
    }

}
