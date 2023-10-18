package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.love2code.taskmaster.R;

public class SettingsPage extends AppCompatActivity {

    public static final String USERNAME_TAG = "userName";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        //==========Color===========
        TextView myTextView = findViewById(R.id.textViewUsername);
        myTextView.setBackgroundColor(getResources().getColor(R.color.your_color));
        //==========Color===========

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        Button saveButton = findViewById(R.id.saveUserSettingButton);

        saveButton.setOnClickListener(view -> {

            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

            EditText usernameEditText = findViewById(R.id.usernameInputEditText);
            String usernameText = usernameEditText.getText().toString();

            preferenceEditor.putString(USERNAME_TAG , usernameText);
            preferenceEditor.apply();

            Snackbar.make(findViewById(R.id.userSettingPage) , "setting Saved" , Snackbar.LENGTH_SHORT).show();
        });
    }
}
