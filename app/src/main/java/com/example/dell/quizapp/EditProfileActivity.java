package com.example.dell.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dell.quizapp.database.DatabaseHelper;
import com.example.dell.quizapp.models.ProfileInfo;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "EditProfileActivity";

    private EditText nameField;
    private EditText emailField;

    private RadioGroup genderRadioGroup;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;

    private Button saveButton;

    private ProgressBar progressBar;

    private String name;
    private String email;
    private String gender;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initialize();


        databaseHelper.getProfileInfo().setOnCompleteListener(new DatabaseHelper.OnCompleteListener<ProfileInfo>() {
            @Override
            public void onComplete(ProfileInfo profileInfo) {
                nameField.setText(profileInfo.getName());
                emailField.setText(profileInfo.getEmail());

                if (profileInfo.getGender().equals("Male")) {
                    maleRadio.setChecked(true);
                } else if (profileInfo.getGender().equals("Female")) {
                    femaleRadio.setChecked(true);
                }
                progressBar.setVisibility(View.GONE);
            }

        });
    }

    private void initialize() {
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);

        genderRadioGroup = findViewById(R.id.gender_radio_group);
        maleRadio = findViewById(R.id.male_radio);
        femaleRadio = findViewById(R.id.female_radio);

        saveButton = findViewById(R.id.save_button);

        progressBar = findViewById(R.id.progressBar);

        genderRadioGroup.setOnCheckedChangeListener(this);

        saveButton.setOnClickListener(this);

        progressBar.setVisibility(View.VISIBLE);

        databaseHelper = new DatabaseHelper();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_button) {
            name = nameField.getText().toString();
            email = emailField.getText().toString();

            if (isValid()) {
                progressBar.setVisibility(View.VISIBLE);
                databaseHelper.setProfileInfo(new ProfileInfo(name, email, gender))
                        .setOnCompleteListener(new DatabaseHelper.OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(Boolean updated) {
                                if (updated) {
                                    Toast.makeText(EditProfileActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditProfileActivity.this, DashboardActivity.class));
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Failed to update please try again", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.male_radio) {
            gender = "Male";
            Log.d(TAG, "onCheckedChanged: Male Checked");
        } else if (checkedId == R.id.female_radio) {
            gender = "Female";
            Log.d(TAG, "onCheckedChanged: Female Checked");
        }
    }

    private boolean isValid() {
        if (name.isEmpty()) {
            nameField.setError("Enter name");
            nameField.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter valid email");
            emailField.requestFocus();
            return false;
        }
        if (gender == null) {
            Toast.makeText(this, "Choose gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
