package com.example.dell.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText phoneNumberField;
    private EditText verificationCodeField;

    private Button signInButton;
    private Button verifyButton;
    private Button resendCodeButton;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String verificationId;
    private boolean verificationInProgress = false;

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFICATION_FAILED = 3;
    private static final int STATE_VERIFICATION_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumberField = findViewById(R.id.phoneNumberField);
        verificationCodeField = findViewById(R.id.verificationCodeField);

        signInButton = findViewById(R.id.signInButton);
        verifyButton = findViewById(R.id.verifyButton);
        resendCodeButton = findViewById(R.id.resendCodeButton);

        signInButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
        resendCodeButton.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "Verification complete");
                Toast.makeText(LoginActivity.this, "Verification complete", Toast.LENGTH_SHORT).show();
                verificationInProgress = false;
                signInWithPhoneAuthCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(TAG, "Invalid Phone Number");
                    phoneNumberField.setError("Invalid phone number.");
                    Toast.makeText(LoginActivity.this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.d(TAG, "Quota exceeded.");
                    Toast.makeText(LoginActivity.this, "Quota exceeded.", Toast.LENGTH_SHORT).show();
                } else{
                    Log.d(TAG, "Verification failed unknown error.");
                    Toast.makeText(LoginActivity.this, "Verification failed unknown error.", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(GONE);
                verificationInProgress = false;
            }

            @Override
            public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d(TAG, "Code Sent");
                verificationId = id;
                resendToken = forceResendingToken;

                Toast.makeText(LoginActivity.this, "Verification code sent.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(GONE);

                updateUi(STATE_CODE_SENT);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            updateUi(mAuth.getCurrentUser());
        }
        else{
            updateUi(STATE_INITIALIZED);
        }

        if(verificationInProgress && validPhoneNumber()){
            Log.d(TAG, "In a method in onStart");
            startPhoneNumberVerification(getPhoneNumber());
        }

    }

    private void updateUi(FirebaseUser user){
        if(user != null){
            mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        Log.d(TAG, "onAuthStateChanged: Logged out");
                    } else {
                        Log.d(TAG, "onAuthStateChanged: Logged in");
                    }
                }
            });
            Log.d(TAG, "Login success.");
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
    }


    private void updateUi(int uiState) {
        switch (uiState){
            case STATE_INITIALIZED:
                phoneNumberField.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);

                resendCodeButton.setVisibility(GONE);

                verificationCodeField.setVisibility(GONE);
                verifyButton.setVisibility(GONE);

                progressBar.setVisibility(GONE);
                break;
            case STATE_CODE_SENT:
                verificationCodeField.setVisibility(View.VISIBLE);
                verifyButton.setVisibility(View.VISIBLE);
                resendCodeButton.setVisibility(View.VISIBLE);

                signInButton.setVisibility(GONE);

                progressBar.setVisibility(GONE);

                resendCodeButton.setEnabled(false);
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        resendCodeButton.setText("Resend in " + millisUntilFinished / 1000 +" sec.");
                    }

                    public void onFinish() {
                        resendCodeButton.setText("Resend Code");
                        resendCodeButton.setEnabled(true);
                    }
                }.start();

                break;
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks
        );
        verificationInProgress = true;
    }

    private boolean validPhoneNumber() {
        String phoneNumber = phoneNumberField.getText().toString();
        if(phoneNumber.isEmpty()){
            phoneNumberField.setError("Enter a Phone Number");
            phoneNumberField.requestFocus();
            return false;
        }
        if(phoneNumber.length() != 11){
            phoneNumberField.setError("Phone number must have 11 digits.");
            phoneNumberField.requestFocus();
            return false;
        }

        return true;
    }

    private void signInWithPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(GONE);

                        if(task.isSuccessful()){
                            updateUi(mAuth.getCurrentUser());
                        }
                        else{
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                verificationCodeField.setError("Invalid code.");
                            }

                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredentials(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken resendToken) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks,
                resendToken
        );
        Log.d(TAG, "Resending code");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signInButton:
                Log.d(TAG, "sign in button clicked");
                if(!validPhoneNumber()){
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                startPhoneNumberVerification(getPhoneNumber());
                break;
            case R.id.verifyButton:
                String code = verificationCodeField.getText().toString();
                if(code.isEmpty()){
                    verificationCodeField.setError("Enter Code");
                    verificationCodeField.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyPhoneNumberWithCode(verificationId, code);
                break;
            case R.id.resendCodeButton:
                if(validPhoneNumber()){
                    progressBar.setVisibility(View.VISIBLE);
                    resendVerificationCode(getPhoneNumber(), resendToken);
                }

        }
    }

    private String getPhoneNumber(){
        String phoneNumber="+88"+phoneNumberField.getText().toString();
        Log.d(TAG, "getPhoneNumber: " + phoneNumber);
        return phoneNumber;
    }

}
