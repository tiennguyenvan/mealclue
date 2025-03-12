package com.example.mealclue.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealclue.databinding.RecyclerItemRecipeOverlayBinding;
import com.example.mealclue.model.MealPlan;
import java.util.List;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {
    private List<MealPlan> plans;

    public PlanListAdapter(List<MealPlan> plans) {
        this.plans = plans;
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

        if (plan.isGoal()) {
            holder.$.txtGoalRibbon.setVisibility(View.VISIBLE);
            holder.$.txtName.setText(plan.getName() + " - 80%");
            holder.$.txtMeta.setText("30 recipes - 3 left");
        } else {
            holder.$.txtGoalRibbon.setVisibility(View.GONE);
            holder.$.txtMeta.setText("30 recipes");
        }
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
