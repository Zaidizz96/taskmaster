package com.love2code.taskmaster.activity;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;

public class TaskMasterAmplifyApplication extends Application {

    public static final String TAG = "taskmasterApplication";
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
        }catch (AmplifyException amplifyException){
            Log.i(TAG , "Error initializing Amplify" + amplifyException.getMessage() , amplifyException);
        }
    }
}
