package com.example.mealclue.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.mealclue.R;
import com.example.mealclue.controller.UserDAO;

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


    public static User getLoggedInUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.k_meal_clue_prefs), MODE_PRIVATE);
        int savedUserId = prefs.getInt(context.getString(R.string.k_logged_in_user_id), -1);
        if (savedUserId == -1) {
            Toast.makeText(context, "User should log in", Toast.LENGTH_SHORT).show();
            return null;
        }
        UserDAO userDAO = new UserDAO(context);
        if (userDAO.count() == 0) {
            Toast.makeText(context, "Empty User Base", Toast.LENGTH_SHORT).show();
            return null;
        }

        // for demo, I use first user at this time
        User user = userDAO.getUserById(savedUserId);
        if (user == null) {
            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
            return null;
        }
        return user;
    }
}
