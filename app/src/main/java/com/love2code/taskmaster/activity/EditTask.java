package com.love2code.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class EditTask extends AppCompatActivity {

    public static final String TAG = "editTaskActivity";
    private CompletableFuture<Task> completableFuture;
    private CompletableFuture<List<Team>> teamCompletableFuture;
    private Task taskToEdit = null;
    private EditText editTextTitle = null;
    private EditText editTextDescription = null;
    private Spinner editTaskStateSpinner = null;
    private Spinner editTaskTeamSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        completableFuture = new CompletableFuture<>();
        teamCompletableFuture = new CompletableFuture<>();

        setupEditableUIElements();
        setupSaveButton();
        setupDeleteButton();

    }

    private void setupEditableUIElements() {
        Intent callingIntent = getIntent();
        String taskId = null;

        if (callingIntent != null) {
            taskId = callingIntent.getStringExtra(TaskDetails.TASK_ID_TAG);
        }
        String reicevedTaskID = taskId;

        Amplify.API.query(
                ModelQuery.list(Task.class),
                successResponse -> {
                    Log.i(TAG, "Reading task successfully");
                    for (Task task : successResponse.getData()) {
                        if (task.getId().equals(reicevedTaskID)) {
                            completableFuture.complete(task);
                        }
                    }
                    runOnUiThread(() -> {
                        //update UI element
                    });
                },
                failureResponse -> {
                    Log.e(TAG, "could not reading task from DB");
                }
        );

        try {
            taskToEdit = completableFuture.get();
        } catch (InterruptedException interruptedException) {
            Log.e(TAG, "interruptedException while getting task");
        } catch (ExecutionException executionException) {
            Log.e(TAG, "executionException while getting task");
        }
        setupEditTexts();
        setupSpinners();
    }

    private void setupEditTexts() {
        editTextTitle = findViewById(R.id.editTaskTitle);
        editTextTitle.setText(taskToEdit.getTitle());

        editTextDescription = findViewById(R.id.editTaskDescription);
        editTextDescription.setText(taskToEdit.getBody());
    }

    private void setupSpinners() {
        editTaskTeamSpinner = (Spinner) findViewById(R.id.editTeamSpinner);
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
                    teamCompletableFuture.complete(teams);
                    runOnUiThread(() -> {
                        editTaskTeamSpinner.setAdapter(new ArrayAdapter<>(
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

        editTaskStateSpinner = (Spinner) findViewById(R.id.editTaskState);
        editTaskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()
        ));
        editTaskStateSpinner.setSelection(getSpinnerIndex(editTaskStateSpinner, taskToEdit.getState().toString()));

    }

    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)) {
                return i;
            }
        }
        return 0;
    }

    private void setupSaveButton() {
        Button saveChangesButton = (Button) findViewById(R.id.editSaveTaskButton);

        saveChangesButton.setOnClickListener(v -> {

            String title = ((EditText) findViewById(R.id.editTaskTitle)).getText().toString();
            String body = ((EditText) findViewById(R.id.editTaskDescription)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
            String selectedTeamString = editTaskTeamSpinner.getSelectedItem().toString();

            List<Team> teams = null;
            try {
                teams = teamCompletableFuture.get();
            } catch (InterruptedException interruptedException) {
                Log.e(TAG, " InterruptedException while getting teams");
            } catch (ExecutionException e) {
                Log.e(TAG, " ExecutionException while getting teams");
            }

            assert teams != null;
            Team selectedTeam = teams.stream().filter(e -> e.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);


            Task newtask = Task.builder()
                    .title(editTextTitle.getText().toString())
                    .id(taskToEdit.getId())
                    .body(editTextDescription.getText().toString())
                    .dateCreated(taskToEdit.getDateCreated())
                    .team(selectedTeam)
                    .state((taskStateFromString(editTaskStateSpinner.getSelectedItem().toString())))
                    .build();

            Amplify.API.mutate(
                    ModelMutation.update(newtask),
                    successResponse -> Log.i(TAG, "save edited task successfully"),
                    failureResponse -> Log.e(TAG, "add task failed" + failureResponse)

            );

            Snackbar.make(findViewById(R.id.editTaskActivity), "Task Saved Successfully", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setupDeleteButton() {
        Button deleteButton = (Button) findViewById(R.id.deleteTask);
        deleteButton.setOnClickListener(v -> {
            Amplify.API.mutate(
                    ModelMutation.delete(taskToEdit),
                    successResponse -> {
                        Log.i(TAG, "Deleting task successfully");
                        Intent goToHomePage = new Intent(EditTask.this, MainActivity.class);
                        startActivity(goToHomePage);
                    },
                    failure -> {
                        Log.e(TAG, "could not delete the task");
                    }
            );

        });
    }

    public static TaskState taskStateFromString(String inputTaskStateText) {
        for (TaskState taskState : TaskState.values()) {
            if (taskState.toString().equals(inputTaskStateText)) {
                return taskState;
            }
        }
        return null;
    }


}