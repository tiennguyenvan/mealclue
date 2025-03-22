package com.example.mealclue.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealclue.model.Ingredient;

import java.util.List;

import com.example.mealclue.databinding.RecyclerItemIngredientBinding;
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemIngredientBinding $ = RecyclerItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder($);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient i = ingredients.get(position);
        holder.$.txtName.setText(i.getName());
        holder.$.txtAmountUnit.setText(i.getAmount() + " " + i.getUnit());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerItemIngredientBinding $;

        public ViewHolder(@NonNull RecyclerItemIngredientBinding $) {
            super($.getRoot());
            this.$ = $;
        }
    }
}

