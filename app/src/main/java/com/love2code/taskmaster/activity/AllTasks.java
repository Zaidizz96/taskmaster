package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.love2code.taskmaster.R;

public class AllTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        ImageView backIcon = findViewById(R.id.backIcon);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBackToAddTask = new Intent(AllTasks.this , AddTask.class);

                AllTasks.this.startActivity(goBackToAddTask);
            }
        });
    }
}