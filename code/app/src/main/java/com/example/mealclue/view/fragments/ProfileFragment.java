package com.example.mealclue.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private Context context;
    private Recipe recipe;
    Integer uncookedRecipeId;

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
        context = requireContext();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * I place code here because on onCreate, the binding  for Header Include is not ready
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = loadUser();
        if (user == null) {
            return;
        }

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

            $.txtPlanName.setOnClickListener(v -> {
                ProfileFragmentDirections.ActionFrgProfileToFrgPlanDetail action = ProfileFragmentDirections.actionFrgProfileToFrgPlanDetail();
                action.setArgMealPlanId(goalPlan.getId());
                NavController navController = Navigation.findNavController(v);
                navController.navigate(action);
            });


            $.txtPlanName.setText(goalPlan.getName());
            uncookedRecipeId = goalPlan.getFirstUncookedRecipeId();

            if (uncookedRecipeId == null) {
                $.txtRecipeName.setText(R.string.finished_all_recipes);
                $.btnCook.setText(R.string.reset_plan);
            } else {
                recipe = recipeDAO.getRecipeById(uncookedRecipeId);
                if (recipe != null && recipe.getImage() != null) {
                    Glide.with(requireContext())
                            .load(recipe.getImage())
                            .into($.imgRecipeThumb);

                }
                $.txtRecipeName.setText(recipe.getTitle());
                $.linearCurRecipe.setOnClickListener(v -> {
                    moveToRecipeDetail(v, recipe);
                });
            }

            $.btnCook.setOnClickListener(v -> {
                if (uncookedRecipeId != null) {
                    moveToRecipeDetail(v, recipe);
                    return;
                }
                goalPlan.setCookedRecipes(null);
                mealPlanDAO.update(goalPlan);
                uncookedRecipeId = goalPlan.getFirstUncookedRecipeId();
                recipe = recipeDAO.getRecipeById(uncookedRecipeId);
                if (recipe != null && recipe.getImage() != null) {
                    Glide.with(requireContext())
                            .load(recipe.getImage())
                            .into($.imgRecipeThumb);
                }
                $.txtRecipeName.setText(recipe.getTitle());
                $.btnCook.setText(R.string.cook);
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

    public User loadUser() {
        SharedPreferences prefs = context.getSharedPreferences(getString(R.string.k_meal_clue_prefs), MODE_PRIVATE);
        int savedUserId = prefs.getInt(getString(R.string.k_logged_in_user_id), -1);
        if (savedUserId == -1) {
            Toast.makeText(context, "User should log in", Toast.LENGTH_SHORT).show();
            return null;
        }
        UserDAO userDAO = new UserDAO(context);
        if (userDAO.count() == 0) {
            Toast.makeText(context, "Empty User Base", Toast.LENGTH_SHORT).show();
            return null;
        }
        // for demo, I use first user at this time
        User user = userDAO.getUserById(savedUserId);
        if (user == null) {
            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
            return null;
        }

        $.incUser.txtFullName.setText(user.getFullName());
        $.incUser.txtHeartCount.setText(String.format("%s hearts", user.getHearts()));
        if (user.getAvatar() != null) {
            Glide.with(this).load(user.getAvatar()).into($.incUser.imgAvatar);
        }
        return user;
    }
}