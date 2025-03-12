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

import com.example.mealclue.R;
import com.example.mealclue.controller.MealPlanDAO;
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.controller.RetrofitClient;
import com.example.mealclue.controller.SpoonacularApiService;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.Recipe;
import com.example.mealclue.model.RecipeResponse;
import com.example.mealclue.view.adapters.CategoryAdapter;
import com.example.mealclue.view.adapters.RecipeListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import com.example.mealclue.databinding.FragmentPlanDetailBinding;
import com.example.mealclue.view.adapters.SubCategoryAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class PlanDetailFragment extends Fragment {
    private FragmentPlanDetailBinding $;
    MealPlanDAO mealPlanDAO;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlanDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanDetailFragment newInstance(String param1, String param2) {
        PlanDetailFragment fragment = new PlanDetailFragment();
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
        $ = FragmentPlanDetailBinding.inflate(inflater, container, false);
        return $.getRoot();
    }

    public void clearBottomNavSelection() {
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.navMainMenu);
//            bottomNav.setSelectedItemId(R.id.frgPlanList);
        bottomNav.getMenu().setGroupCheckable(0, true, false); // Disable selection behavior
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setChecked(false); // Uncheck all items
        }
        bottomNav.getMenu().setGroupCheckable(0, true, true); // Re-enable selection behavior
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearBottomNavSelection();

        long mealPlanId = PlanDetailFragmentArgs.fromBundle(getArguments()).getArgMealPlanId();
        Log.d("PlanDetailFragment", "Meal Plan ID: " + mealPlanId);
        mealPlanDAO = new MealPlanDAO(requireContext());
        if (mealPlanId != -1) {
            populateMealPlan(mealPlanId);
            return;
        }
        createNewPlan();
    }

    /**
     *
     * @param mealPlanId
     */
    public void populateMealPlan(long mealPlanId) {
        MealPlan mealPlan = mealPlanDAO.getById((int) mealPlanId);
        if (mealPlan == null) {
            Toast.makeText(requireContext(), "Meal Plan not found", Toast.LENGTH_SHORT).show();
            return;
        }

        $.incPlanHeader.txtPlanName.setText(mealPlan.getName());
        $.incPlanHeader.txtPlanName.setVisibility(View.VISIBLE);
        $.incPlanHeader.btnEditPlanName.setVisibility(View.VISIBLE);
        $.incPlanHeader.inpPlanName.setVisibility(View.GONE);
        $.incPlanHeader.linearGoalSetter.setVisibility(View.VISIBLE);

        List<Integer> recipeIds = mealPlan.getRecipeIdsList();
        if (recipeIds.isEmpty()) {
            Toast.makeText(requireContext(), "No Recipes Found", Toast.LENGTH_SHORT).show();
            return;
        }
        RecipeDAO recipeDAO = new RecipeDAO(requireContext());
        List<Recipe> recipes = new ArrayList<>();
        for (int id : recipeIds) {
            Recipe recipe = recipeDAO.getRecipeById(id);
            if (recipe != null) recipes.add(recipe);
        }

        $.recyclerAddedRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));
        RecipeListAdapter adapter = new RecipeListAdapter(recipes, requireContext());
        $.recyclerAddedRecipes.setAdapter(adapter);
    }


    /**
     *
     */
    public void createNewPlan() {
        $.incPlanHeader.btnEditPlanName.setVisibility(View.GONE);
        $.incPlanHeader.txtPlanName.setVisibility(View.GONE);
        $.incPlanHeader.inpPlanName.setVisibility(View.VISIBLE);

        $.incPlanHeader.inpPlanName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                return;
            }
            String planName = $.incPlanHeader.inpPlanName.getText().toString().trim();
            if (planName.isEmpty()) {
                return;
            }
            // Save new plan
            MealPlan newPlan = new MealPlan(planName, 1, "[]", false); // User ID 1 (replace as needed)
            long newId = mealPlanDAO.insert(newPlan);

            if (newId == -1) {
                Toast.makeText(requireContext(), "Failed to create new plan", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("PlanDetailFragment", "New Plan Created with ID: " + newId);
            $.incPlanHeader.txtPlanName.setText(planName);
            $.incPlanHeader.txtPlanName.setVisibility(View.VISIBLE);
            $.incPlanHeader.btnEditPlanName.setVisibility(View.VISIBLE);
            $.incPlanHeader.inpPlanName.setVisibility(View.GONE);
            $.incPlanHeader.linearGoalSetter.setVisibility(View.VISIBLE);
            $.incBotButtons.btnAddNewRecipe.setVisibility(View.VISIBLE);
            $.incBotButtons.spacer.setVisibility(View.VISIBLE);
        });
    }
}