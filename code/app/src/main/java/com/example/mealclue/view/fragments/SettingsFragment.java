package com.example.mealclue.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealclue.R;
import com.example.mealclue.controller.UserDAO;
import com.example.mealclue.databinding.FragmentSettingsBinding;
import com.example.mealclue.model.User;
import com.example.mealclue.view.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding $;
    private Context context;
    private User user;
    private SharedPreferences prefs;
    private ActivityResultLauncher<String> imagePickerLauncher;
    String[] fonts;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = requireContext();
        $ = FragmentSettingsBinding.inflate(inflater, container, false);
        return $.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        prefs = context.getSharedPreferences(getString(R.string.k_meal_clue_prefs), MODE_PRIVATE);
        user = loadUser();
        if (user == null) {
            return;
        }
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String avatarPath = uri.toString();
                user.setAvatar(avatarPath);
                Glide.with(this).load(avatarPath).into($.incUser.imgAvatar);
                new UserDAO(context).update(user);
            }
        });
        fonts = getResources().getStringArray(R.array.arr_fonts);
        initFields();
        $.btnSaveSettings.setOnClickListener(this::saveSettings);
        $.btnLogout.setOnClickListener(this::logout);
        $.incUser.imgAvatar.setOnClickListener(this::onClickUserAvatar);
    }

    private void onClickUserAvatar(View v) {
        imagePickerLauncher.launch("image/*");
    }
    private void saveSettings(View v) {
        String fullName = $.incInpFullName.inpField.getText().toString();
        String postalCode = $.incInpPostalCode.inpField.getText().toString();

        if (fullName.length() < 3) {
            Toast.makeText(context, "Name must be at least 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        postalCode = postalCode.replaceAll("\\s+", ""); // remove spaces
        if (!postalCode.matches("^[A-Z]\\d[A-Z]\\d[A-Z]\\d$")) {
            Toast.makeText(context, "Invalid postal code", Toast.LENGTH_SHORT).show();
            return;
        }
        postalCode = postalCode.substring(0, 3) + " " + postalCode.substring(3);

        user.setFullName(fullName);
        user.setPostalCode(postalCode);

        UserDAO userDAO = new UserDAO(context);
        userDAO.update(user);

        SharedPreferences.Editor editor = context.getSharedPreferences(getString(R.string.k_meal_clue_prefs), MODE_PRIVATE).edit();
        int selectedThemeModeId = $.incInpThemeModes.inpSelect.getSelectedItemPosition();
        editor.putInt(getString(R.string.k_selected_theme_mode), selectedThemeModeId);
        editor.apply();

        if (selectedThemeModeId == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (selectedThemeModeId == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        requireActivity().recreate();

        Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(v).navigateUp();
    }

    private void logout(View v) {

        prefs.edit().remove(getString(R.string.k_logged_in_user_id)).apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        requireActivity().finish();
    }


    private void initFields() {
        System.out.println("initFields for " + user.getFullName());
        $.incInpFullName.txtLabel.setText(R.string.name);
        $.incInpFullName.inpField.setText(user.getFullName());

        $.incInpPostalCode.txtLabel.setText(R.string.postal);
        $.incInpPostalCode.inpField.setText(user.getPostalCode());
        $.incInpPostalCode.inpField.setHint("XXX XXX");

        $.incInpThemeModes.txtLabel.setText(R.string.color);
        $.incInpThemeModes.inpField.setVisibility(View.GONE);
        $.incInpThemeModes.inpSelect.setVisibility(View.VISIBLE);


//        $.incInpFonts.txtLabel.setText(R.string.fonts);
//        $.incInpFonts.inpField.setVisibility(View.GONE);
//        $.incInpFonts.inpSelect.setVisibility(View.VISIBLE);
//        $.incInpFonts.inpSelect.setAdapter(new ArrayAdapter<>(
//                context, android.R.layout.simple_spinner_item, fonts
//        ));

        // get theme modes from prefs then set the inputSelect value to the selected value
        String[] themeModes = getResources().getStringArray(R.array.arr_theme_modes);
        int selectedThemeModeId = prefs.getInt(getString(R.string.k_selected_theme_mode), 0);
        if (selectedThemeModeId >= themeModes.length) {
            selectedThemeModeId = 0;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, themeModes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        $.incInpThemeModes.inpSelect.setAdapter(adapter);
        $.incInpThemeModes.inpSelect.setSelection(selectedThemeModeId);
    }

    public User loadUser() {
        User user = User.getLoggedInUser(context);
        if (user == null) {
            return null;
        }

        $.incUser.txtFullName.setText(user.getFullName());
        $.incUser.txtHeartCount.setText(String.format("%s hearts", user.getHearts()));
        if (user.getAvatar() != null) {
            Glide.with(this).load(user.getAvatar()).into($.incUser.imgAvatar);
        }
        return user;
    }
}