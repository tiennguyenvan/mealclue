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
import com.example.mealclue.databinding.RecyclerItemRecipeOverlayBinding;
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
        RecyclerItemRecipeOverlayBinding binding = RecyclerItemRecipeOverlayBinding.inflate(
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

        if (plan.isGoal()) {
            holder.$.txtGoalRibbon.setVisibility(View.VISIBLE);
            holder.$.txtName.setText(plan.getName() + " - 80%");
            holder.$.txtMeta.setText("30 recipes - 3 left");
        } else {
            holder.$.txtGoalRibbon.setVisibility(View.GONE);
            holder.$.txtMeta.setText(String.format("%s recipes", plan.getRecipeIdsList().size()));
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
        RecyclerItemRecipeOverlayBinding $;

        public ViewHolder(@NonNull RecyclerItemRecipeOverlayBinding binding) {
            super(binding.getRoot());
            this.$ = binding;
        }
    }
}
