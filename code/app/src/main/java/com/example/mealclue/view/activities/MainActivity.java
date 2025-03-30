package com.example.mealclue.view.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mealclue.R;
import com.example.mealclue.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding $;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        $ = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView($.getRoot());

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.frgMain);
//        if (navHostFragment != null) {
//            NavController navController = navHostFragment.getNavController();
//            NavigationUI.setupWithNavController($.navMainMenu, navController);
//        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // get theme modes from prefs then set the mode accordingly
        prefs = getSharedPreferences(getString(R.string.k_meal_clue_prefs), MODE_PRIVATE);
        String[] themeModes = getResources().getStringArray(R.array.arr_theme_modes);
        int selectedThemeModeId = prefs.getInt(getString(R.string.k_selected_theme_mode), 0);
        if (selectedThemeModeId >= themeModes.length) {
            selectedThemeModeId = 0;
        }

        if (selectedThemeModeId == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (selectedThemeModeId == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.frgMain);
//        if (navHostFragment != null) {
//            NavController navController = navHostFragment.getNavController();
//            NavigationUI.setupWithNavController($.navMainMenu, navController);
//
//            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//                for (int i = 0; i < $.navMainMenu.getMenu().size(); i++) {
//                    if ($.navMainMenu.getMenu().getItem(i).getItemId() == destination.getId()) {
//                        $.navMainMenu.getMenu().getItem(i).setChecked(true);
//                    }
//                }
//            });
//        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        NavController navController = Navigation.findNavController(this, R.id.frgMain);
        NavigationUI.setupWithNavController($.navMainMenu, navController);
        syncWithNavigationActions();
    }

    /**
     * when navigate using actions from graph,
     * this bottom nav does not update when click back
     * on the previous jump, ex: from profile to plans manually by action
     * then we cannot click back on profile
     * so I use this to sync with the current real fragment
     */
    private void syncWithNavigationActions() {
        $.navMainMenu.setOnItemSelectedListener(item -> {
            NavController nv = Navigation.findNavController(this, R.id.frgMain);
            int itemId = item.getItemId();
            if (itemId == R.id.frgProfile) {
                nv.navigate(R.id.frgProfile);
            } else if (itemId == R.id.frgPlanList) {
                nv.navigate(R.id.frgPlanList);
            } else if (itemId == R.id.frgSocial) {
                nv.navigate(R.id.frgSocial);
            } else if (itemId == R.id.frgSettings) {
                nv.navigate(R.id.frgSettings);
            }
            return true;
        });
    }
}