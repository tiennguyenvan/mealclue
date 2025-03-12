package com.example.mealclue.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealclue.R;
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.controller.RetrofitClient;
import com.example.mealclue.controller.SpoonacularApiService;
import com.example.mealclue.databinding.FragmentPlanDetailBinding;
import com.example.mealclue.model.Recipe;
import com.example.mealclue.model.RecipeResponse;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.mealclue.databinding.FragmentPlanDetailSearchRecipeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanDetailSearchRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanDetailSearchRecipeFragment extends Fragment {
    private FragmentPlanDetailSearchRecipeBinding $;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlanDetailSearchRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanDetailSearchRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanDetailSearchRecipeFragment newInstance(String param1, String param2) {
        PlanDetailSearchRecipeFragment fragment = new PlanDetailSearchRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        $ = FragmentPlanDetailSearchRecipeBinding.inflate(inflater, container, false);
        return $.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String apiKey = RetrofitClient.getApiKey();
        Log.d("API_KEY_TEST", "Retrieved API Key: " + apiKey);


//        List<Recipe> mockRecipes = new ArrayList<>();
//        mockRecipes.add(new Recipe("Spaghetti Bolognese", String.valueOf(R.drawable.login_background), "", ""));
//        mockRecipes.add(new Recipe("Grilled Chicken Salad", String.valueOf(R.drawable.login_background), "", ""));
//        mockRecipes.add(new Recipe("Pancakes", String.valueOf(R.drawable.login_background), "", ""));
//        updateRecyclerView(mockRecipes);
        List<Recipe> recipes = new RecipeDAO(requireContext()).getAllRecipes();
        updateRecyclerView(recipes);


        searchRecipes("noodle");

//        $.incSearchBar.btnSearch.setOnClickListener(v -> {
//            String keyword = $.incSearchBar.inpKeywords.getText().toString().trim();
//            if (!keyword.isEmpty()) {
//                searchRecipes(keyword);
//            }
//        });

        List<String> categories = Arrays.asList("Cuisines", "Ingredients", "Calories", "Test");
//        $.recyclerRecipeCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
//        $.recyclerRecipeCategories.setAdapter(categoryAdapter);

//        List<String> subCategories = Arrays.asList("Clear", "Italian", "Thai", "Mexican");
//        $.recyclerRecipeSubCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//        SubCategoryAdapter subCatAdapt = new SubCategoryAdapter(subCategories);
//        $.recyclerRecipeSubCategories.setAdapter(subCatAdapt);

    }


    private void searchRecipes(String keyword) {
        RecipeDAO recipeDAO = new RecipeDAO(requireContext());
        List<Recipe> recipes = recipeDAO.searchRecipes(keyword);

        if (!recipes.isEmpty()) {
            updateRecyclerView(recipes);
        } else {
            fetchRecipesFromAPI(keyword);
        }
    }


    /**
     * 2025-02-28 10:19:30.725  1309-1309  API_RESPONSE
     * com.example.mealclue                 D
     * Response body:
     * {"results":[{"id":633858,"image":"https://img.spoonacular.com/recipes/633858-312x231.jpg"},{"id":632778,"image":"https://img.spoonacular.com/recipes/632778-312x231.jpg"},{"id":643642,"image":"https://img.spoonacular.com/recipes/643642-312x231.jpg"},{"id":642583,"image":"https://img.spoonacular.com/recipes/642583-312x231.jpg"},{"id":645354,"image":"https://img.spoonacular.com/recipes/645354-312x231.jpg"}]}
     * <p>
     * {"results":[{"id":633858,"title":"Baked Tortellini In Red Sauce","image":"https://img.spoonacular.com/recipes/633858-312x231.jpg","imageType":"jpg"},{"id":632778,"title":"Artisan Farfalle Pasta With Smoked Salmon and Cream Sauce","image":"https://img.spoonacular.com/recipes/632778-312x231.jpg","imageType":"jpg"},{"id":643642,"title":"Macaroni Pasta with Fresh Tomatoes, Zucchini and Artichokes","image":"https://img.spoonacular.com/recipes/643642-312x231.jpg","imageType":"jpg"},{"id":642583,"title":"Farfalle with Peas, Ham and Cream","image":"https://img.spoonacular.com/recipes/642583-312x231.jpg","imageType":"jpg"},{"id":645354,"title":"Greek Shrimp Orzo","image":"https://img.spoonacular.com/recipes/645354-312x231.jpg","imageType":"jpg"}],"offset":0,"number":5,"totalResults":297}
     *
     * @param keyword
     */
    private void fetchRecipesFromAPI(String keyword) {
        SpoonacularApiService apiService = RetrofitClient.getClient().create(SpoonacularApiService.class);
        String apiKey = RetrofitClient.getApiKey();

        apiService.searchRecipes(apiKey, keyword, 5).enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                Log.d("API_REQUEST", "Request URL: " + call.request().url());
                Log.d("API_RESPONSE", "Response body: " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().getRecipes();

                    if (recipes == null || recipes.isEmpty()) {
                        Log.e("API_ERROR", "No recipes found from API");
                        return;
                    }

                    saveRecipesToDatabase(recipes);
                    updateRecyclerView(recipes);
                } else {
                    Log.e("API_ERROR", "API response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.e("API_REQUEST", "Request URL: " + call.request().url());
                Log.e("API_ERROR", "Failed to fetch recipes", t);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "API Error: " + t.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }


    private void saveRecipesToDatabase(List<Recipe> recipes) {
        RecipeDAO recipeDAO = new RecipeDAO(requireContext());
        for (Recipe recipe : recipes) {
            recipeDAO.insertOrUpdateRecipe(recipe);
        }
    }

    private void updateRecyclerView(List<Recipe> recipes) {
//        RecyclerView recyclerView = $.recyclerRecipeList;
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        recyclerView.setAdapter(new RecipeListAdapter(recipes, requireContext()));
    }

}