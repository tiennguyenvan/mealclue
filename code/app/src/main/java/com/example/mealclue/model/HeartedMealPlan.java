package com.example.mealclue.model;

public class HeartedMealPlan {
    private int userId;
    private int mealPlanId;

    public HeartedMealPlan(int userId, int mealPlanId) {
        this.userId = userId;
        this.mealPlanId = mealPlanId;
    }

    public int getUserId() {
        return userId;
    }

    public int getMealPlanId() {
        return mealPlanId;
    }
}
