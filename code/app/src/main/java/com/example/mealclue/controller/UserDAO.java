package com.example.mealclue.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mealclue.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static final String TABLE_NAME = "User";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "avatar TEXT, "
            + "full_name TEXT NOT NULL, "
            + "hearts INTEGER DEFAULT 0, "
            + "postal_code TEXT, "
            + "email TEXT UNIQUE NOT NULL);";

    private SQLiteDatabase db;

    public UserDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put("avatar", user.getAvatar());
        values.put("full_name", user.getFullName());
        values.put("hearts", user.getHearts());
        values.put("postal_code", user.getPostalCode());
        values.put("email", user.getEmail());

        return db.insert(TABLE_NAME, null, values);
    }

    public User getUserByEmail(String email) {
        Cursor cursor = db.query(TABLE_NAME, null, "email = ?", new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                    cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("hearts")),
                    cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email"))
            );
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            cursor.close();
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                        cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("hearts")),
                        cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email"))
                );
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put("avatar", user.getAvatar());
        values.put("full_name", user.getFullName());
        values.put("hearts", user.getHearts());
        values.put("postal_code", user.getPostalCode());
        values.put("email", user.getEmail());

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(int userId) {
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(userId)});
    }

    public void insertMockUser() {
        User mockUser = new User(
                "default_avatar.png", // Avatar
                "Alice Johnson",       // Full Name
                5,                     // Hearts count
                "V6B 1A9",             // Postal Code
                "alice.johnson@example.com" // Email
        );
        insertUser(mockUser);
    }
}
