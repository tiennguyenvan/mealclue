package com.example.mealclue.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mealclue.R;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.Recipe;
import java.util.List;

import com.example.mealclue.databinding.RecyclerItemRecipeEditorBinding;
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private RecyclerItemRecipeEditorBinding $;
    Context context;
    MealPlan mealPlan;

    public RecipeListAdapter(List<Recipe> recipes, MealPlan mealPlan, Context context, OnClickItemBtnAddListener listener) {
        this.recipes = recipes;
        this.context = context;
        this.mealPlan = mealPlan;
        this.onClickItemBtnAddListener = listener; // Assign listener here
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_recipe_item_editor, parent, false);
//        return new ViewHolder(view);
        RecyclerItemRecipeEditorBinding binding = RecyclerItemRecipeEditorBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding.getRoot(), binding);
    }

    public interface OnClickItemBtnAddListener {
        void onClickItemBtnAdd(int position);
    }
    private OnClickItemBtnAddListener onClickItemBtnAddListener;


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.$.txtName.setText(recipe.getTitle());

        try {
            int imageResId = Integer.parseInt(recipe.getImage()); // Convert stored string back to int
            Glide.with(holder.itemView.getContext()).load(imageResId).into(holder.$.imgThumb);
        } catch (NumberFormatException e) {
            // Fallback for URL images
            Glide.with(holder.itemView.getContext()).load(recipe.getImage()).into(holder.$.imgThumb);
        }
        if (mealPlan != null && mealPlan.getRecipeIdsList().contains(recipe.getId())) {
            holder.$.btnAdd.setImageResource(R.drawable.ic_check);
            return;
        }

        holder.$.btnAdd.setOnClickListener(v -> {
            System.out.println("Add button clicked");
            if (mealPlan != null && mealPlan.getRecipeIdsList().contains(recipe.getId())) {
                holder.$.btnAdd.setImageResource(R.drawable.ic_check);
                return;
            }
            if (onClickItemBtnAddListener != null) {
                onClickItemBtnAddListener.onClickItemBtnAdd(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerItemRecipeEditorBinding $;

        public ViewHolder(@NonNull View itemView, RecyclerItemRecipeEditorBinding $) {
            super(itemView);
            this.$ = $;
        }
    }
}
