package com.love2code.taskmaster.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.love2code.taskmaster.R;
import com.love2code.taskmaster.activity.adapter.HomePageRecyclerViewAdapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String TASK_TITLE_EXTRA_TAG = "taskTitle";
    public static final String TASK_BODY_EXTRA_TAG = "taskBody";
    public static final String TASK_STATE_EXTRA_TAG = "taskState";
    public static final String TASK_DATE_EXTRA_TAG = "taskDate";
    public static final String DATABASE_NAME = "tasks";
    public static final String TAG = "MainActivity";

    List<Task> tasks = null;
    HomePageRecyclerViewAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        tasks = new ArrayList<>();



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

        Amplify.API.query(
                ModelQuery.list(Task.class),
                successResponse -> {
                    Log.i(TAG , "Reading tasks successfully");
                    tasks.clear();
                    for (Task task  : successResponse.getData()) {
                        tasks.add(task);
                    }
                    runOnUiThread(() ->{
                        adapter.notifyDataSetChanged();
                    });
                },
                failureResponse -> {
                    Log.i(TAG , "Fail to read a tasks");
                }
        );

    }

    public void setupHomePageRecyclerView() {
        RecyclerView homePageRecyclerView = (RecyclerView) findViewById(R.id.homeActivityRecylerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        homePageRecyclerView.setLayoutManager(layoutManager);
        adapter = new HomePageRecyclerViewAdapter(tasks, this);
        homePageRecyclerView.setAdapter(adapter);
    }

}