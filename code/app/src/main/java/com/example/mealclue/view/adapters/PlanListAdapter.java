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
import com.example.mealclue.controller.RecipeDAO;
import com.example.mealclue.databinding.RecyclerItemRecipeOrPlanOverlayBinding;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.Recipe;
import com.example.mealclue.view.fragments.PlanListFragmentDirections;

import java.util.List;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {
    private List<MealPlan> plans;
    private RecipeDAO recipeDAO;

    public PlanListAdapter(Context context, List<MealPlan> plans) {
        this.plans = plans;
        this.recipeDAO = new RecipeDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemRecipeOrPlanOverlayBinding binding = RecyclerItemRecipeOrPlanOverlayBinding.inflate(
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
        int recipeDoneCount = plan.getCookedRecipeIdsList().size();
        int recipeDonePercent = (recipeDoneCount * 100) / recipeCount;
        int recipeLeftCount = recipeCount - recipeDoneCount;
        String ribbonText = "";
        if (plan.isGoal()) {
            ribbonText += "Goal";
        }
        if (plan.isPrivate()) {
            if (!ribbonText.isEmpty()) {
                ribbonText += " - ";
            }
            ribbonText += "Private";
        }
        if (ribbonText.isEmpty()) {
            holder.$.txtRibbon.setVisibility(View.GONE);
        } else {
            holder.$.txtRibbon.setVisibility(View.VISIBLE);
            holder.$.txtRibbon.setText(ribbonText);
        }

        // at this time we force to show all the details
        if (true || plan.isGoal()) {
            holder.$.txtName.setText(
                    String.format("%s - %s%%", plan.getName(), recipeDonePercent)
            );
            holder.$.txtMeta.setText(
                    String.format("%s recipes - %s", recipeCount, recipeLeftCount > 0 ? recipeLeftCount + " left": "Finished")
            );
        } else {
            holder.$.txtMeta.setText(
                    String.format("%s recipes", plan.getRecipeIdsList().size())
            );
        }

        holder.$.item.setOnClickListener(v -> {
            PlanListFragmentDirections.ActionFrgPlanListToFrgPlanDetail action = PlanListFragmentDirections.actionFrgPlanListToFrgPlanDetail();
            action.setArgMealPlanId(plan.getId());
            NavController navController = Navigation.findNavController(v);
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerItemRecipeOrPlanOverlayBinding $;

        public ViewHolder(@NonNull RecyclerItemRecipeOrPlanOverlayBinding binding) {
            super(binding.getRoot());
            this.$ = binding;
        }
    }
}
