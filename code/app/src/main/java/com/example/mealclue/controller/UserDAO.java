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
            + "username TEXT UNIQUE NOT NULL, "
            + "password_hash TEXT NOT NULL);";

    private SQLiteDatabase db;

    public UserDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(User user) {
        ContentValues values = new ContentValues();
        values.put("avatar", user.getAvatar());
        values.put("full_name", user.getFullName());
        values.put("hearts", user.getHearts());
        values.put("postal_code", user.getPostalCode());
        values.put("username", user.getUsername());
        values.put("password_hash", user.getPasswordHash());

        return db.insert(TABLE_NAME, null, values);
    }

    public User getByEmail(String email) {
        Cursor cursor = db.query(TABLE_NAME, null, "email = ?", new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                    cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("hearts")),
                    cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password_hash"))
            );
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            cursor.close();
            return user;
        }
        return null;
    }

    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                        cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("hearts")),
                        cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password_hash"))
                );
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public int update(User user) {
        ContentValues values = new ContentValues();
        values.put("avatar", user.getAvatar());
        values.put("full_name", user.getFullName());
        values.put("hearts", user.getHearts());
        values.put("postal_code", user.getPostalCode());
        values.put("username", user.getUsername());
        values.put("password_hash", user.getPasswordHash());

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(int userId) {
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(userId)});
    }

    public void insertMockUser() {
        String password = "123456";
        String hash = String.valueOf(password.hashCode());

        User mockUser = new User(
                "default_avatar.png", // Avatar
                "Tim Nguyen",         // Full Name
                5,                    // Hearts count
                "V6B 1A9",            // Postal Code
                "timnguyen",          // Username
                hash                  // Password hash
        );
        insert(mockUser);
    }


    public int count() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public User getUserByUsername(String username) {
        Cursor cursor = db.query(TABLE_NAME, null, "username = ?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                    cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("hearts")),
                    cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password_hash"))
            );
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            cursor.close();
            return user;
        }
        return null;
    }

    public User getUserById(int id) {
        Cursor cursor = db.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                    cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("hearts")),
                    cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password_hash"))
            );
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

}
