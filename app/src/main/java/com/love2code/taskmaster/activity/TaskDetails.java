package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.love2code.taskmaster.R;

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

        if(callingIntent != null){
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_TITLE_EXTRA_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_EXTRA_TAG);
        }

        TextView taskDetailTitle = findViewById(R.id.taskDetailTitle);
        TextView taskDetailBody = findViewById(R.id.taskDetailDescription);

        taskDetailTitle.setText(Objects.requireNonNullElse(taskTitle, "no task title specified!!!!"));
        taskDetailBody.setText(Objects.requireNonNullElse(taskBody , "no task description specified"));
    }




}