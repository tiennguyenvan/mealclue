package com.example.mealclue.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mealclue.model.MealPlan;

import java.util.ArrayList;
import java.util.List;

public class MealPlanDAO {
    public static final String TABLE_NAME = "MealPlan";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL, "
            + "user_id INTEGER NOT NULL, "
            + "recipes TEXT NOT NULL, "
            + "goal INTEGER NOT NULL CHECK(goal IN (0,1)), "
            + "FOREIGN KEY(user_id) REFERENCES User(id));";

    private SQLiteDatabase db;

    public MealPlanDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert MealPlan
    public long insertPlan(MealPlan mealPlan) {
        ContentValues values = new ContentValues();
        values.put("name", mealPlan.getName());
        values.put("user_id", mealPlan.getUserId());
        values.put("recipes", mealPlan.getRecipes());
        values.put("goal", mealPlan.isGoal() ? 1 : 0);

        return db.insert(TABLE_NAME, null, values);
    }

    // Get MealPlan by ID
    public MealPlan getPlanById(int id) {
        Cursor cursor = db.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.moveToFirst()) {
            MealPlan mealPlan = new MealPlan(
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("recipes")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("goal")) == 1
            );
            mealPlan.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            cursor.close();
            return mealPlan;
        }
        return null;
    }

    // Get All Plans for a User
    public List<MealPlan> getAllPlansByUser(int userId) {
        List<MealPlan> mealPlanList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                MealPlan mealPlan = new MealPlan(
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("recipes")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("goal")) == 1
                );
                mealPlan.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                mealPlanList.add(mealPlan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mealPlanList;
    }

    // Get Only Goal Plans for a User
    public List<MealPlan> getGoalPlansByUser(int userId) {
        List<MealPlan> mealPlanList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE user_id = ? AND goal = 1", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                MealPlan mealPlan = new MealPlan(
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("recipes")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("goal")) == 1
                );
                mealPlan.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                mealPlanList.add(mealPlan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mealPlanList;
    }

    // Update MealPlan
    public int updatePlan(MealPlan mealPlan) {
        ContentValues values = new ContentValues();
        values.put("name", mealPlan.getName());
        values.put("user_id", mealPlan.getUserId());
        values.put("recipes", mealPlan.getRecipes());
        values.put("goal", mealPlan.isGoal() ? 1 : 0);

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(mealPlan.getId())});
    }

    // Delete MealPlan
    public void deletePlan(int id) {
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }
}
