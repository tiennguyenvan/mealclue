package com.example.mealclue.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mealclue.db";
    private static final int DB_VERSION = 12;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDAO.CREATE_TABLE);
        db.execSQL(RecipeDAO.CREATE_TABLE);
        db.execSQL(MealPlanDAO.CREATE_TABLE); // Added MealPlan table
        db.execSQL(HeartedMealPlanDAO.CREATE_TABLE); // Added HeartedMealPlan table
        Utils.demoDataCreation(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 12) {
            db.execSQL("ALTER TABLE " + MealPlanDAO.TABLE_NAME + " ADD COLUMN is_private INTEGER NOT NULL DEFAULT 0;");
        }

//        if (oldVersion < newVersion) {
//            db.execSQL(HeartedMealPlanDAO.CREATE_TABLE);
//        }
        //        db.execSQL("DROP TABLE IF EXISTS " + UserDAO.TABLE_NAME);
        //        db.execSQL("DROP TABLE IF EXISTS " + RecipeDAO.TABLE_NAME);
        //        db.execSQL("DROP TABLE IF EXISTS " + MealPlanDAO.TABLE_NAME);
//        // Rename existing table to temporary table
//        db.execSQL("ALTER TABLE " + MealPlanDAO.TABLE_NAME + " RENAME TO temp_MealPlan;");
//
//        // Create new table with correct column name
//        db.execSQL("CREATE TABLE " + MealPlanDAO.TABLE_NAME + " ("
//                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + "name TEXT NOT NULL, "
//                + "user_id INTEGER NOT NULL, "
//                + "recipes TEXT NOT NULL, "
//                + "goal INTEGER NOT NULL CHECK(goal IN (0,1)), "
//                + "cooked_recipes TEXT, "
//                + "FOREIGN KEY(user_id) REFERENCES User(id));");
//
//        // Copy data from temp table to new table
//        db.execSQL("INSERT INTO " + MealPlanDAO.TABLE_NAME + " (id, name, user_id, recipes, goal, cooked_recipes) "
//                + "SELECT id, name, user_id, recipes, goal, cookedRecipes FROM temp_MealPlan;");
//
//        // Drop temporary table
//        db.execSQL("DROP TABLE temp_MealPlan;");

        // onCreate(db);
    }


}
