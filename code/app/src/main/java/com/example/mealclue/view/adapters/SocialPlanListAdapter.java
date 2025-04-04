package com.example.mealclue.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealclue.controller.HeartedMealPlanDAO;
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.databinding.RecyclerItemSocialPlanOverlayBinding;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.Recipe;
import com.example.mealclue.view.fragments.SocialFragmentDirections;

import java.util.List;

public class SocialPlanListAdapter extends RecyclerView.Adapter<SocialPlanListAdapter.ViewHolder> {
    private List<MealPlan> plans;
    private RecipeDAO recipeDAO;
    private HeartedMealPlanDAO heartedMealPlanDAO;
    private Context context;

    public SocialPlanListAdapter(Context context, List<MealPlan> plans) {
        this.plans = plans;
        this.recipeDAO = new RecipeDAO(context);
        this.context = context;
        this.heartedMealPlanDAO = new HeartedMealPlanDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemSocialPlanOverlayBinding binding = RecyclerItemSocialPlanOverlayBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealPlan plan = plans.get(position);
        holder.$.txtName.setText(plan.getName());

        List<Integer> recipeIds = plan.getRecipeIdsList();
        if (!recipeIds.isEmpty()) {
            Recipe firstRecipe = recipeDAO.getRecipeById(recipeIds.get(0));
            if (firstRecipe != null && firstRecipe.getImage() != null) {
                Glide.with(holder.itemView.getContext())
                        .load(firstRecipe.getImage())
                        .into(holder.$.imgThumb);
            }
        }

        int recipeCount = recipeIds.size();
        if (recipeCount < 1) {
            holder.$.txtRecipeCount.setVisibility(View.GONE);
        } else {
            if (recipeCount == 1) {
                holder.$.txtRecipeCount.setText(String.format("%s recipe", recipeCount));

            } else {
                holder.$.txtRecipeCount.setText(String.format("%s recipes", recipeCount));
            }
            holder.$.txtRecipeCount.setVisibility(View.VISIBLE);
        }

        int heartCount = heartedMealPlanDAO.countHeartsForPlan(plan.getId());
        if (heartCount == 0) {
            holder.$.txtHeartCount.setVisibility(View.GONE);
        } else {
            holder.$.txtHeartCount.setVisibility(View.VISIBLE);
            holder.$.txtHeartCount.setText(String.format(
                    "%s ❤", heartCount
            ));
        }

        holder.$.item.setOnClickListener(v -> {
            SocialFragmentDirections.ActionFrgSocialToFrgPlanDetail action =
                    SocialFragmentDirections.actionFrgSocialToFrgPlanDetail();
            action.setArgMealPlanId(plan.getId());
            action.setArgFromSocial(true);
            NavController navController = Navigation.findNavController(v);
            navController.navigate(action);
        });
    }


    @Override
    public int getItemCount() {
        return plans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerItemSocialPlanOverlayBinding $;

        public ViewHolder(@NonNull RecyclerItemSocialPlanOverlayBinding binding) {
            super(binding.getRoot());
            this.$ = binding;
        }
    }
}
