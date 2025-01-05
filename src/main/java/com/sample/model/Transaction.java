package com.sample.model;

import lombok.Data;
@Data
public class Transaction {
    private String date;
    private String description;
    private String chqNo;
    private String withdrawals;
    private String deposits;
    private String balance;
    private String transactionType;
    private String modeOfTransaction;
    private String name;

    public Transaction(String date, String description, String chqNo, String withdrawals, String deposits, String balance, String transactionType, String modeOfTransaction, String name) {
        this.date = date;
        this.description = description;
        this.chqNo = chqNo;
        this.withdrawals = withdrawals;
        this.deposits = deposits;
        this.balance = balance;
        this.transactionType = transactionType;
        this.modeOfTransaction = modeOfTransaction;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", chqNo='" + chqNo + '\'' +
                ", withdrawals='" + withdrawals + '\'' +
                ", deposits='" + deposits + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
