package com.example.mealclue.controller;

import com.example.mealclue.model.Recipe;
import com.example.mealclue.model.RecipeResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularApiService {
    @GET("recipes/random")
    Call<RecipeResponse> getRandomRecipes(
            @Query("apiKey") String apiKey,
            @Query("number") int number
    );

    @GET("recipes/complexSearch")
    Call<RecipeResponse> searchRecipes(
            @Query("apiKey") String apiKey,
            @Query("query") String keyword,
            @Query("number") int number
    );

    @GET("recipes/{id}/information")
    Call<JsonObject> getRecipeInformation(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );


}
