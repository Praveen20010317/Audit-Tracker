package com.sample.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
public class User {
    @Id
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String emailId;
    private String mobileNo;
    private String userRole;
}
