package com.example.mealclue.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.controller.RetrofitClient;
import com.example.mealclue.controller.SpoonacularApiService;
import com.example.mealclue.model.Ingredient;
import com.example.mealclue.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import com.example.mealclue.databinding.FragmentRecipeDetailBinding;
import com.example.mealclue.view.adapters.IngredientAdapter;
import com.example.mealclue.view.adapters.StepSelectorAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailFragment extends Fragment {
    private FragmentRecipeDetailBinding $;
    private RecipeDAO recipeDAO;
    private int recipeId;
    private Recipe currentRecipe;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailFragment newInstance(String param1, String param2) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
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
        // Inflate the layout for this fragment
        $ = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        return $.getRoot();
//        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recipeId = RecipeDetailFragmentArgs.fromBundle(getArguments()).getArgRecipeId();
        System.out.println("recipeId: " +recipeId);
        recipeDAO = new RecipeDAO(requireContext());
        currentRecipe = recipeDAO.getRecipeById(recipeId);

        if (currentRecipe == null) {
            Toast.makeText(requireContext(), "Recipe not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentRecipe.getInstructions() == null && currentRecipe.getIngredients() == null) {
            System.out.println(currentRecipe);
            fetchRecipeDetailsFromAPI(recipeId);
        } else {
            showRecipe();
        }
    }

    private void showRecipe() {
        $.txtRecipeName.setText(currentRecipe.getTitle());
        Glide.with(this).load(currentRecipe.getImage()).into($.imgRecipeThumb);

        try {
            List<Ingredient> ingredients = currentRecipe.getIngredientList();
            $.recyclerIngredients.setLayoutManager(new LinearLayoutManager(requireContext()));
            $.recyclerIngredients.setAdapter(new IngredientAdapter(ingredients));

            String[] steps = currentRecipe.getInstructionsArray();
            List<Integer> stepNumbers = new ArrayList<>();
            for (int i = 0; i < steps.length + 1; i++) {
                stepNumbers.add(i + 1); // +1 for ingredients
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            StepSelectorAdapter adapter = new StepSelectorAdapter(stepNumbers, selectedStep -> showStep(currentRecipe, selectedStep));
            $.recyclerRecipePrepareSteps.setLayoutManager(layoutManager);
            $.recyclerRecipePrepareSteps.setAdapter(adapter);

            // default to step 1
            showStep(currentRecipe, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showStep(Recipe recipe, int stepIndex) {
        $.txtInstruction.setVisibility(View.GONE);
        $.recyclerIngredients.setVisibility(View.GONE);

        try {
            if (stepIndex == 1) {
                $.recyclerIngredients.setVisibility(View.VISIBLE);
            } else {
                String[] steps = recipe.getInstructionsArray();
                int stepPos = stepIndex - 2;
                if (stepPos >= 0 && stepPos < steps.length) {
                    $.txtInstruction.setText(steps[stepPos]);
                    $.txtInstruction.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    private void fetchRecipeDetailsFromAPI(int id) {
        SpoonacularApiService api = RetrofitClient.getClient().create(SpoonacularApiService.class);
        api.getRecipeInformation(id, RetrofitClient.getApiKey()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("Called API Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject body = response.body();



//                    int recipeId = body.get("id").getAsInt();
//                    String title = body.get("title").getAsString();
//                    String image = body.get("image").getAsString();

                    JsonArray extendedIngredients = body.getAsJsonArray("extendedIngredients");
                    JSONArray ingredientJsonArray = new JSONArray();
                    for (JsonElement e : extendedIngredients) {
                        JsonObject obj = e.getAsJsonObject();
                        JSONObject ingObj = new JSONObject();
                        try {
                            ingObj.put("name", obj.get("name").getAsString());
                            ingObj.put("amount", obj.get("amount").getAsString());
                            ingObj.put("unit", obj.get("unit").getAsString());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        ingredientJsonArray.put(ingObj);
                    }

                    // Parse steps from <li> HTML
                    String instructionHtml = body.get("instructions").getAsString();
                    System.out.println("instructionHtml:\n" + instructionHtml);
                    List<String> steps = new ArrayList<>();
                    Matcher matcher = Pattern.compile("(?i)<li>(.*?)</li>").matcher(instructionHtml);
                    while (matcher.find()) {
                        steps.add(matcher.group(1).trim());
                    }
                    JSONArray instructionsArray = new JSONArray(steps);

//                    Recipe recipe = new Recipe(recipeId, title, image);

                    currentRecipe.setIngredients(ingredientJsonArray.toString());
                    currentRecipe.setInstructions(instructionsArray.toString());

                    System.out.println("ingredientJsonArray: " + ingredientJsonArray.toString());
                    System.out.println("instructionsArray: " + instructionsArray.toString());

                    recipeDAO.insertOrUpdateRecipe(currentRecipe, null);
                    showRecipe();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(requireContext(), "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}