package com.example.mealclue.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealclue.databinding.RecyclerItemCategoryBinding;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<String> categories;
    private int selectedPosition = 0; // Default selected item
    private Listener listener;

    public CategoryAdapter(List<String> categories, Listener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public interface Listener {
        void onClickCategory(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemCategoryBinding binding = RecyclerItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        holder.$.item.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            if (listener != null) {
                listener.onClickCategory(position);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerItemCategoryBinding $;

        public ViewHolder(@NonNull RecyclerItemCategoryBinding binding) {
            super(binding.getRoot());
            this.$ = binding;
        }
    }
}
