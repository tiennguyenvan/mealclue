package com.example.mealclue.model;

public class User {
    private int id;
    private String avatar; // File path or Base64 string
    private String fullName;
    private int hearts;
    private String postalCode;
    private String username;
    private String passwordHash;

    public User(String avatar, String fullName, int hearts, String postalCode, String username, String passwordHash) {
        this.avatar = avatar;
        this.fullName = fullName;
        this.hearts = hearts;
        this.postalCode = postalCode;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
