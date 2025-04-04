package com.example.mealclue.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mealclue.model.HeartedMealPlan;

import java.util.ArrayList;
import java.util.List;

public class HeartedMealPlanDAO {
    public static final String TABLE_NAME = "HeartedMealPlan";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + "user_id INTEGER NOT NULL, "
            + "meal_plan_id INTEGER NOT NULL, "
            + "PRIMARY KEY(user_id, meal_plan_id), "
            + "FOREIGN KEY(user_id) REFERENCES User(id), "
            + "FOREIGN KEY(meal_plan_id) REFERENCES MealPlan(id));";

    private SQLiteDatabase db;

    public HeartedMealPlanDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(HeartedMealPlan hearted) {
        ContentValues values = new ContentValues();
        values.put("user_id", hearted.getUserId());
        values.put("meal_plan_id", hearted.getMealPlanId());
        return db.insert(TABLE_NAME, null, values);
    }

    public void delete(int userId, int mealPlanId) {
        db.delete(TABLE_NAME, "user_id = ? AND meal_plan_id = ?", new String[]{String.valueOf(userId), String.valueOf(mealPlanId)});
    }

    public boolean isHearted(int userId, int mealPlanId) {
        Cursor cursor = db.query(TABLE_NAME, null, "user_id = ? AND meal_plan_id = ?", new String[]{String.valueOf(userId), String.valueOf(mealPlanId)}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public List<Integer> getHeartedPlanIdsByUser(int userId) {
        List<Integer> mealPlanIds = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"meal_plan_id"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                mealPlanIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("meal_plan_id")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mealPlanIds;
    }

    public int countHeartsForPlan(int mealPlanId) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE meal_plan_id = ?", new String[]{String.valueOf(mealPlanId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int countHeartsUserGot(int userId) {
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_NAME + " h JOIN MealPlan m ON h.meal_plan_id = m.id WHERE m.user_id = ?",
                new String[]{String.valueOf(userId)}
        );
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

}
