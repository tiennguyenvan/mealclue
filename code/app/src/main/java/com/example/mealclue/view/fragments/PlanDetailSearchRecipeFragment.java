package com.example.mealclue.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealclue.controller.MealPlanDAO;
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.controller.RetrofitClient;
import com.example.mealclue.controller.SpoonacularApiService;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.Recipe;
import com.example.mealclue.model.RecipeResponse;
import com.example.mealclue.view.adapters.RecipeListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
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
public class PlanDetailSearchRecipeFragment extends Fragment
    implements RecipeListAdapter.OnClickItemBtnAddListener
{
    private FragmentPlanDetailSearchRecipeBinding $;
    MealPlanDAO mealPlanDAO;
    long mealPlanId = -1;
    MealPlan mealPlan;
    RecipeDAO recipeDAO;
    List<Recipe> foundRecipes;
    RecyclerView.Adapter recyclerSearchFoundRecipesAdapter;
    String keyword = "";

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
        initMealPlan();
        recyclerSearchFoundRecipesAdapter = new RecipeListAdapter(foundRecipes, mealPlan, requireContext(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        $.recyclerSearchFoundRecipes.setAdapter(recyclerSearchFoundRecipesAdapter);
        $.recyclerSearchFoundRecipes.setLayoutManager(layoutManager);

        $.incSearchBar.btnSearch.setOnClickListener(v -> {
            keyword = $.incSearchBar.inpKeywords.getText().toString().trim();
            System.out.println("Hit Search Button: " + keyword);
            if (keyword.isEmpty()) {
                System.out.println("Keyword empty: " + keyword);
                return;
            }
            searchRecipes();
        });
    }

    @Override
    public void onClickItemBtnAdd(int position) {
        System.out.println("Add button clicked on position: " + position);
        Recipe addedRecipe = foundRecipes.get(position);
        if (addedRecipe == null || mealPlan == null) {
            return;
        }
        List<Integer> recipeIds = mealPlan.getRecipeIdsList();
        // check if id is already in the list or not
        if (recipeIds.contains(addedRecipe.getId())) {
            return;
        }
        recipeIds.add(addedRecipe.getId());
        try {
            mealPlan.setRecipeIdsList(recipeIds);
            mealPlanDAO.update(mealPlan);
            Toast.makeText(requireContext(), "Recipe added to plan", Toast.LENGTH_SHORT).show();
            recyclerSearchFoundRecipesAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initMealPlan() {
        mealPlanDAO = new MealPlanDAO(requireContext());
        recipeDAO = new RecipeDAO(requireContext());
        mealPlanId = PlanDetailFragmentArgs.fromBundle(getArguments()).getArgMealPlanId();
        Log.d("PlanDetailFragment", "Meal Plan ID: " + mealPlanId);
        if (mealPlanId == -1) {
            Toast.makeText(requireContext(), "Meal Plan not found", Toast.LENGTH_SHORT).show();
            return;
        }
        mealPlan = mealPlanDAO.getById((int) mealPlanId);
        if (mealPlan == null) {
            Toast.makeText(requireContext(), "Meal Plan not found", Toast.LENGTH_SHORT).show();
            return;
        }
        foundRecipes = new ArrayList<>();
        showExistingPlanComponents(mealPlan.getName());
    }

    /**
     *
     * @param planName
     */
    public void showExistingPlanComponents(String planName) {
        System.out.println("Plan Name: " + planName);

        $.incPlanHeader.linearPlanNameDisplay.setVisibility(View.VISIBLE);
        $.incPlanHeader.linearPlanNameInput.setVisibility(View.GONE);
        $.incPlanHeader.txtPlanName.setText(planName);
        $.incPlanHeader.btnEditPlanName.setVisibility(View.VISIBLE);
    }

    private void searchRecipes() {
        RecipeDAO recipeDAO = new RecipeDAO(requireContext());
        List<Recipe> recipes = recipeDAO.searchRecipes(keyword);
        System.out.println("Searching for keywords: " + keyword);
        if (!recipes.isEmpty() && recipes.size() >= 5) {
            System.out.println("Recipes found: " + recipes.size());
            updateRecyclerView(recipes);
        } else {
            fetchRecipesFromAPI();
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
     */
    private void fetchRecipesFromAPI() {
        SpoonacularApiService apiService = RetrofitClient.getClient().create(SpoonacularApiService.class);
        String apiKey = RetrofitClient.getApiKey();
        Log.d("API_KEY_TEST", "Retrieved API Key: " + apiKey);
        Log.d("API_KEY_TEST", "Searching for keywords: " + keyword);

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
            recipeDAO.insertOrUpdateRecipe(recipe, keyword);
        }
    }

    private void updateRecyclerView(List<Recipe> recipes) {
        foundRecipes.clear();
        foundRecipes.addAll(recipes);
        recyclerSearchFoundRecipesAdapter.notifyDataSetChanged();
    }

}