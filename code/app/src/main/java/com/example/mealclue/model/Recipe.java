package com.example.mealclue.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String title;
    private String image; // URL or file path
    private String ingredients; // JSON map of <name of ingredient -> amount>
    private String instructions; // JSON array of steps [str,str]
    private String keywords;

    public Recipe(int id, String title, String image, String ingredients, String instructions, String keywords) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<Ingredient> getIngredientList() throws JSONException {
        List<Ingredient> list = new ArrayList<>();
        JSONArray arr = new JSONArray(ingredients);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            list.add(new Ingredient(
                    o.getString("name"),
                    o.getString("amount"),
                    o.getString("unit")
            ));
        }
        return list;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", instructions='" + instructions + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
