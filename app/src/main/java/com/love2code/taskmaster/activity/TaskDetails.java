package com.love2code.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.love2code.taskmaster.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

public class TaskDetails extends AppCompatActivity {

    public static final String TASK_ID_TAG = "taskID";
    private static final String TAG = "Task details activity";
    private MediaPlayer mp = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        Intent callingIntent = getIntent();
        String taskId;
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;
        String dateCreatedString = null;
        String imageS3Key = null;
        String userLatitude = null;
        String userLongitude = null;


        if(callingIntent != null){
            taskId = callingIntent.getStringExtra(MainActivity.TASK_ID_EXTRA_TAG);
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_TITLE_EXTRA_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_EXTRA_TAG);
            taskState = callingIntent.getStringExtra(MainActivity.TASK_STATE_EXTRA_TAG);
            dateCreatedString = callingIntent.getStringExtra(MainActivity.TASK_DATE_EXTRA_TAG);
            imageS3Key = callingIntent.getStringExtra(MainActivity.TASK_S3_IMAGE_KEY);
            userLatitude = callingIntent.getStringExtra(MainActivity.TASK_USER_LATITUDE);
            userLongitude = callingIntent.getStringExtra(MainActivity.TASK_USER_LONGITUDE);

        } else {
            taskId = null;
        }

        TextView taskDetailTitle = findViewById(R.id.taskDetailTitle);
        TextView taskDetailBody = findViewById(R.id.taskDetailDescription);
        TextView taskDetailState = findViewById(R.id.task_state);
        TextView taskDetailsDate = findViewById(R.id.task_date);
        TextView taskUserLatitude = findViewById(R.id.taskLatitude);
        TextView taskUserLongitude = findViewById(R.id.taskLongitude);

        taskDetailTitle.setText(Objects.requireNonNullElse(taskTitle, "no task title specified!!!!"));
        taskDetailBody.setText(Objects.requireNonNullElse(taskBody , "no task description specified"));
        taskDetailState.setText(Objects.requireNonNullElse(taskState , "no task state specified"));
        taskDetailsDate.setText(Objects.requireNonNullElse(dateCreatedString , "no task createdAt date specified"));
        taskUserLatitude.setText(Objects.requireNonNullElse(userLatitude , "no user latitude specified"));
        taskUserLongitude.setText(Objects.requireNonNullElse(userLongitude , "no user longitude specified"));
        
        displayImage(imageS3Key);

        Button editTaskButton = (Button) findViewById(R.id.editTaskButton);
        editTaskButton.setOnClickListener(v -> {
            Intent gotToEditTaskActivity = new Intent(TaskDetails.this , EditTask.class);
            gotToEditTaskActivity.putExtra(TASK_ID_TAG , taskId);
            startActivity(gotToEditTaskActivity);
        });

        mp = new MediaPlayer();
        setupSpeakButton();
    }

    private void displayImage(String imageS3Key){

        if (imageS3Key != null && !imageS3Key.isEmpty())
        {
            Amplify.Storage.downloadFile(
                    imageS3Key,
                    new File(getApplication().getFilesDir(), imageS3Key),
                    success ->
                    {
                        ImageView productImageView = findViewById(R.id.imageTaskDetails);
                        productImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure ->
                    {
                        Log.e(TAG, "Unable to get image from S3 for the product for S3 key: " + imageS3Key + " for reason: " + failure.getMessage());
                    }
            );
        }
    }

    private void setupSpeakButton(){
        Button speachButton = (Button) findViewById(R.id.convertTextToSpeach);
        speachButton.setOnClickListener(b -> {
            String taskDescription = ((TextView)findViewById(R.id.taskDetailDescription)).getText().toString();

            Amplify.Predictions.convertTextToSpeech(
                taskDescription,
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e(TAG , "conversation failed")

            );
        });
    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
}