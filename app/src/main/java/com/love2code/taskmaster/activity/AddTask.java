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
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.love2code.taskmaster.R;


import java.util.Date;

public class AddTask extends AppCompatActivity {

    public static final String TAG = "Add Task Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        Spinner taskStateSpinner =(Spinner)findViewById(R.id.taskState);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()
        ));

        Button submittedTaskButton = (Button) findViewById(R.id.submittedTaskButton);
        ImageView goToHomePage = findViewById(R.id.backIcon);

        submittedTaskButton.setOnClickListener(v -> {

            String title = ((EditText)findViewById(R.id.taskTitle)).getText().toString();
            String body = ((EditText)findViewById(R.id.taskBody)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());

            Task newtask = Task.builder()
                    .title(title)
                    .body(body)
                    .dateCreated(new Temporal.DateTime(new Date() , 0))
                    .state((TaskState) taskStateSpinner.getSelectedItem())
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newtask),
                    successResponse -> Log.i(TAG , "add task successfully"),
                    failureResponse -> Log.e(TAG , "add task failed" + failureResponse)

            );

        });

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

        goToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddTask = new Intent(AddTask.this, MainActivity.class);
                AddTask.this.startActivity(goToAddTask);
            }
        });
    }

//    Spinner setupSpinner() {
//
//        return taskStateSpinner;
//    }

}