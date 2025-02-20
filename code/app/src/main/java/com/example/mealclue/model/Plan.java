package com.example.mealclue.model;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class Plan {
    private int id;
    private String name;
    private int userId; // FK to User
    private String recipes; // JSON array of Recipe IDs
    private boolean goal; // True if it's a goal plan

    public Plan(String name, int userId, String recipes, boolean goal) {
        this.name = name;
        this.userId = userId;
        this.recipes = recipes;
        this.goal = goal;
    }

    // Convert recipes JSON string to a List<Integer>
    public List<Integer> getRecipeIdsList() throws JSONException {
        List<Integer> recipeIds = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(recipes);

        for (int i = 0; i < jsonArray.length(); i++) {
            recipeIds.add(jsonArray.getInt(i));
        }
        return recipeIds;
    }

    // Convert List<Integer> to JSON string
    public void setRecipeIdsList(List<Integer> recipeIds) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int id : recipeIds) {
            jsonArray.put(id);
        }
        this.recipes = jsonArray.toString();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getRecipes() { return recipes; }
    public void setRecipes(String recipes) { this.recipes = recipes; }

    public boolean isGoal() { return goal; }
    public void setGoal(boolean goal) { this.goal = goal; }
}
