package com.example.dell.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dell.quizapp.database.DatabaseHelper;
import com.example.dell.quizapp.models.Profile;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    public static String INTENT_TITLE_TAG = "title";

    private static final String TAG = "DashboardActivity";

    private FirebaseAuth mAuth;

    private CardView studyCardView;
    private CardView practiceCardView;
    private CardView examCardView;
    private CardView questionBankCardView;
    private CardView scoreCardView;
    private CardView forumCardView;

    private SharedPreferences profileInfoPref;

    private TextView profileName;
    private TextView profilePhone;

    private DrawerLayout navigationDrawer;

    private NavigationView navigationView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initialize();

        setNavigationDrawer();

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationDrawer.openDrawer(GravityCompat.START);
                Log.d(TAG, "onOptionsItemSelected: Open drawer");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navigationDrawer, toolbar, R.string.drawer_close, R.string.drawer_open);

        navigationDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    private void initialize() {
        mAuth = FirebaseAuth.getInstance();

        studyCardView = findViewById(R.id.cardview_study);
        studyCardView.setOnClickListener(this);

        practiceCardView = findViewById(R.id.cardview_practice);
        practiceCardView.setOnClickListener(this);

        examCardView = findViewById(R.id.cardview_exam);
        examCardView.setOnClickListener(this);

        questionBankCardView = findViewById(R.id.cardview_questionbank);
        questionBankCardView.setOnClickListener(this);

        scoreCardView = findViewById(R.id.cardview_score);
        scoreCardView.setOnClickListener(this);

        forumCardView = findViewById(R.id.cardview_forum);
        forumCardView.setOnClickListener(this);

        navigationDrawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);

        toolbar = findViewById(R.id.toolbar);

        profileName = navigationView.getHeaderView(0).findViewById(R.id.profile_name);
        profilePhone = navigationView.getHeaderView(0).findViewById(R.id.profile_phone);

        profileInfoPref = this.getSharedPreferences(getString(R.string.preference_profile_info_key), MODE_PRIVATE);




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardview_study:
                Intent studyIntent = new Intent(DashboardActivity.this, SubjectsActivity.class);
                studyIntent.putExtra(INTENT_TITLE_TAG, "Study");
                studyIntent.putExtra(BaseQuestionPageActivity.QUESTION_TYPE_KEY, BaseQuestionPageActivity.QUESTION_TYPE_STUDY);
                startActivity(studyIntent);
                break;
            case R.id.cardview_practice:
                Intent practiceIntent = new Intent(DashboardActivity.this, SubjectsActivity.class);
                practiceIntent.putExtra(INTENT_TITLE_TAG, "Practice");
                practiceIntent.putExtra(BaseQuestionPageActivity.QUESTION_TYPE_KEY, BaseQuestionPageActivity.QUESTION_TYPE_PRACTICE);
                startActivity(practiceIntent);
                break;
            case R.id.cardview_exam:
                Intent examIntent = new Intent(this, BaseQuestionPageActivity.class);
                examIntent.putExtra(BaseQuestionPageActivity.QUESTION_TYPE_KEY, BaseQuestionPageActivity.QUESTION_TYPE_EXAM);
                startActivity(examIntent);
                break;
            case R.id.cardview_questionbank:
                break;
            case R.id.cardview_score:
                break;
            case R.id.cardview_forum:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            navigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                mAuth.signOut();
                Log.d(TAG, "onOptionsItemSelected: sign out clicked");
                break;
        }
        return true;
    }

    private void setProfile() {

        if (profileInfoPref.contains(getString(R.string.pref_profile_name_key)) && profileInfoPref.contains(getString(R.string.pref_profile_phone_key))) {

            Log.d(TAG, "setProfile: Set profile from local");

            profileName.setText(profileInfoPref.getString(getString(R.string.pref_profile_name_key), "No name"));
            profilePhone.setText(profileInfoPref.getString(getString(R.string.pref_profile_phone_key), "No phone"));
        } else {

            DatabaseHelper databaseHelper = new DatabaseHelper();
            databaseHelper.getProfile().setOnCompleteListener(new DatabaseHelper.OnCompleteListener<Profile>() {

                @Override
                public void onComplete(Profile profile) {
                    Log.d(TAG, "onComplete: Profile collected");
                    profileInfoPref.edit()
                            .putString(getString(R.string.pref_profile_name_key), profile.getName())
                            .putString(getString(R.string.pref_profile_phone_key), profile.getPhoneNumber())
                            .apply();

                    Log.d(TAG, "onComplete: Set profile from cloud");

                    profileName.setText(profile.getName());
                    profilePhone.setText(profile.getPhoneNumber());
                }
            });
        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        setProfile();
    }

}