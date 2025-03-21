package com.example.mealclue.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.mealclue.R;
import com.example.mealclue.controller.MealPlanDAO;
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.controller.UserDAO;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.Recipe;
import com.example.mealclue.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import com.example.mealclue.databinding.FragmentProfileBinding;

import java.util.List;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding $;
    private RecipeDAO recipeDAO;
    MealPlanDAO mealPlanDAO;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public void onResume() {
        super.onResume();

    }

    /**
     * I place code here because on onCreate, the binding  for Header Include is not ready
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDAO userDAO = new UserDAO(requireContext());
        if (userDAO.count() == 0) {
            userDAO.insertMockUser();
        }
        // for demo, I use first user at this time
        User user = userDAO.getAll().get(0);

        $.incUser.txtFullName.setText(user.getFullName());
        $.incUser.txtHeartCount.setText(String.format("%s hearts", user.getHearts()));

        mealPlanDAO = new MealPlanDAO(requireContext());
        recipeDAO = new RecipeDAO(requireContext());
        List<MealPlan> goalPlans = mealPlanDAO.getGoaledByUser(user.getId());
        if (goalPlans.isEmpty()) {
            $.linearPlanGoal.setVisibility(View.GONE);
            $.linearNoGoal.setVisibility(View.VISIBLE);
        } else {
            $.linearPlanGoal.setVisibility(View.VISIBLE);
            $.linearNoGoal.setVisibility(View.GONE);
            MealPlan goalPlan = goalPlans.get(0);
            List<Integer> recipeIds = goalPlan.getRecipeIdsList();
            $.txtPlanName.setText(goalPlan.getName());
            if (!recipeIds.isEmpty()) {
                Recipe firstRecipe = recipeDAO.getRecipeById(recipeIds.get(0));
                if (firstRecipe != null && firstRecipe.getImage() != null) {
                    Glide.with(requireContext())
                            .load(firstRecipe.getImage())
                            .into($.imgRecipeThumb);

                }
                $.txtRecipeName.setText(firstRecipe.getTitle());
                $.linearCurRecipe.setOnClickListener(v -> {
                    moveToRecipeDetail(v, firstRecipe);
                });
                $.btnCook.setOnClickListener(v -> {
                    moveToRecipeDetail(v, firstRecipe);
                });
            }

            $.txtPlanName.setOnClickListener(v -> {
                ProfileFragmentDirections.ActionFrgProfileToFrgPlanDetail action = ProfileFragmentDirections.actionFrgProfileToFrgPlanDetail();
                action.setArgMealPlanId(goalPlan.getId());
                NavController navController = Navigation.findNavController(v);
                navController.navigate(action);
            });

        }

        $.btnSetGoalPlan.setOnClickListener(this::moveToPlanListToSetGoal);
    }

    private void moveToRecipeDetail(View view, Recipe recipe) {
        ProfileFragmentDirections.ActionFrgProfileToFrgRecipeDetail action = ProfileFragmentDirections.actionFrgProfileToFrgRecipeDetail();
        action.setArgRecipeId(recipe.getId());
        NavController navController = Navigation.findNavController(view);
        navController.navigate(action);
    }

    private void moveToPlanListToSetGoal(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_frgProfile_to_frgPlanList);
//
//        ProfileFragmentDirections.ActionFrgProfileToFrgPlanDetail action = ProfileFragmentDirections.actionFrgProfileToFrgPlanDetail();
//        action.setArgMealPlanId(-1);
//        NavController navController = Navigation.findNavController(v);
//        navController.navigate(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        $ = FragmentProfileBinding.inflate(inflater, container, false);
        return $.getRoot();
    }
}