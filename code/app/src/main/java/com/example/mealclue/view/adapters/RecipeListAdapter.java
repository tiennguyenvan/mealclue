package com.example.mealclue.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mealclue.model.Recipe;
import java.util.List;

import com.example.mealclue.databinding.ComponentRecipeItemEditorBinding;
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private ComponentRecipeItemEditorBinding $;
    Context context;

    public RecipeListAdapter(List<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_recipe_item_editor, parent, false);
//        return new ViewHolder(view);
        ComponentRecipeItemEditorBinding binding = ComponentRecipeItemEditorBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.$.txtName.setText(recipe.getName());

        try {
            int imageResId = Integer.parseInt(recipe.getImage()); // Convert stored string back to int
            Glide.with(holder.itemView.getContext()).load(imageResId).into(holder.$.imgThumb);
        } catch (NumberFormatException e) {
            // Fallback for URL images
            Glide.with(holder.itemView.getContext()).load(recipe.getImage()).into(holder.$.imgThumb);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ComponentRecipeItemEditorBinding $;

        public ViewHolder(@NonNull View itemView, ComponentRecipeItemEditorBinding $) {
            super(itemView);
            this.$ = $;
        }
    }
}
