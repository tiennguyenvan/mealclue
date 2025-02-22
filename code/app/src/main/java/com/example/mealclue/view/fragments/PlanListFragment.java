package com.example.mealclue.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealclue.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import com.example.mealclue.databinding.FragmentPlanListBinding;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.view.adapters.PlanListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlanListFragment extends Fragment {
    private FragmentPlanListBinding $;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlanListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanListFragment newInstance(String param1, String param2) {
        PlanListFragment fragment = new PlanListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        $.btnBack.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
        // mock plan data
        List<MealPlan> plans = new ArrayList<>();
        plans.add(new MealPlan("Plan Name", 0, "", true));
        plans.add(new MealPlan("Plan Name", 0, "", true));
        plans.add(new MealPlan("Plan Name", 0, "", true));
        plans.add(new MealPlan("Plan Name", 0, "", true));
        $.recyclerPlanList.setLayoutManager(new LinearLayoutManager(requireContext()));
        PlanListAdapter planAdapter = new PlanListAdapter(plans);
        $.recyclerPlanList.setAdapter(planAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        $ = FragmentPlanListBinding.inflate(inflater, container, false);
//        return inflater.inflate(R.layout.fragment_plan_list, container, false);
        return $.getRoot();
    }
}