package com.love2code.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.love2code.taskmaster.R;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "loginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent callingIntent = getIntent();
        String email = callingIntent.getStringExtra(VerifyAccountActivity.VERIFY_ACCOUNT_EMAIL_TAG);
        EditText usernameEditText = ((EditText) findViewById(R.id.loginUsernameEditText));
        usernameEditText.setText(email);

        Button loginSubmitButton = (Button) findViewById(R.id.loginSubmitButton);
        loginSubmitButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = ((EditText) findViewById(R.id.loginPasswordEditText)).getText().toString();

            Amplify.Auth.signIn(username,
                    password,
                    successResponse -> {
                        Log.i(TAG, "Sign in success" + successResponse.toString());
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "welcome back", Toast.LENGTH_SHORT).show();
                        });
                        Intent goToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(goToMainActivity);
                    },
                    failureResponse -> {
                        Log.e(TAG, "Sign in failed" + failureResponse.toString());
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "failed to loggedIn", Toast.LENGTH_LONG).show();
                        });
                    }
            );
        });

        Button signupIntentPage = (Button) findViewById(R.id.loginSignupIntentButton);
        signupIntentPage.setOnClickListener(v -> {
            Intent goToSignPage = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(goToSignPage);
        });
    }
}