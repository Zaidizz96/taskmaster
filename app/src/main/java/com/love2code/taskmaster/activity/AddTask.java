package com.love2code.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.love2code.taskmaster.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {

    public static final String TAG = "Add Task Activity";
    static Spinner teamSpinner = null;
    Spinner taskStateSpinner = null;

    static CompletableFuture<List<Team>> completableFuture = new CompletableFuture<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        completableFuture = new CompletableFuture<>();

        setUpTeamSpinner();
        setUpTaskStateSpinner();
        setUpSaveButtonAndSaveEntityToDB();


//        final TextView labelMessage = (TextView) findViewById(R.id.submittedLabel);

//        Toast toast = Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT);
//        submittedTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String titleText = taskTitle.getText().toString();
//                String bodyText = taskBody.getText().toString();
//
//                if (titleText.isEmpty() || bodyText.isEmpty()) {
//                    toast.show();
//                } else {
//                    labelMessage.setVisibility(View.VISIBLE);
//
//                    taskTitle.setText("");
//                    taskBody.setText("");
//                }
//            }
//        });
        ImageView goToHomePage = findViewById(R.id.backIcon);
        goToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddTask = new Intent(AddTask.this, MainActivity.class);
                AddTask.this.startActivity(goToAddTask);
            }
        });
    }

    public void setUpTeamSpinner() {
        teamSpinner = (Spinner) findViewById(R.id.teamSpinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                successResponse -> {
                    Log.i(TAG, "Reading Team Entity Successfully");
                    List<String> teamNames = new ArrayList<>();
                    List<Team> teams = new ArrayList<>();
                    for (Team team : successResponse.getData()) {
                        teams.add(team);
                        teamNames.add(team.getName());
                    }
                    completableFuture.complete(teams);
                    runOnUiThread(() -> {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                (android.R.layout.simple_spinner_item),
                                teamNames
                        ));
                    });
                },
                failureResponse -> {
                    completableFuture.complete(null);

                    Log.i(TAG, "Reading Team Entity failed");
                }
        );
    }

    private void setUpTaskStateSpinner() {

        taskStateSpinner = (Spinner) findViewById(R.id.taskState);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()
        ));
    }

    private void setUpSaveButtonAndSaveEntityToDB() {
        Button submittedTaskButton = (Button) findViewById(R.id.submittedTaskButton);

        submittedTaskButton.setOnClickListener(v -> {

            String title = ((EditText) findViewById(R.id.taskTitle)).getText().toString();
            String body = ((EditText) findViewById(R.id.taskBody)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
            String selectedTeamString= teamSpinner.getSelectedItem().toString();

            List<Team> teams = null;
            try {
                teams = completableFuture.get();
            }catch (InterruptedException interruptedException){
                Log.e(TAG, " InterruptedException while getting teams");
            }catch (ExecutionException e){
                Log.e(TAG," ExecutionException while getting teams");
            }

            assert teams != null;
            Team selectedTeam = teams.stream().filter(e -> e.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);


            Task newtask = Task.builder()
                    .title(title)
                    .body(body)
                    .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .state((TaskState) taskStateSpinner.getSelectedItem())
                    .team(selectedTeam)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newtask),
                    successResponse -> Log.i(TAG, "add task successfully"),
                    failureResponse -> Log.e(TAG, "add task failed" + failureResponse)

            );

            Snackbar.make(findViewById(R.id.addTaskActivity) , "Task Saved Successfully" , Snackbar.LENGTH_SHORT).show();
        });
    }

}