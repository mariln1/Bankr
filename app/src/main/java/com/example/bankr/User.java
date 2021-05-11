package com.example.bankr;


import java.math.BigDecimal;

public class User {
    private String username;
    private BigDecimal balance;
    private String password;

    public User(String username, BigDecimal balance, String password) {
        this.username = username;
        this.balance = balance;
        this.password = password;
    }

    public String getUsername() { return username; }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getPassword() {
        return password;
    }

    public void setBalance(BigDecimal balance) { this.balance = balance; }

}
