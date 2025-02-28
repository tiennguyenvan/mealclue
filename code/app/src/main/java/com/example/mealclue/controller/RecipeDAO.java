package com.example.mealclue.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mealclue.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    public static final String TABLE_NAME = "Recipe";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + "id INTEGER PRIMARY KEY, "
            + "title TEXT NOT NULL, "
            + "image TEXT, "
            + "youtube_url TEXT, "
            + "ingredients TEXT, "
            + "instructions TEXT);";

    private SQLiteDatabase db;

    public RecipeDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insertRecipe(Recipe recipe) {
        System.out.println(recipe);
        ContentValues values = new ContentValues();
        values.put("id", recipe.getId()); // Ensure ID is inserted
        values.put("title", recipe.getTitle());
        values.put("image", recipe.getImage());
        values.put("ingredients", recipe.getIngredients());
        values.put("instructions", recipe.getInstructions());

        return db.insert(TABLE_NAME, null, values);
    }

    public void insertOrUpdateRecipe(Recipe recipe) {
        // if this id is not in the the database yet
        if (getRecipeById(recipe.getId()) == null) {
            insertRecipe(recipe);
        }
    }

    public Recipe getRecipeById(int id) {
        Cursor cursor = db.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.moveToFirst()) {
            Recipe recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image"))
            );
            cursor.close();
            return recipe;
        }
        return null;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image"))
                );
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipeList;
    }

    public int updateRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put("title", recipe.getTitle());
        values.put("image", recipe.getImage());
        values.put("ingredients", recipe.getIngredients());
        values.put("instructions", recipe.getInstructions());

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(recipe.getId())});
    }


    // Delete Recipe
    public void deleteRecipe(int id) {
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Recipe> searchRecipes(String keyword) {
        List<Recipe> recipeList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE title LIKE ?", new String[]{"%" + keyword + "%"});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image"))
                );
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipeList;
    }

}
