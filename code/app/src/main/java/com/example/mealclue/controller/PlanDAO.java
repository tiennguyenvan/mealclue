package com.example.mealclue.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mealclue.model.Plan;

import java.util.ArrayList;
import java.util.List;

public class PlanDAO {
    public static final String TABLE_NAME = "Plan";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL, "
            + "user_id INTEGER NOT NULL, "
            + "recipes TEXT NOT NULL, "
            + "goal INTEGER NOT NULL CHECK(goal IN (0,1)), "
            + "FOREIGN KEY(user_id) REFERENCES User(id));";

    private SQLiteDatabase db;

    public PlanDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert Plan
    public long insertPlan(Plan plan) {
        ContentValues values = new ContentValues();
        values.put("name", plan.getName());
        values.put("user_id", plan.getUserId());
        values.put("recipes", plan.getRecipes());
        values.put("goal", plan.isGoal() ? 1 : 0);

        return db.insert(TABLE_NAME, null, values);
    }

    // Get Plan by ID
    public Plan getPlanById(int id) {
        Cursor cursor = db.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.moveToFirst()) {
            Plan plan = new Plan(
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("recipes")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("goal")) == 1
            );
            plan.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            cursor.close();
            return plan;
        }
        return null;
    }

    // Get All Plans for a User
    public List<Plan> getAllPlansByUser(int userId) {
        List<Plan> planList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Plan plan = new Plan(
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("recipes")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("goal")) == 1
                );
                plan.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                planList.add(plan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return planList;
    }

    // Get Only Goal Plans for a User
    public List<Plan> getGoalPlansByUser(int userId) {
        List<Plan> planList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE user_id = ? AND goal = 1", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Plan plan = new Plan(
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("recipes")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("goal")) == 1
                );
                plan.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                planList.add(plan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return planList;
    }

    // Update Plan
    public int updatePlan(Plan plan) {
        ContentValues values = new ContentValues();
        values.put("name", plan.getName());
        values.put("user_id", plan.getUserId());
        values.put("recipes", plan.getRecipes());
        values.put("goal", plan.isGoal() ? 1 : 0);

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(plan.getId())});
    }

    // Delete Plan
    public void deletePlan(int id) {
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }
}
