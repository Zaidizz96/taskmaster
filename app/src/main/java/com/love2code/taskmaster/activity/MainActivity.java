package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.love2code.taskmaster.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Button settingButton = findViewById(R.id.settingsButton);
        Button addTaskButton =  findViewById(R.id.addTask);
        Button allTaskButton = findViewById(R.id.allTask);
        Button task1Button = findViewById(R.id.task1Button);
        Button task2Button = findViewById(R.id.task2Button);
        Button task3Button = findViewById(R.id.task3Button);

        settingButton.setOnClickListener(view -> {
            Intent goToSettingPage = new Intent(MainActivity.this , SettingsPage.class);
            MainActivity.this.startActivity(goToSettingPage);
        });

        task1Button.setOnClickListener(view -> {
            Intent goToTaskDetails1 = new Intent(MainActivity.this , TaskDetails.class);
            goToTaskDetails1.putExtra("taskTitle", "Doing My Lab");
            startActivity(goToTaskDetails1);
        });
        task2Button.setOnClickListener(view -> {
            Intent goToTaskDetails2 = new Intent(MainActivity.this, TaskDetails.class);
            goToTaskDetails2.putExtra("taskTitle", "Going to gym");
            startActivity(goToTaskDetails2);
        });
        task3Button.setOnClickListener(view -> {
            Intent goToTaskDetails3 = new Intent(MainActivity.this, TaskDetails.class);
            goToTaskDetails3.putExtra("taskTitle", "Visiting my neighbor");
            startActivity(goToTaskDetails3);
        });

        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskPage = new Intent(MainActivity.this , AddTask.class);
            startActivity(goToAddTaskPage);
        });

        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasks = new Intent(MainActivity.this , AllTasks.class);
            startActivity(goToAllTasks);
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        String username = sharedPreferences.getString(SettingsPage.USERNAME_TAG, "DefaultUsername");

        TextView usernameTextView = findViewById(R.id.userNameReplacedText);
        usernameTextView.setText(username + "'s tasks");

    }
}