package com.example.bankr;


public class User {
    private String username;
    private float balance;
    private String password;

    public User(String username, float balance, String password) {
        this.username = username;
        this.balance = balance;
        this.password = password;
    }

    public String getUsername() { return username; }

    public float getBalance() {
        return balance;
    }

    public String getPassword() {
        return password;
    }

    public void setBalance(float balance) { this.balance = balance; }

}
