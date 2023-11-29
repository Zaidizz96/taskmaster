package com.love2code.taskmaster.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.snackbar.Snackbar;
import com.love2code.taskmaster.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {


    public static final String TAG = "Add Task Activity";
     static final int LOCATION_POLLING_INTERVAL = 5 * 1000;
    static Spinner teamSpinner = null;
    Spinner taskStateSpinner = null;

    static CompletableFuture<List<Team>> completableFuture = new CompletableFuture<>();

    ActivityResultLauncher<Intent> activityResultLauncher;

    FusedLocationProviderClient locationProviderClient = null;
    Geocoder geocoder = null;


    private String s3ImageKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        completableFuture = new CompletableFuture<>();

        activityResultLauncher = getImagePickingActivityResultLauncher();

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationProviderClient.flushLocations();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Log.e(TAG, "Location callback is null");
            }
            String currentLatitude = Double.toString(location.getLatitude());
            String currentLongitude = Double.toString(location.getLongitude());
            Log.i(TAG, "User latitude: " + location.getLatitude());
            Log.i(TAG, "User longitude: " + location.getLongitude());
        });
        locationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        });

        geocoder=new Geocoder(getApplicationContext(), new Locale("english" , "jordan"));
        LocationRequest locationRequest= LocationRequest.create();
        locationRequest.setInterval(LOCATION_POLLING_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                try {
                    String address = geocoder.getFromLocation(
                                    locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude(),
                                    1)
                            .get(0)
                            .getAddressLine(0);
                    Log.i(TAG,"Repeating current location is: "+address);
                }catch (IOException ioe){
                    Log.e(TAG, "Could not get subscribed location: "+ioe.getMessage(), ioe);
                }
            }
        };

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
        setUpTeamSpinner();
        setUpTaskStateSpinner();
        setupAddImageButton();
        setupSaveButton();
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent callingIntent = getIntent();
        if (callingIntent != null && callingIntent.getType() != null && callingIntent.getType().equals("text/plain")){
            String callingText = callingIntent.getStringExtra(Intent.EXTRA_TEXT);
            if (callingText != null){
                String cleanedText = cleanText(callingText);

                ((EditText)findViewById(R.id.addTaskTitle)).setText(cleanedText);
            }
        }
        if (callingIntent != null && callingIntent.getType() != null && callingIntent.getType().startsWith("image")){
            Uri incomingImageFileUri = callingIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (incomingImageFileUri != null){

                InputStream incomingImageFileInputStream = null;
                try{
                    incomingImageFileInputStream = getContentResolver().openInputStream(incomingImageFileUri);
                    ImageView addTaskImageView = findViewById(R.id.addTaskImageView);
                    if (addTaskImageView != null) {
                        addTaskImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));
                    }
                }catch (IOException notFoundException){
                    Log.e(TAG , "could not get file input stream from URI" + notFoundException.getMessage());
                }
            }
        }
    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher() {
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                Button addImageButton = findViewById(R.id.addTaskAddImageButton);
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    if (result.getData() != null) {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);
                                        } catch (FileNotFoundException fileNotFoundException) {
                                            Log.e(TAG, "Could not get file from file picker! " + fileNotFoundException.getMessage(), fileNotFoundException);
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );

        return imagePickingActivityResultLauncher;
    }


    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename, Uri pickedImageFileUri) {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success ->
                {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    // Part 4: Update/save our Product object to have an image key
//                    setUpSaveButtonAndSaveEntityToDB(success.getKey());


                    ImageView selectedImagePicker = findViewById(R.id.addTaskImageView);
                    InputStream pickedImageInputStreamCopy = null;
                    try {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    } catch (FileNotFoundException fileNotFoundException) {
                        Log.e(TAG, "Could not get file stream from URI! " + fileNotFoundException.getMessage(), fileNotFoundException);
                    }
                    selectedImagePicker.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));

                },
                failure ->
                {
                    Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFilename + " with error: " + failure.getMessage());
                }
        );
    }

    public void setUpTeamSpinner() {
        teamSpinner = (Spinner) findViewById(R.id.addTeamSpinner);

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
                    completableFuture.complete(teams);
                    runOnUiThread(() -> {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
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
    }

    private void setUpTaskStateSpinner() {

        taskStateSpinner = (Spinner) findViewById(R.id.addTaskState);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()
        ));
    }

    private void setupSaveButton() {
        Button submittedTaskButton = (Button) findViewById(R.id.saveTaskButton);
        submittedTaskButton.setOnClickListener(b -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            String title = ((EditText) findViewById(R.id.addTaskTitle)).getText().toString();
            String body = ((EditText) findViewById(R.id.addTaskDescription)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
            String selectedTeamString = teamSpinner.getSelectedItem().toString();


            List<Team> teams = null;
            try {
                teams = completableFuture.get();
            } catch (InterruptedException interruptedException) {
                Log.e(TAG, " InterruptedException while getting teams");
            } catch (ExecutionException e) {
                Log.e(TAG, " ExecutionException while getting teams");
            }

            assert teams != null;
            Team selectedTeam = teams.stream().filter(e -> e.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

            locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location == null) {
                    Log.e(TAG, "Location callback is null");
                }
                String currentLatitude = Double.toString(location.getLatitude());
                String currentLongitude = Double.toString(location.getLongitude());
                Log.i(TAG, "User latitude: " + location.getLatitude());
                Log.i(TAG, "User longitude: " + location.getLongitude());
                saveTask(title , body , selectedTeam , currentLatitude , currentLongitude);
            }
            ).addOnCanceledListener(() ->
            {
                Log.e(TAG, "Location request was Canceled");
            })
                    .addOnFailureListener(failure ->
                    {
                        Log.e(TAG, "Location request failed, Error was: " + failure.getMessage(), failure.getCause());
                    })
                    .addOnCompleteListener(complete ->
                    {
                        Log.e(TAG, "Location request Completed");
                    });
        });
    }

    private void saveTask(String title , String body , Team selectedTeam , String currentLatitude , String currentLongitude) {

        Task newtask = Task.builder()
                .title(title)
                .body(body)
                .dateCreated(new Temporal.DateTime(new Date(), 0))
                .state((TaskState) taskStateSpinner.getSelectedItem())
                .taskUserLatitude(currentLatitude)
                .taskUserLongitude(currentLongitude)
                .team(selectedTeam)
                .taskImageS3Key("")
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newtask),
                successResponse -> Log.i(TAG, "add task successfully"),
                failureResponse -> Log.e(TAG, "add task failed" + failureResponse)

        );

        Snackbar.make(findViewById(R.id.addTaskActivity), "Task Saved Successfully", Snackbar.LENGTH_SHORT).show();
    }

    private void setupDeleteImageButton() {
        Button deleteImage = (Button) findViewById(R.id.deleteImageAddTask);
        String s3ImageKey = this.s3ImageKey;
        deleteImage.setOnClickListener(b -> {
            Amplify.Storage.remove(
                    s3ImageKey,
                    success ->
                    {
                        Log.i(TAG, "Succeeded in deleting file on S3! Key is: " + success.getKey());

                    },
                    failure ->
                    {
                        Log.e(TAG, "Failure in deleting file on S3 with key: " + s3ImageKey + " with error: " + failure.getMessage());
                    }
            );
            ImageView selectedImagePicker = findViewById(R.id.addTaskImageView);
            selectedImagePicker.setImageResource(android.R.color.transparent);

//            setUpSaveButtonAndSaveEntityToDB("");
            switchFromDeleteButtonToAddButton(deleteImage);
        });
    }

    private void updateImageButtons() {
        Button addImageButton = findViewById(R.id.addTaskAddImageButton);
        Button deleteImageButton = findViewById(R.id.deleteImageAddTask);
        runOnUiThread(() -> {
            if (s3ImageKey.isEmpty()) {
                deleteImageButton.setVisibility(View.INVISIBLE);
                addImageButton.setVisibility(View.VISIBLE);
            } else {
                deleteImageButton.setVisibility(View.VISIBLE);
                addImageButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void switchFromDeleteButtonToAddButton(Button deleteImage) {
        Button addImageButton = (Button) findViewById(R.id.addTaskAddImageButton);
        deleteImage.setVisibility(View.INVISIBLE);
        addImageButton.setVisibility(View.VISIBLE);
    }

    private void switchFromAddButtonToDeleteButton(Button addImageButton) {
        Button deleteImageButton = findViewById(R.id.deleteImageAddTask);
        deleteImageButton.setVisibility(View.VISIBLE);
        addImageButton.setVisibility(View.INVISIBLE);
    }

    private void setupAddImageButton() {
        Button addImageButton = (Button) findViewById(R.id.addTaskAddImageButton);
        addImageButton.setOnClickListener(b -> {
            launchImageSelectionIntent();
        });
    }

    private void launchImageSelectionIntent() {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});

        activityResultLauncher.launch(imageFilePickingIntent);
    }

    private String cleanText(String text) {
        // Remove links
        text = text.replaceAll("\\b(?:https?|ftp):\\/\\/\\S+\\b", "");

        // Remove double quotes
        text = text.replaceAll("\"", "");

        return text;
    }

}