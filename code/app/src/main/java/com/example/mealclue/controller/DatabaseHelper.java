package com.example.mealclue.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mealclue.db";
    private static final int DB_VERSION = 4;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDAO.CREATE_TABLE);
        db.execSQL(RecipeDAO.CREATE_TABLE);
        db.execSQL(MealPlanDAO.CREATE_TABLE); // Added MealPlan table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserDAO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeDAO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MealPlanDAO.TABLE_NAME);
        onCreate(db);
    }
}
