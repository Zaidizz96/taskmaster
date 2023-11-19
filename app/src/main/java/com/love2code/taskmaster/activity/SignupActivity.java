package com.love2code.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.love2code.taskmaster.R;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "signup";
    public static final String SIGNUP_EMAIL_TAG = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signupSubmitButton = findViewById(R.id.signupSubmitButton);
        signupSubmitButton.setOnClickListener(v -> {
            String username = ((EditText) findViewById(R.id.signupUsernameEditText)).getText().toString();
            String password = ((EditText) findViewById(R.id.signupPasswordEditText)).getText().toString();

            Amplify.Auth.signUp(username ,
                    password,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), username)
                            .userAttribute(AuthUserAttributeKey.nickname(), "Zi96")
                            .build(),
                    goodResponse -> {
                        Log.i(TAG, "Signup success " + goodResponse.toString());
                        Intent goToVerificationPage = new Intent(SignupActivity.this , VerifyAccountActivity.class);
                        goToVerificationPage.putExtra(SIGNUP_EMAIL_TAG , username);
                        startActivity(goToVerificationPage);

                    },
                    badResponse -> {
                        Log.e(TAG, "Signup failed");
                        runOnUiThread(() -> {
                            Toast.makeText(SignupActivity.this , "Signup failed" , Toast.LENGTH_SHORT).show();
                        });
                    }

            );
        });


    }
}