package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.love2code.taskmaster.R;

import java.util.Date;
import java.util.Objects;

public class TaskDetails extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;
        String dateCreatedString = null;


        if(callingIntent != null){
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_TITLE_EXTRA_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_EXTRA_TAG);
            taskState = callingIntent.getStringExtra(MainActivity.TASK_STATE_EXTRA_TAG);
            dateCreatedString = callingIntent.getStringExtra(MainActivity.TASK_DATE_EXTRA_TAG);

        }

        TextView taskDetailTitle = findViewById(R.id.taskDetailTitle);
        TextView taskDetailBody = findViewById(R.id.taskDetailDescription);
        TextView taskDetailState = findViewById(R.id.task_state);
        TextView taskDetailsDate = findViewById(R.id.task_date);

        taskDetailTitle.setText(Objects.requireNonNullElse(taskTitle, "no task title specified!!!!"));
        taskDetailBody.setText(Objects.requireNonNullElse(taskBody , "no task description specified"));
        taskDetailState.setText(Objects.requireNonNullElse(taskState , "no task state specified"));
        taskDetailsDate.setText(Objects.requireNonNullElse(dateCreatedString , "no task createdAt date specified"));
    }
}