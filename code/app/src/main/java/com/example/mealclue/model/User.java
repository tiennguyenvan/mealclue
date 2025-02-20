package com.example.mealclue.model;

public class User {
    private int id;
    private String avatar; // File path or Base64 string
    private String fullName;
    private int hearts;
    private String postalCode;
    private String email;

    public User(String avatar, String fullName, int hearts, String postalCode, String email) {
        this.avatar = avatar;
        this.fullName = fullName;
        this.hearts = hearts;
        this.postalCode = postalCode;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
