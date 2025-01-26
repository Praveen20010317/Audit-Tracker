package com.sample.repo;

import com.sample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    User findByUserName(String userName);

    User findByUserNameAndPassword(String userName, String password);

    Boolean existsByEmailId(String emailId);

    Boolean existsByUserName(String userName);
}
