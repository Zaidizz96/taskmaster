package com.love2code.taskmaster.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.love2code.taskmaster.R;
import com.love2code.taskmaster.activity.adapter.HomePageRecyclerViewAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String TASK_ID_EXTRA_TAG = "taskID";
    public static final String TASK_TITLE_EXTRA_TAG = "taskTitle";
    public static final String TASK_BODY_EXTRA_TAG = "taskBody";
    public static final String TASK_STATE_EXTRA_TAG = "taskState";
    public static final String TASK_DATE_EXTRA_TAG = "taskDate";
    public static final String TASK_S3_IMAGE_KEY = "taskImage";
    public static final String TASK_USER_LATITUDE = "userLatitude";
    public static final String TASK_USER_LONGITUDE = "userLongitude";
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

//        Amplify.Auth.signUp("zaid.ali.izziddine@gmail.com" ,
//                "Zaidizz1996" ,
//                AuthSignUpOptions.builder()
//                        .userAttribute(AuthUserAttributeKey.email() , "zaid.ali.izziddine@gmail.com")
//                        .userAttribute(AuthUserAttributeKey.nickname() , "Zi96")
//                        .build(),
//                goodResponse -> {
//                    Log.i(TAG , "Signup success " + goodResponse.toString());
//                },
//                badResponse -> {
//                    Log.e(TAG , "Signup failed");
//                }
//
//                );

//        Amplify.Auth.confirmSignUp("zaid.ali.izziddine@gmail.com",
//                "807220",
//                successResponse -> {
//                    Log.i(TAG, "Verification success" + successResponse.toString());
//                },
//                failureResponse -> {
//                    Log.e(TAG, "Verification failed" + failureResponse.toString());
//                }
//        );

//        Amplify.Auth.signIn("zaid.ali.izziddine@gmail.com" ,
//                "Zaidizz1996",
//                successResponse -> {
//            Log.i(TAG , "Sign in success" + successResponse.toString());
//                },
//                failureResponse -> {
//            Log.e(TAG , "Sign in failed" + failureResponse.toString());
//                }
//                );

//        Amplify.Auth.signOut( () -> {
//            Log.i(TAG, "Sign out success");
//        },
//                failerResponse -> {
//            Log.e(TAG , "Sign out failed" + failerResponse.toString());
//                }
//        );
        String emptyFileName = "emptyTestFileName";
        File emptyFile = new File(getApplicationContext().getFilesDir(), emptyFileName);

        try {
            BufferedWriter emptyFileBufferWriter = new BufferedWriter(new FileWriter(emptyFile));
            emptyFileBufferWriter.append("some text from zaid \n another text from zaid");
            emptyFileBufferWriter.close();
        } catch (IOException ioException) {
            Log.e(TAG, "could not write locally with" + emptyFileName);
        }

        String emptyFileS3Key = "file in S3.txt";
        Amplify.Storage.uploadFile(
                emptyFileS3Key,
                emptyFile,
                success -> {
                    Log.i(TAG, " S3 Upload the file succeeded and the key:" + success.getKey());
                },
                failure -> {
                    Log.e(TAG, "s3 Failed to upload the file " + failure.getMessage());
                }
        );


        setupIntents();
        setupLogoutButton();
        setupHomePageRecyclerView();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();


        String username = sharedPreferences.getString(SettingsPage.USERNAME_TAG, "DefaultUsername");
        String settingPageTeamSelected = sharedPreferences.getString(SettingsPage.TEAM_NAME_PREFERENCE_TAG, "DefaultTeamName");

        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser == null) {
            Button loginButton = findViewById(R.id.homeLoginInterntButton);
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton = (Button) findViewById(R.id.logoutButton);
            logoutButton.setVisibility(View.INVISIBLE);
        } else {
            String email = authUser.getUsername();
            Button loginButton = findViewById(R.id.homeLoginInterntButton);
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton = (Button) findViewById(R.id.logoutButton);
            logoutButton.setVisibility(View.VISIBLE);

            Amplify.Auth.fetchUserAttributes(
                    success ->
                    {
                        Log.i(TAG, "successfully fetching user session: ");
                        for (AuthUserAttribute userAttribute : success) {
                            if (userAttribute.getKey().getKeyString().equals("email")) {
                                String userEmail = userAttribute.getValue();
                                runOnUiThread(() ->
                                {
                                    ((TextView) findViewById(R.id.userEmailReplacedText)).setText(userEmail);
                                });
                            }
                        }
                    },
                    failure ->
                    {
                        Log.i(TAG, "Fetch user attributes failed: " + failure.toString());
                    }
            );


        }

        TextView usernameTextView = findViewById(R.id.userNameReplacedText);
        TextView teamNameTextView = findViewById(R.id.teamNameReplacedText);
        usernameTextView.setText(username + "'s tasks");
        teamNameTextView.setText("Department: " + " " + settingPageTeamSelected);

        Amplify.API.query(
                ModelQuery.list(Task.class),
                successResponse -> {
                    Log.i(TAG, "Reading tasks successfully");
                    tasks.clear();
                    for (Task task : successResponse.getData()) {
                        if (task.getTeam().getName().equals(settingPageTeamSelected)) {
                            tasks.add(task);
                        }
                    }
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                },
                failureResponse -> {
                    Log.i(TAG, "Fail to read a tasks");
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

    private void init() {
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        tasks = new ArrayList<>();
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("openedApp")
                .addProperty("time" , Long.toString(new Date().getTime()))
                .addProperty("tracking event" , "main activity")
                .build();

        Amplify.Analytics.recordEvent(event);
    }

    private void setupIntents() {
        Button settingButton = findViewById(R.id.settingsButton);
        Button addTaskButton = findViewById(R.id.addTask);
        Button allTaskButton = findViewById(R.id.allTask);
        Button loginButton = findViewById(R.id.homeLoginInterntButton);

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

        loginButton.setOnClickListener(v -> {
            Intent gotoLoginPage = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(gotoLoginPage);

        });
    }

    private void setupLogoutButton() {
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            Amplify.Auth.signOut(() -> {
                        Log.i(TAG, "Sign out success");
                        runOnUiThread(() -> {
                            ((TextView) findViewById(R.id.userEmailReplacedText)).setText("");
                        });
                    },
                    failerResponse -> {
                        Log.e(TAG, "Sign out failed" + failerResponse.toString());
                        Toast.makeText(MainActivity.this, "logout failed", Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }

}