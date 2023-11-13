package com.love2code.taskmaster.activity;

import static com.love2code.taskmaster.activity.AddTask.completableFuture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.love2code.taskmaster.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsPage extends AppCompatActivity {

    public static final String USERNAME_TAG = "userName";
    public static final String TEAM_NAME_TAG = "teamName";
    public static final String TEAM_NAME_PREFERENCE_TAG = "teamName";

    SharedPreferences sharedPreferences;

    Spinner settingPageTeamSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        //==========Color===========
        TextView myTextView = findViewById(R.id.textViewUsername);
        myTextView.setBackgroundColor(getResources().getColor(R.color.your_color));
        //==========Color===========

        setUpTeamSpinner();

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        Button saveButton = findViewById(R.id.saveUserSettingButton);

        saveButton.setOnClickListener(view -> {

            String selectedTeamString= settingPageTeamSpinner.getSelectedItem().toString();

            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

            EditText usernameEditText = findViewById(R.id.usernameInputEditText);

            String usernameText = usernameEditText.getText().toString();

            preferenceEditor.putString(USERNAME_TAG , usernameText);
            preferenceEditor.putString(TEAM_NAME_PREFERENCE_TAG , selectedTeamString );
            preferenceEditor.apply();

            Snackbar.make(findViewById(R.id.userSettingPage) , "setting Saved" , Snackbar.LENGTH_SHORT).show();
        });
    }
    public void setUpTeamSpinner() {
        settingPageTeamSpinner = (Spinner) findViewById(R.id.teamSpinnerSettingsPage);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                successResponse -> {
                    Log.i(TEAM_NAME_TAG, "Reading Team Entity Successfully");
                    List<String> teamNames = new ArrayList<>();
                    List<Team> teams = new ArrayList<>();
                    for (Team team : successResponse.getData()) {
                        teams.add(team);
                        teamNames.add(team.getName());
                    }
                    completableFuture.complete(teams);
                    runOnUiThread(() -> {
                        settingPageTeamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                (android.R.layout.simple_spinner_item),
                                teamNames
                        ));
                    });
                },
                failureResponse -> {
                    completableFuture.complete(null);

                    Log.i(TEAM_NAME_TAG, "Reading Team Entity failed");
                }
        );
    }

}
