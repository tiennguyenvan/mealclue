package com.example.mealclue.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import com.example.mealclue.R;
import com.example.mealclue.controller.MealPlanDAO;
import com.example.mealclue.controller.UserDAO;
import com.example.mealclue.controller.Utils;
import com.example.mealclue.databinding.FragmentSocialBinding;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.User;
import com.example.mealclue.view.adapters.CategoryAdapter;
import com.example.mealclue.view.adapters.PlanListAdapter;
import com.example.mealclue.view.adapters.SocialPlanListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends Fragment {
    private FragmentSocialBinding $;
    private MealPlanDAO mealPlanDAO;
    private List<MealPlan> userMealPlans;
    private Context context;
    private UserDAO userDAO;
    private List<User> users;
    private User loggedInUser;
    private Utils.RegionType selectedRegion = Utils.RegionType.COUNTRY;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance(String param1, String param2) {
        SocialFragment fragment = new SocialFragment();
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
        context = requireContext();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        $.btnBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        loggedInUser = User.getLoggedInUser(context);
        if (loggedInUser == null) {
            return;
        }
        if (loggedInUser.getPostalCode().isEmpty()) {
            $.txtMessage.setText(R.string.user_should_set_postal_code);
            $.txtMessage.setVisibility(View.VISIBLE);
            $.linearMain.setVisibility(View.GONE);
            return;
        }

        userDAO = new UserDAO(context);
        users = userDAO.getAll();
        if (users.isEmpty()) {
            $.txtMessage.setText(R.string.empty_user_base);
            $.txtMessage.setVisibility(View.VISIBLE);
            return;
        }


        mealPlanDAO = new MealPlanDAO(requireContext());
        List<MealPlan> first10MealPlans = mealPlanDAO.getFirstMealPlans(10, loggedInUser.getId());

        if (first10MealPlans.isEmpty()) {
            $.txtMessage.setText(R.string.not_meal_plan_found);
            $.txtMessage.setVisibility(View.VISIBLE);
            return;
        }

        $.recyclerPlanList.setLayoutManager(new LinearLayoutManager(requireContext()));
        SocialPlanListAdapter planAdapter = new SocialPlanListAdapter(requireContext(), first10MealPlans);
        $.recyclerPlanList.setAdapter(planAdapter);



        $.recyclerSubCategories.setVisibility(View.GONE);

        List<String> regionNames = new ArrayList<>();
        for (Utils.RegionType type : Utils.RegionType.values()) {
            regionNames.add(type.name());
        }
        $.recyclerCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        CategoryAdapter regionNameAdapter = new CategoryAdapter(regionNames, position -> {
            selectedRegion = Utils.RegionType.valueOf(regionNames.get(position));
            performSearch();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        $.recyclerCategories.setLayoutManager(layoutManager);
        $.recyclerCategories.setAdapter(regionNameAdapter);

        $.incSearchBar.btnSearch.setOnClickListener(v -> {
            performSearch();
        });
    }

    // filter meal plans by keywords (search their title) and region selected
    private void performSearch() {
        String keyword = $.incSearchBar.inpKeywords.getText().toString().trim().toLowerCase();
        List<MealPlan> filtered = new ArrayList<>();

        for (User user : users) {
            if (user.getId() == loggedInUser.getId()) continue;
            if (user.getPostalCode() == null || user.getPostalCode().isEmpty()) continue;

            Utils.RegionType commonRegion = Utils.getCommonRegion(loggedInUser.getPostalCode(), user.getPostalCode());

            boolean matchedSelectedRegion = false;
            switch (selectedRegion) {
                case DISTRICT:
                    matchedSelectedRegion = (commonRegion == Utils.RegionType.DISTRICT);
                    break;
                case PROVINCE:
                    matchedSelectedRegion = (commonRegion == Utils.RegionType.DISTRICT || commonRegion == Utils.RegionType.PROVINCE);
                    break;
                case COUNTRY:
                    matchedSelectedRegion = true;
                    break;
            }

            if (!matchedSelectedRegion) continue;

            List<MealPlan> plans = mealPlanDAO.getByUser(user.getId());
            for (MealPlan plan : plans) {
                if (keyword.isEmpty() || plan.getName().toLowerCase().contains(keyword)) {
                    filtered.add(plan);
                }
            }
        }

        $.recyclerPlanList.setAdapter(new PlanListAdapter(requireContext(), filtered));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        $ = FragmentSocialBinding.inflate(inflater, container, false);
        return $.getRoot();
//        return inflater.inflate(R.layout.fragment_social, container, false);
    }


}