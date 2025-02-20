package com.example.mealclue.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Recipe {
    private int id;
    private String name;
    private String image; // URL or file path
    private String ingredients; // JSON map of <name of ingredient -> amount>
    private String instructions; // JSON array of steps [str,str]

    public Recipe(String name, String image, String ingredients, String instructions) {
        this.name = name;
        this.image = image;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Map<String, String> getIngredientsMap() throws JSONException {
        Map<String, String> ingredientsMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject(ingredients);

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            ingredientsMap.put(key, jsonObject.getString(key));
        }

        return ingredientsMap;
    }

    public String[] getInstructionsArray() throws JSONException {
        JSONArray jsonArray = new JSONArray(instructions);
        String[] steps = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            steps[i] = jsonArray.getString(i);
        }
        return steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
