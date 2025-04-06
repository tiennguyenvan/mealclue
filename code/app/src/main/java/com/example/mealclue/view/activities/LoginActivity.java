package com.example.mealclue.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mealclue.R;

import com.example.mealclue.controller.UserDAO;
import com.example.mealclue.databinding.ActivityLoginBinding;
import com.example.mealclue.model.User;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding $;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        $ = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView($.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        SharedPreferences prefs = getSharedPreferences(getString(R.string.k_meal_clue_prefs), MODE_PRIVATE);
//        int savedUserId = prefs.getInt(getString(R.string.k_logged_in_user_id), -1);
//        if (savedUserId != -1) {
//            goToMain(savedUserId);
//            return;
//        }
        User loggedInUser = User.getLoggedInUser(this);
        if (loggedInUser != null) {
            goToMain(loggedInUser.getId());
            return;
        }

//        UserDAO userDAO = new UserDAO(this);
//        if (userDAO.count() == 0) {
//            // create demo users and database
//        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        $.incInpUser.txtLabel.setText(R.string.username);
        $.incInpUser.inpField.setHint(R.string.enter_username);
        $.incInpPassword.txtLabel.setText(R.string.password);
        $.incInpPassword.inpField.setHint(R.string.enter_password);
        $.incInpPassword.inpField.setInputType(
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        $.btnLoginRegister.setOnClickListener(v -> {
            String username = $.incInpUser.inpField.getText().toString().trim();
            String password = $.incInpPassword.inpField.getText().toString();
            username = username.toLowerCase().replaceAll("[^a-z0-9_]", "");
            $.incInpUser.inpField.setText(username);

            if (username.length() < 3) {
                Toast.makeText(this, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 4) {
                Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            UserDAO userDAO = new UserDAO(this);
            User user = userDAO.getUserByUsername(username);

            if (user != null) {
                if (user.getPasswordHash().equals(String.valueOf(password.hashCode()))) {
                    saveLoginAndRedirect(user.getId());
                    return;
                }

                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                return;
            }


            // New user registration
            User newUser = new User(null, username, 0, "", username, String.valueOf(password.hashCode()));
            long userId = userDAO.insert(newUser);
            System.out.println("New user ID: " + userId);
            saveLoginAndRedirect((int) userId);
        });

    }

    private void saveLoginAndRedirect(int userId) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.k_meal_clue_prefs), MODE_PRIVATE).edit();
        editor.putInt(getString(R.string.k_logged_in_user_id), userId);
        editor.apply();
        goToMain(userId);
    }

    private void goToMain(int userId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.k_logged_in_user_id), userId);
        startActivity(intent);
        finish();
    }
}