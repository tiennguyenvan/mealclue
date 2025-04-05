package com.example.mealclue.model;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class MealPlan {
    private int id;
    private String name;
    private int userId; // FK to User
    private String recipes; // JSON array of Recipe IDs
    private String cookedRecipes; // JSON array of cooked Recipe IDs, allowing duplicates
    private boolean isPrivate;

    private boolean goal; // True if it's a goal plan

    public MealPlan(String name, int userId, String recipes, boolean goal) {
        this.name = name;
        this.userId = userId;
        this.recipes = recipes;
        this.goal = goal;
        this.cookedRecipes = null;
        this.isPrivate = false;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    // Convert recipes JSON string to a List<Integer>
    public List<Integer> getRecipeIdsList() {
        List<Integer> recipeIds = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(recipes);
            for (int i = 0; i < jsonArray.length(); i++) {
                recipeIds.add(jsonArray.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    public List<Integer> getCookedRecipeIdsList() {
        List<Integer> cookedRecipeIds = new ArrayList<>();
        if (cookedRecipes == null || cookedRecipes.trim().isEmpty()) return cookedRecipeIds;
        try {
            JSONArray jsonArray = new JSONArray(cookedRecipes);
            for (int i = 0; i < jsonArray.length(); i++) {
                cookedRecipeIds.add(jsonArray.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cookedRecipeIds;
    }

    public void setCookedRecipeIdsList(List<Integer> cookedRecipeIds) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int id : cookedRecipeIds) {
            jsonArray.put(id);
        }
        this.cookedRecipes = jsonArray.toString();
    }

    public Integer getFirstUncookedRecipeId() {
        List<Integer> allRecipes = getRecipeIdsList();
        List<Integer> cooked = new ArrayList<>(getCookedRecipeIdsList());
        for (Integer recipeId : allRecipes) {
            if (cooked.contains(recipeId)) {
                cooked.remove(recipeId);
            } else {
                return recipeId;
            }
        }
        return null;
    }


    public String getCookedRecipes() { return cookedRecipes; }
    public void setCookedRecipes(String cookedRecipes) { this.cookedRecipes = cookedRecipes; }

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
