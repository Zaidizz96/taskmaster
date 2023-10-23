package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.love2code.taskmaster.activity.adapter.HomePageRecyclerViewAdapter;
import com.love2code.taskmaster.activity.model.Task;
import com.love2code.taskmaster.activity.model.TaskState;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String TASK_TITLE_EXTRA_TAG="taskTitle";
    public static final String TASK_BODY_EXTRA_TAG="taskBody";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Button settingButton = findViewById(R.id.settingsButton);
        Button addTaskButton =  findViewById(R.id.addTask);
        Button allTaskButton = findViewById(R.id.allTask);
//        Button task1Button = findViewById(R.id.task1Button);
//        Button task2Button = findViewById(R.id.task2Button);
//        Button task3Button = findViewById(R.id.task3Button);

        settingButton.setOnClickListener(view -> {
            Intent goToSettingPage = new Intent(MainActivity.this , SettingsPage.class);
            MainActivity.this.startActivity(goToSettingPage);
        });

//        task1Button.setOnClickListener(view -> {
//            Intent goToTaskDetails1 = new Intent(MainActivity.this , TaskDetails.class);
//            goToTaskDetails1.putExtra("taskTitle", "Doing My Lab");
//            startActivity(goToTaskDetails1);
//        });
//        task2Button.setOnClickListener(view -> {
//            Intent goToTaskDetails2 = new Intent(MainActivity.this, TaskDetails.class);
//            goToTaskDetails2.putExtra("taskTitle", "Going to gym");
//            startActivity(goToTaskDetails2);
//        });
//        task3Button.setOnClickListener(view -> {
//            Intent goToTaskDetails3 = new Intent(MainActivity.this, TaskDetails.class);
//            goToTaskDetails3.putExtra("taskTitle", "Visiting my neighbor");
//            startActivity(goToTaskDetails3);
//        });

        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskPage = new Intent(MainActivity.this , AddTask.class);
            startActivity(goToAddTaskPage);
        });

        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasks = new Intent(MainActivity.this , AllTasks.class);
            startActivity(goToAllTasks);
        });
        setupHomePageRecyclerView();

    }
    @Override
    protected void onResume() {
        super.onResume();

        String username = sharedPreferences.getString(SettingsPage.USERNAME_TAG, "DefaultUsername");

        TextView usernameTextView = findViewById(R.id.userNameReplacedText);
        usernameTextView.setText(username + "'s tasks");
    }
    public void setupHomePageRecyclerView(){
        RecyclerView homePageRecyclerView =(RecyclerView) findViewById(R.id.homeActivityRecylerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        homePageRecyclerView.setLayoutManager(layoutManager);

        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Doing my lab" , "submit lab 28 and codeChallenge" , TaskState.NEW));
        taskList.add(new Task("Working on project" , "work on the final project " , TaskState.ASSIGNED));
        taskList.add(new Task("Visiting my neighbor" , "go to see your friend mohammad" , TaskState.COMPLETE));
        taskList.add(new Task("Grocery Shopping" , " Buy groceries for the week, including fruits, vegetables, and household supplies." , TaskState.NEW));
        taskList.add(new Task("Review Presentation" , "Go through the sales presentation slides and provide feedback." , TaskState.ASSIGNED));
        taskList.add(new Task("Write Documentation" , "Create a user manual for the new software release." , TaskState.COMPLETE));
        taskList.add(new Task("Birthday Party Planning" , "Organize a birthday party for a friend, including decorations, food, and entertainment" , TaskState.NEW));
        taskList.add(new Task("Exercise Routine" , "Stick to the daily exercise routine, including jogging and strength training" , TaskState.COMPLETE));
        taskList.add(new Task("Exercise Routine" , "Stick to the daily exercise routine, including jogging and strength training" , TaskState.COMPLETE));
        taskList.add(new Task("Exercise Routine" , "Stick to the daily exercise routine, including jogging and strength training" , TaskState.COMPLETE));
        taskList.add(new Task("Exercise Routine" , "Stick to the daily exercise routine, including jogging and strength training" , TaskState.COMPLETE));



        HomePageRecyclerViewAdapter adapter = new HomePageRecyclerViewAdapter(taskList , this);
        homePageRecyclerView.setAdapter(adapter);


    }
}