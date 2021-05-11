package com.example.bankr;


import java.math.BigDecimal;

/**
 * Represents a user of Bankr
 * Contains username, password, and account balance
 */
public class User {
    private String username;
    private BigDecimal balance;
    private String password;

    public User(String username, BigDecimal balance, String password) {
        this.username = username;
        this.balance = balance;
        this.password = password;
    }

    /**
     * Gets the username
     * @return username
     */
    public String getUsername() { return username; }

    /**
     * Gets the balance
     * @return balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Gets the password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets balance of a user
     * @param balance
     */
    public void setBalance(BigDecimal balance) { this.balance = balance; }

}
