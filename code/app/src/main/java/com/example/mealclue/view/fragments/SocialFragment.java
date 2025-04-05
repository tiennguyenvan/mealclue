package com.example.mealclue.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import com.example.mealclue.R;
import com.example.mealclue.controller.HeartedMealPlanDAO;
import com.example.mealclue.controller.MealPlanDAO;
import com.example.mealclue.controller.UserDAO;
import com.example.mealclue.controller.Utils;
import com.example.mealclue.databinding.FragmentSocialBinding;
import com.example.mealclue.model.MealPlan;
import com.example.mealclue.model.User;
import com.example.mealclue.view.adapters.CategoryAdapter;
import com.example.mealclue.view.adapters.SocialPlanListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends Fragment {
    private FragmentSocialBinding $;
    private MealPlanDAO mealPlanDAO;
    private List<MealPlan> showingPlanList;
    private Context context;
    private UserDAO userDAO;
    private List<User> users;
    private User loggedInUser;
    private Utils.RegionType selectedCategory = Utils.RegionType.COUNTRY;
    SocialPlanListAdapter planAdapter;
    private HeartedMealPlanDAO heartedMealPlanDAO;


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
        showingPlanList = mealPlanDAO.getFirstPublicPlansFromOtherUsers(10, loggedInUser.getId());

        if (showingPlanList.isEmpty()) {
            $.txtMessage.setText(R.string.not_meal_plan_found);
            $.txtMessage.setVisibility(View.VISIBLE);
            return;
        }

        heartedMealPlanDAO = new HeartedMealPlanDAO(requireContext());

        fillPlanList();
        fillCategories();

        $.incSearchBar.inpKeywords.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                performSearch();
                return true;
            }
            return false;
        });

        $.incSearchBar.btnSearch.setOnClickListener(v -> {
            performSearch();
        });
    }

    private void fillCategories() {
        // at this time, I use one filter to serve all the categories including ❤
        $.recyclerSubCategories.setVisibility(View.GONE);

        List<String> categories = new ArrayList<>();
        for (Utils.RegionType type : Utils.RegionType.values()) {
            categories.add(type.name());
        }
        categories.add("♡");
        int heartPosition = categories.size() - 1;
        $.recyclerCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        CategoryAdapter regionNameAdapter = new CategoryAdapter(categories, position -> {
            if (position == heartPosition) {
                selectedCategory = null;
                categories.set(heartPosition, "♥");
            } else {
                selectedCategory = Utils.RegionType.valueOf(categories.get(position));
                categories.set(heartPosition, "♡");
            }
            performSearch();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        $.recyclerCategories.setLayoutManager(layoutManager);
        $.recyclerCategories.setAdapter(regionNameAdapter);

    }

    private void fillPlanList() {
        $.recyclerPlanList.setLayoutManager(new LinearLayoutManager(requireContext()));
        planAdapter = new SocialPlanListAdapter(requireContext(), showingPlanList);
        $.recyclerPlanList.setAdapter(planAdapter);
    }

    // filter meal plans by keywords (search their title) and region selected
    private void performSearch() {
        String keyword = $.incSearchBar.inpKeywords.getText().toString().trim().toLowerCase();
        showingPlanList.clear();

        // selected heart
        if (selectedCategory == null) {
            List<Integer> heartedPlanIds = heartedMealPlanDAO.getHeartedPlanIdsByUser(loggedInUser.getId());
            for (int planId : heartedPlanIds) {
                MealPlan plan = mealPlanDAO.getById(planId);
                if (plan != null && (keyword.isEmpty() || plan.getName().toLowerCase().contains(keyword))) {
                    showingPlanList.add(plan);
                }
            }
            planAdapter.notifyDataSetChanged();
            return;
        }

        for (User user : users) {
            if (user.getId() == loggedInUser.getId()) continue;
            if (user.getPostalCode() == null || user.getPostalCode().isEmpty()) continue;

            Utils.RegionType commonRegion = Utils.getCommonRegion(loggedInUser.getPostalCode(), user.getPostalCode());

            boolean matchedSelectedRegion = false;
            switch (selectedCategory) {
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

            List<MealPlan> plans = mealPlanDAO.getPublicByUser(user.getId());
            for (MealPlan plan : plans) {
                if (keyword.isEmpty() || plan.getName().toLowerCase().contains(keyword)) {
                    showingPlanList.add(plan);
                }
            }
        }

//        $.recyclerPlanList.setAdapter(new PlanListAdapter(requireContext(), filtered));
        planAdapter.notifyDataSetChanged();
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