package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.love2code.taskmaster.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTaskButton = findViewById(R.id.addTask);
        Button allTaskButton = findViewById(R.id.allTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddTask = new Intent(MainActivity.this , AddTask.class);
                MainActivity.this.startActivity(goToAddTask);
            }
        });

        allTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllTask = new Intent(MainActivity.this , AllTasks.class);
                MainActivity.this.startActivity(goToAllTask);
            }
        });
    }
}