package com.example.dell.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button signOutButton;
    private Button quizPracticeButton;

    private TextView phoneNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

        phoneNumberText = findViewById(R.id.phoneNumberText);

        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                checkLogin();
            }
        });
        checkLogin();

        String phoneNumber = mAuth.getCurrentUser().getPhoneNumber();

        phoneNumberText.setText(phoneNumber);

        quizPracticeButton = findViewById(R.id.quizPracticeButton);
        quizPracticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, BaseQuestionPageActivity.class));
            }
        });

    }

    private void checkLogin() {
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, BaseQuestionPageActivity.class));
            finish();
        }
    }
}
