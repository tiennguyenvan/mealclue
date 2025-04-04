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
    int expandedPosition = -1;
    private MainActionListener mainActionListener;

    /**
     * @param recipes
     * @param mealPlan if mealPlan is null, means we are using this for the plan detail, not search
     * @param context
     * @param listener
     */
    public RecipeListAdapter(List<Recipe> recipes, MealPlan mealPlan, Context context, MainActionListener listener) {
        this.recipes = recipes;
        this.context = context;
        this.mealPlan = mealPlan;
        this.mainActionListener = listener; // Assign listener here
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

    public interface MainActionListener {
        void onRecyclerMainAction(int position);

        void onRecyclerRecipeListChange();
    }


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

        if (mainActionListener == null) {
            holder.$.btnMainAction.setVisibility(View.GONE);
            return;
        }

        if (mealPlan == null) {
            holder.$.btnMainAction.setImageResource(R.drawable.ic_pen);
            if (expandedPosition == position) {
                holder.$.linearEditingActions.setVisibility(View.VISIBLE);
                holder.$.btnMainAction.setAlpha(0.5f);
            } else {
                holder.$.linearEditingActions.setVisibility(View.GONE);
                holder.$.btnMainAction.setAlpha(1f);
            }
            holder.$.btnMainAction.setOnClickListener(v -> {
                int curPos = holder.getAdapterPosition();
                if (expandedPosition == curPos) {
                    expandedPosition = -1;
                } else {
                    expandedPosition = curPos;
                }
                notifyDataSetChanged();
//                mainActionListener.onRecyclerRecipeListChange();
            });

            holder.$.btnDelete.setOnClickListener(v -> {
                int curPos = holder.getAdapterPosition();
                recipes.remove(curPos);
                this.mealPlan.setCookedRecipes(null);
                notifyItemRemoved(curPos);
                mainActionListener.onRecyclerRecipeListChange();
                expandedPosition = -1;
            });
            holder.$.btnDown.setOnClickListener(v -> {
                int curPos = holder.getAdapterPosition();

                if (curPos < recipes.size() - 1) {
                    Recipe temp = recipes.get(curPos);
                    recipes.set(curPos, recipes.get(curPos + 1));
                    recipes.set(curPos + 1, temp);
                    this.mealPlan.setCookedRecipes(null);
                    notifyItemMoved(curPos, curPos + 1);
                    mainActionListener.onRecyclerRecipeListChange();
                }
            });
            holder.$.btnUp.setOnClickListener(v -> {
                int curPos = holder.getAdapterPosition();
                if (curPos > 0) {
                    Recipe temp = recipes.get(curPos);
                    recipes.set(curPos, recipes.get(curPos - 1));
                    recipes.set(curPos - 1, temp);
                    this.mealPlan.setCookedRecipes(null);
                    notifyItemMoved(curPos, curPos - 1);
                    mainActionListener.onRecyclerRecipeListChange();
                }

            });
            holder.$.btnDuplicate.setOnClickListener(v -> {
                int curPos = holder.getAdapterPosition();
                Recipe duplicateRecipe = new Recipe(
                        recipes.get(curPos).getId(),
                        recipes.get(curPos).getTitle(),
                        recipes.get(curPos).getImage(),
                        recipes.get(curPos).getIngredients(),
                        recipes.get(curPos).getInstructions(),
                        recipes.get(curPos).getKeywords()
                );
                recipes.add(curPos + 1, duplicateRecipe);
                this.mealPlan.setCookedRecipes(null);
                notifyItemInserted(curPos + 1);
                mainActionListener.onRecyclerRecipeListChange();
            });
            return;
        }

        holder.$.btnMainAction.setOnClickListener(v -> {
            System.out.println("Add button clicked");
            if (mealPlan != null && mealPlan.getRecipeIdsList().contains(recipe.getId())) {
                holder.$.btnMainAction.setImageResource(R.drawable.ic_check);
                return;
            }
            if (mainActionListener != null) {
                mainActionListener.onRecyclerMainAction(position);
            }
        });

        if (mealPlan.getRecipeIdsList().contains(recipe.getId())) {
            holder.$.btnMainAction.setImageResource(R.drawable.ic_check);
        }
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
