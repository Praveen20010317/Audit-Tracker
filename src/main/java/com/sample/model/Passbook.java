package com.sample.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
@Entity
public class Passbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private Date transactionDate;
    private String transactionType;
    private int transactionAmount;
    private String modeOfTransaction;
    private int openingBalance;
    private int closingBalance;
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
