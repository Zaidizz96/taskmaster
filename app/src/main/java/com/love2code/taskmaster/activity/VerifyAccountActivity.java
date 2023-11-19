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

public class VerifyAccountActivity extends AppCompatActivity {

    public static final String TAG = "VerifyAccountActivity";
    public static final String VERIFY_ACCOUNT_EMAIL_TAG = "verifyEmailTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        Intent callingIntent = getIntent();
        String email = callingIntent.getStringExtra(SignupActivity.SIGNUP_EMAIL_TAG);

        EditText usernameEditText = findViewById(R.id.verifyUsernameEditText);
        usernameEditText.setText(email);

        Button verifyButton = (Button) findViewById(R.id.verificationCodeButton);
        verifyButton.setOnClickListener(v -> {

            String username = usernameEditText.getText().toString();
            String verificationCode = ((EditText)findViewById(R.id.verificationCodeEditText)).getText().toString();

            Amplify.Auth.confirmSignUp(username,
                    verificationCode,
                    successResponse -> {
                        Log.i(TAG, "Verification success" + successResponse.toString());
                        Intent goToLoginPage = new Intent(VerifyAccountActivity.this , LoginActivity.class);
                        goToLoginPage.putExtra(VERIFY_ACCOUNT_EMAIL_TAG , username);
                        startActivity(goToLoginPage);
                    },
                    failureResponse -> {
                        Log.e(TAG, "Verification failed" + failureResponse.toString());
                        runOnUiThread(() ->{
                          Toast.makeText(VerifyAccountActivity.this , "verify failed : check your verification code", Toast.LENGTH_LONG).show();
                        });
                    }
            );
        });
    }
}