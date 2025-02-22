package com.example.mealclue.view.activities;

import android.os.Bundle;

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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

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