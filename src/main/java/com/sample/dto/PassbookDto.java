package com.sample.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class PassbookDto {
    private String transactionId;
    private Date transactionDate;
    private String transactionType;
    private int transactionAmount;
    private String modeOfTransaction;
    private String remarks;
    private String customerId;
    private String customerName;
    private int openingBalance;
    private int closingBalance;
}
