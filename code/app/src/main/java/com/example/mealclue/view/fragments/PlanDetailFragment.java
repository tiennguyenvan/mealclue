package com.example.mealclue.view.fragments;

import android.content.Context;
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
import com.example.mealclue.controller.HeartedMealPlanDAO;
import com.example.mealclue.controller.MealPlanDAO;
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.controller.UserDAO;
import com.example.mealclue.model.HeartedMealPlan;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.Recipe;
import com.example.mealclue.model.User;
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

public class PlanDetailFragment extends Fragment implements RecipeListAdapter.MainActionListener {
    private FragmentPlanDetailBinding $;
    private MealPlanDAO mealPlanDAO;
    private long mealPlanId = -1;
    private MealPlan mealPlan;
    private RecipeDAO recipeDAO;
    private List<Recipe> mealPlanRecipes;
    private Context context;
    private User loggedInUser;
    private Boolean isFromSocial = false;
    private UserDAO userDAO;

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
        context = requireContext();
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
        loggedInUser = User.getLoggedInUser(context);
        mealPlanDAO = new MealPlanDAO(requireContext());
        recipeDAO = new RecipeDAO(requireContext());
        userDAO = new UserDAO(requireContext());
        mealPlanId = PlanDetailFragmentArgs.fromBundle(getArguments()).getArgMealPlanId();
        isFromSocial = PlanDetailFragmentArgs.fromBundle(getArguments()).getArgFromSocial();
        Log.d("PlanDetailFragment", "Meal Plan ID: " + mealPlanId);
        mealPlanRecipes = new ArrayList<>();

        loadMealPlanRecipes();
        populateMealPlan();

        if (isFromSocial) {
            $.incPlanHeader.btnEditPlanName.setVisibility(View.GONE);
            $.incPlanHeader.btnToggleSetGoal.setVisibility(View.GONE);
            $.incPlanHeader.btnTogglePrivate.setVisibility(View.GONE);
//            $.incPlanHeader.txtGoalRibbon.setVisibility(View.GONE);

            $.incBotButtons.btnAddNewRecipe.setVisibility(View.GONE);
            $.incBotButtons.spacer.setVisibility(View.GONE);
            $.incBotButtons.btnFork.setVisibility(View.VISIBLE);
            $.incBotButtons.btnHeart.setVisibility(View.VISIBLE);
        } else {
            $.incPlanHeader.btnEditPlanName.setOnClickListener(v -> {
                showPlanNameInput(mealPlan.getName());
            });
        }

        $.incPlanHeader.btnUpdatePlanName.setOnClickListener(this::onClickUpdatePlanName);
        $.incPlanHeader.btnDeletePlan.setOnClickListener(this::onClickDeletePlan);
        $.incBotButtons.btnAddNewRecipe.setOnClickListener(this::onClickAddNewRecipe);
        $.incPlanHeader.btnToggleSetGoal.setOnClickListener(this::setMealPlanAsAGoal);
        $.incPlanHeader.btnTogglePrivate.setOnClickListener(this::setMealPlanAsPrivate);
        $.incBotButtons.btnFork.setOnClickListener(this::onClickForkPlan);
        $.incBotButtons.btnHeart.setOnClickListener(this::onClickHeart);


        $.incBotButtons.btnBack.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void onClickDeletePlan(View v) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete Meal Plan")
                .setMessage("Are you sure you want to delete this meal plan?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    mealPlanDAO.delete((int) mealPlanId);
                    Navigation.findNavController(requireView()).navigateUp();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void onClickHeart(View view) {
        HeartedMealPlan hearted = new HeartedMealPlan(loggedInUser.getId(), (int) mealPlanId);
        HeartedMealPlanDAO heartedDAO = new HeartedMealPlanDAO(requireContext());
        User planOwner = userDAO.getUserById(mealPlan.getUserId());
        int heartCount = planOwner.getHearts();
        if (heartedDAO.isHearted(hearted.getUserId(), hearted.getMealPlanId())) {
            heartedDAO.delete(hearted.getUserId(), hearted.getMealPlanId());
            Toast.makeText(requireContext(), "Meal Plan unhearted", Toast.LENGTH_SHORT).show();
            heartCount--;
            if (heartCount < 0) heartCount = 0;
        } else {
            heartedDAO.insert(hearted);
            Toast.makeText(requireContext(), "Meal Plan hearted", Toast.LENGTH_SHORT).show();
            heartCount++;
        }
        planOwner.setHearts(heartCount);
        userDAO.update(planOwner);
        Navigation.findNavController(view).navigateUp();
    }

    private void onClickForkPlan(View view) {
        MealPlan clonedMealPlan = new MealPlan(
                mealPlan.getName(),
                loggedInUser.getId(),
                mealPlan.getRecipes(),
                false
        );
        long newId = mealPlanDAO.insert(clonedMealPlan);
        if (newId == -1) {
            Toast.makeText(requireContext(), "Error forking meal plan", Toast.LENGTH_SHORT).show();
            return;
        }

        Navigation.findNavController(view).navigate(
                PlanDetailFragmentDirections.actionFrgPlanDetailToFrgPlanList()
        );
    }

    private void loadMealPlanRecipes() {
        if (mealPlanId == -1) {
            mealPlan = new MealPlan("", loggedInUser.getId(), "[]", false);
        } else {
            mealPlan = mealPlanDAO.getById((int) mealPlanId);
            if (mealPlan == null) {
                Toast.makeText(requireContext(), "Meal Plan not found", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Integer> recipeIds = mealPlan.getRecipeIdsList();
            for (int id : recipeIds) {
                Recipe recipe = recipeDAO.getRecipeById(id);
                if (recipe != null) mealPlanRecipes.add(recipe);
            }
        }
    }

    private void onClickUpdatePlanName(View view) {
        String planName = $.incPlanHeader.inpPlanName.getText().toString().trim();
        if (planName.isEmpty()) {
            return;
        }
        mealPlan.setName(planName);
        mealPlanId = insertOrUpdateMealPlanToDB();
        populateMealPlan();
    }

    private void onClickAddNewRecipe(View view) {
        PlanDetailFragmentDirections.ActionFrgPlanDetailToFrgPlanDetailSearchRecipe action = PlanDetailFragmentDirections.actionFrgPlanDetailToFrgPlanDetailSearchRecipe();
        action.setArgMealPlanId(mealPlanId);
        Navigation.findNavController(view).navigate(action);
    }

    private void decoratePrivateButton(boolean isPrivate) {
        if (isPrivate) {
            $.incPlanHeader.btnTogglePrivate.setText(R.string.set_as_public);
            $.incPlanHeader.btnTogglePrivate.setBackground(
                    getResources().getDrawable(R.drawable.button_primary, null)
            );
            return;
        }
        $.incPlanHeader.btnTogglePrivate.setText(R.string.set_as_private);
        $.incPlanHeader.btnTogglePrivate.setBackground(
                getResources().getDrawable(R.drawable.button_tertiary, null)
        );
    }

    private void setMealPlanAsPrivate(View view) {
        boolean newIsPrivate = !mealPlan.isPrivate();
        decoratePrivateButton(newIsPrivate);
        mealPlan.setPrivate(newIsPrivate);
        insertOrUpdateMealPlanToDB();
    }

    private void decorateGoalButton(boolean isGoal) {
        if (isGoal) {
            $.incPlanHeader.btnToggleSetGoal.setText(R.string.unset_as_goal);
            $.incPlanHeader.btnToggleSetGoal.setBackground(
                    getResources().getDrawable(R.drawable.button_tertiary, null)
            );
            return;
        }
        $.incPlanHeader.btnToggleSetGoal.setText(R.string.set_as_goal);
        $.incPlanHeader.btnToggleSetGoal.setBackground(
                getResources().getDrawable(R.drawable.button_primary, null)
        );
    }

    private void setMealPlanAsAGoal(View view) {
        boolean newIsGoal = !mealPlan.isGoal();
        decorateGoalButton(newIsGoal);
        // each user should have only goal
        if (newIsGoal) {
            mealPlanDAO.clearAllGoals(mealPlan.getUserId());
        }
        mealPlan.setGoal(newIsGoal);
        insertOrUpdateMealPlanToDB();
    }


    public void populateMealPlan() {
        if (mealPlanId == -1) {
            showPlanNameInput("");
            return;
        }
        if (mealPlan == null) {
            return;
        }

        decorateGoalButton(mealPlan.isGoal());
//        System.out.println("XXXXX mealPlan.isPrivate() = " +mealPlan.isPrivate());
        decoratePrivateButton(mealPlan.isPrivate());

        showExistingPlanComponents(mealPlan.getName());
        if (mealPlanRecipes.isEmpty()) {
            $.incPlanHeader.linearGoalSetter.setVisibility(View.GONE);
        } else {
            $.incPlanHeader.linearGoalSetter.setVisibility(View.VISIBLE);
        }

        $.recyclerAddedRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));
        RecipeListAdapter adapter;
        if (isFromSocial) {
            adapter = new RecipeListAdapter(mealPlanRecipes, null, requireContext(), null, false);
        } else {
            adapter = new RecipeListAdapter(mealPlanRecipes, mealPlan, requireContext(), this, false);
        }
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

    public long insertOrUpdateMealPlanToDB() {
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
    public void onRecyclerMainAction(int position) {

    }

    @Override
    public void onRecyclerRecipeListChange() {
        List<Integer> recipeIds = new ArrayList<>();
        for (Recipe recipe : mealPlanRecipes) {
            recipeIds.add(recipe.getId());
        }
        try {
            mealPlan.setRecipeIdsList(recipeIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        insertOrUpdateMealPlanToDB();
    }

}