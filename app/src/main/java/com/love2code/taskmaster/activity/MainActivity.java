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
import com.amplifyframework.datastore.generated.model.Team;
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

//        Team team1 = Team.builder()
//                .name("Development")
//                .build();
//        Team team2 = Team.builder()
//                .name("Engineering")
//                .build();
//        Team team3 = Team.builder()
//                .name("Marketing")
//                .build();
//        Team team4 = Team.builder()
//                .name("Financial")
//                .build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                successResponse -> {
//                    Log.i(TAG , "team added successfully");
//                } ,
//                failureResponse -> {
//                    Log.i(TAG , "failed to create team to database");
//                }
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                successResponse -> {
//                    Log.i(TAG , "team added successfully");
//                } ,
//                failureResponse -> {
//                    Log.i(TAG , "failed to create team to database");
//                }
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                successResponse -> {
//                    Log.i(TAG , "team added successfully");
//                } ,
//                failureResponse -> {
//                    Log.i(TAG , "failed to create team to database");
//                }
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(team4),
//                successResponse -> {
//                    Log.i(TAG , "team added successfully");
//                } ,
//                failureResponse -> {
//                    Log.i(TAG , "failed to create team to database");
//                }
//        );
        init();
        setupIntents();
        setupHomePageRecyclerView();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();

        String username = sharedPreferences.getString(SettingsPage.USERNAME_TAG, "DefaultUsername");
        String settingPageTeamSelected = sharedPreferences.getString(SettingsPage.TEAM_NAME_PREFERENCE_TAG , "DefaultTeamName" );

        TextView usernameTextView = findViewById(R.id.userNameReplacedText);
        TextView teamNameTextView = findViewById(R.id.teamNameReplacedText);
        usernameTextView.setText(username + "'s tasks");
        teamNameTextView.setText("Department: " +" " +settingPageTeamSelected);

        Amplify.API.query(
                ModelQuery.list(Task.class),
                successResponse -> {
                    Log.i(TAG , "Reading tasks successfully");
                    tasks.clear();
                    for (Task task  : successResponse.getData()) {
                        if (task.getTeam().getName().equals(settingPageTeamSelected)){
                            tasks.add(task);
                        }
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

    private void init(){
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        tasks = new ArrayList<>();
    }

    private void setupIntents(){
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
    }

}