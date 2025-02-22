package com.example.mealclue.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealclue.databinding.ComponentSubCategoryItemBinding;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    private List<String> categories;
    private int selectedPosition = 0; // Default selected item

    public SubCategoryAdapter(List<String> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ComponentSubCategoryItemBinding binding = ComponentSubCategoryItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new SubCategoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.$.txtName.setText(category);

        // Change text appearance for selected item
        if (position == selectedPosition) {
            holder.$.txtName.setTextColor(holder.$.txtName.getContext().getResources().getColor(android.R.color.holo_green_light));
            holder.$.txtName.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            holder.$.txtName.setTextColor(holder.$.txtName.getContext().getResources().getColor(android.R.color.white));
            holder.$.txtName.setTypeface(null, android.graphics.Typeface.NORMAL);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ComponentSubCategoryItemBinding $;

        public ViewHolder(@NonNull ComponentSubCategoryItemBinding binding) {
            super(binding.getRoot());
            this.$ = binding;
        }
    }
}
