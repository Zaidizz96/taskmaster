package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.love2code.taskmaster.R;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button submittedTaskButton =(Button) findViewById(R.id.submittedTaskButton);
        ImageView goToHomePage = findViewById(R.id.backIcon);

        final EditText taskTitle = (EditText) findViewById(R.id.taskTitle);
        final EditText taskBody = (EditText) findViewById(R.id.taskBody);
        final TextView labelMessage = (TextView) findViewById(R.id.submittedLabel);

        Toast toast = Toast.makeText(this , "Both fields are required" , Toast.LENGTH_SHORT);
        submittedTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titleText = taskTitle.getText().toString();
                String bodyText = taskBody.getText().toString();

                if (titleText.isEmpty() || bodyText.isEmpty()){
                    toast.show();
                }else {
                    labelMessage.setVisibility(View.VISIBLE);

                    taskTitle.setText("");
                    taskBody.setText("");
                }
            }
        });

        goToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddTask = new Intent(AddTask.this , MainActivity.class);
                AddTask.this.startActivity(goToAddTask);
            }
        });

    }
}