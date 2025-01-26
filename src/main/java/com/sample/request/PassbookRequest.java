package com.sample.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class PassbookRequest {
    private String transactionId;
    private Date transactionDate;
    private String transactionType;
    private int transactionAmount;
    private String modeOfTransaction;
    private String remarks;
    private String customerName;
}
