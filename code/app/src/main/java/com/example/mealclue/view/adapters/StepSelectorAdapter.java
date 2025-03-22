package com.example.mealclue.view.adapters;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealclue.R;

import java.util.List;

import com.example.mealclue.databinding.RecyclerItemStepBinding;
public class StepSelectorAdapter extends RecyclerView.Adapter<StepSelectorAdapter.ViewHolder> {

    public interface OnStepClickListener {
        void onClick(int stepIndex);
    }

    private final List<Integer> steps;
    private final OnStepClickListener listener;
    private int selectedStep = 0;

    public StepSelectorAdapter(List<Integer> steps, OnStepClickListener listener) {
        this.steps = steps;
        this.listener = listener;
        this.selectedStep = steps.get(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemStepBinding binding = RecyclerItemStepBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.$.txtStepName.setText(String.valueOf(steps.get(position)));
        holder.$.itemStep.setOnClickListener(v -> {
            listener.onClick(steps.get(position));
            selectedStep = steps.get(position);
            notifyDataSetChanged();
        });

        int selectedColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary);
        int unselectedColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorContrast);

        holder.$.txtStepName.setTextColor(steps.get(position) == selectedStep ? selectedColor : unselectedColor);



    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerItemStepBinding $;

        public ViewHolder(@NonNull RecyclerItemStepBinding $) {
            super($.getRoot());
            this.$ = $;
        }
    }
}
