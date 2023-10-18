package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.love2code.taskmaster.R;

public class TaskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        TextView taskDetailTitle = findViewById(R.id.taskDetailTitle);

        String taskTitle = getIntent().getStringExtra("taskTitle");

        taskDetailTitle.setText(taskTitle);
    }




}