package com.example.mealclue.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealclue.R;
import com.example.mealclue.controller.MealPlanDAO;
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.Recipe;
import com.example.mealclue.view.adapters.RecipeListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import com.example.mealclue.databinding.FragmentPlanDetailBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlanDetailFragment extends Fragment implements RecipeListAdapter.OnClickItemBtnAddListener {
    private FragmentPlanDetailBinding $;
    MealPlanDAO mealPlanDAO;
    long mealPlanId = -1;
    MealPlan mealPlan;
    RecipeDAO recipeDAO;

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
        mealPlanDAO = new MealPlanDAO(requireContext());
        recipeDAO = new RecipeDAO(requireContext());
        mealPlanId = PlanDetailFragmentArgs.fromBundle(getArguments()).getArgMealPlanId();
        Log.d("PlanDetailFragment", "Meal Plan ID: " + mealPlanId);
        if (mealPlanId == -1) {
            mealPlan = new MealPlan("", 1, "[]", false);
        } else {
            mealPlan = mealPlanDAO.getById((int) mealPlanId);
            if (mealPlan == null) {
                Toast.makeText(requireContext(), "Meal Plan not found", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        populateMealPlan(mealPlanId, mealPlan);

        $.incPlanHeader.btnEditPlanName.setOnClickListener(v -> {
            showPlanNameInput(mealPlan.getName());
        });
        $.incPlanHeader.btnUpdatePlanName.setOnClickListener(v -> {
            String planName = $.incPlanHeader.inpPlanName.getText().toString().trim();
            if (planName.isEmpty()) {
                return;
            }
            mealPlan.setName(planName);
            mealPlanId = insertOrUpdateMealPlan(mealPlanId, mealPlan);
            populateMealPlan(mealPlanId, mealPlan);
        });
        $.incBotButtons.btnAddNewRecipe.setOnClickListener(v -> {
            PlanDetailFragmentDirections.ActionFrgPlanDetailToFrgPlanDetailSearchRecipe action = PlanDetailFragmentDirections.actionFrgPlanDetailToFrgPlanDetailSearchRecipe();
            action.setArgMealPlanId(mealPlanId);
            Navigation.findNavController(v).navigate(action);
        });
    }


    public void populateMealPlan(long mealPlanId, MealPlan mealPlan) {
        if (mealPlanId == -1) {
            showPlanNameInput("");
            return;
        }
        List<Integer> recipeIds = mealPlan.getRecipeIdsList();
        showExistingPlanComponents(mealPlan.getName());
        if (recipeIds.isEmpty()) {
            $.incPlanHeader.linearGoalSetter.setVisibility(View.GONE);
        } else {
            $.incPlanHeader.linearGoalSetter.setVisibility(View.VISIBLE);
        }

        List<Recipe> recipes = new ArrayList<>();
        for (int id : recipeIds) {
            Recipe recipe = recipeDAO.getRecipeById(id);
            if (recipe != null) recipes.add(recipe);
        }
        $.recyclerAddedRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));
        RecipeListAdapter adapter = new RecipeListAdapter(recipes, mealPlan, requireContext(), this);
        $.recyclerAddedRecipes.setAdapter(adapter);
    }

    /**
     *
     */
    public void showPlanNameInput(String planName) {
        $.incPlanHeader.linearPlanNameDisplay.setVisibility(View.GONE);
        $.incPlanHeader.linearPlanNameInput.setVisibility(View.VISIBLE);
        $.incPlanHeader.inpPlanName.setText(planName);
        $.incPlanHeader.inpPlanName.requestFocus();
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
        $.incBotButtons.btnAddNewRecipe.setVisibility(View.VISIBLE);
        $.incBotButtons.spacer.setVisibility(View.VISIBLE);
    }

    public long insertOrUpdateMealPlan(long mealPlanId, MealPlan mealPlan) {
        long newId = -1;
        if (mealPlanId == -1) {
            newId = mealPlanDAO.insert(mealPlan);
            if (newId == -1) {
                Toast.makeText(requireContext(), "Error inserting meal plan", Toast.LENGTH_SHORT).show();
            }
        } else {
            int updated = mealPlanDAO.update(mealPlan);
            if (updated == 0) {
                Toast.makeText(requireContext(), "Error updating meal plan", Toast.LENGTH_SHORT).show();
            }
            newId = mealPlanId;
        }
        return newId;
    }

    @Override
    public void onClickItemBtnAdd(int position) {

    }
}