package com.love2code.taskmaster.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.love2code.taskmaster.R;
import com.love2code.taskmaster.activity.adapter.HomePageRecyclerViewAdapter;
import com.love2code.taskmaster.activity.database.TasksDatabase;
import com.love2code.taskmaster.activity.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TasksDatabase tasksDatabase;
    public static final String TASK_TITLE_EXTRA_TAG = "taskTitle";
    public static final String TASK_BODY_EXTRA_TAG = "taskBody";
    public static final String TASK_STATE_EXTRA_TAG = "taskState";
    public static final String TASK_DATE_EXTRA_TAG = "taskDate";
    public static final String DATABASE_NAME = "tasks";

    List<Task> tasks = null;
    HomePageRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        instantiateDatabase();

        Button settingButton = findViewById(R.id.settingsButton);
        Button addTaskButton = findViewById(R.id.addTask);
        Button allTaskButton = findViewById(R.id.allTask);

        settingButton.setOnClickListener(view -> {
            Intent goToSettingPage = new Intent(MainActivity.this, SettingsPage.class);
            MainActivity.this.startActivity(goToSettingPage);
        });

        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskPage = new Intent(MainActivity.this, AddTask.class);
            startActivity(goToAddTaskPage);
        });

        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasks = new Intent(MainActivity.this, AllTasks.class);
            startActivity(goToAllTasks);
        });
        setupHomePageRecyclerView();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();

        String username = sharedPreferences.getString(SettingsPage.USERNAME_TAG, "DefaultUsername");

        TextView usernameTextView = findViewById(R.id.userNameReplacedText);
        usernameTextView.setText(username + "'s tasks");

        tasks.clear();
        tasks.addAll(tasksDatabase.taskDao().findAll());
        adapter.notifyDataSetChanged();

    }

    public void setupHomePageRecyclerView() {
        RecyclerView homePageRecyclerView = (RecyclerView) findViewById(R.id.homeActivityRecylerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        homePageRecyclerView.setLayoutManager(layoutManager);

        adapter = new HomePageRecyclerViewAdapter(tasks, this);
        homePageRecyclerView.setAdapter(adapter);
    }

    void instantiateDatabase() {
        tasksDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TasksDatabase.class,
                        DATABASE_NAME).
                fallbackToDestructiveMigration().
                allowMainThreadQueries().build();
        tasks = tasksDatabase.taskDao().findAll();
    }
}