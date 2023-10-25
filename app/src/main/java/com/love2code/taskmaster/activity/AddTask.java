package com.love2code.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.love2code.taskmaster.R;
import com.love2code.taskmaster.activity.database.TasksDatabase;
import com.love2code.taskmaster.activity.model.Task;
import com.love2code.taskmaster.activity.model.TaskState;

import java.util.Date;

public class AddTask extends AppCompatActivity {

    TasksDatabase tasksDatabase;
    public static final String DATABASE_NAME="tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        instantiateDatabase();

        Spinner taskStateSpinner =(Spinner)findViewById(R.id.taskState);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()
        ));

        Button submittedTaskButton = (Button) findViewById(R.id.submittedTaskButton);
        ImageView goToHomePage = findViewById(R.id.backIcon);

        submittedTaskButton.setOnClickListener(v -> {
            Task task = new Task(
                    ((EditText)findViewById(R.id.taskTitle)).getText().toString(),
                    ((EditText)findViewById(R.id.taskBody)).getText().toString(),
                    new Date(),
                    TaskState.fromString(taskStateSpinner.getSelectedItem().toString())
            );
            tasksDatabase.taskDao().addTask(task);
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

    void instantiateDatabase(){
        tasksDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TasksDatabase.class,
                        DATABASE_NAME).
                fallbackToDestructiveMigration().
                allowMainThreadQueries().build();
        tasksDatabase.taskDao().findAll();
    }
}