package com.sample.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Entity
public class Customer {
    @Id
    private String customerId;
    private String customerName;
    private String location;
    private String mobileNo;
    private String emailId;
    private int availableBalance;
}
