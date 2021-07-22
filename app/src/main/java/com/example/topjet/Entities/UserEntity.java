package com.example.topjet.Entities;

// List of all the user qualities
public class UserEntity {
    String email, username, password, cbIdentify;
    double levelScore;

    // Constructor
    public UserEntity(String email, String username, String password, String cbIdentify, double levelScore) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.cbIdentify = cbIdentify;
        this.levelScore = levelScore;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCbIdentify(String cbIdentify) {
        this.cbIdentify = cbIdentify;
    }

    public void setLevelScore(double levelScore) {
        this.levelScore = levelScore;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCbIdentify() {
        return cbIdentify;
    }

    public double getLevelScore() {
        return levelScore;
    }

}
