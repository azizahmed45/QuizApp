package com.example.dell.quizapp.services;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.dell.quizapp.LoginActivity;
import com.example.dell.quizapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ApplicationService extends Application {
    private static final String TAG = "ApplicationService";

    public static FirebaseAuth firebaseAuth;
    public static FirebaseFirestore database;

    public static boolean loginActivityRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null && !loginActivityRunning) {
                    signOut();
                    Log.d(TAG, "onAuthStateChanged: Logged out");
                } else {
                    Log.d(TAG, "onAuthStateChanged: Logged in");
                }
            }
        });
    }

    private void signOut() {
        Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);

        SharedPreferences profilePref = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_profile_info_key), MODE_PRIVATE);

        profilePref.edit()
                .remove(getString(R.string.pref_profile_name_key))
                .remove(getString(R.string.pref_profile_phone_key))
                .apply();
    }
}
