package com.example.mealclue.controller;

import android.content.Context;

import com.example.mealclue.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.spoonacular.com/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getApiKey() {
        return BuildConfig.SPOONACULAR_API_KEY; // Use BuildConfig to get API Key
    }
}
