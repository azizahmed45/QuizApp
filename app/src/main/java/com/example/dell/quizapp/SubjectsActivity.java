package com.example.dell.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity implements SubjectListAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SubjectListAdapter adapter;

    private String title;

    private int questionType;

    private ArrayList<String> subjectList;
    private ArrayList<String> subjectIds;
    private ArrayList<Integer> subjectFieldIds;

    private FirebaseFirestore db;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        initialize();
        setActionBar();
        getSubjects();
    }

    private void initialize() {
        progressBar = findViewById(R.id.subject_progress);
        progressBar.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.subject_list);

        layoutManager = new LinearLayoutManager(this);

        subjectList = new ArrayList<>();
        subjectIds = new ArrayList<>();
        subjectFieldIds = new ArrayList<>();

        title = getIntent().getExtras().getString(DashboardActivity.INTENT_TITLE_TAG);

        questionType = getIntent().getExtras().getInt(BaseQuestionPageActivity.QUESTION_TYPE_KEY);

    }

    private void setSubjects() {
        adapter = new SubjectListAdapter(subjectList);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        Log.d("Set subject: ", "Done");
    }

    private void getSubjects() {
        db.collection("Subjects")
                .orderBy("id", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(SubjectsActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                subjectList.add(documentSnapshot.getString("name"));
                                subjectIds.add(documentSnapshot.getId());
                                subjectFieldIds.add(documentSnapshot.getLong("id").intValue());
                                Log.d("Subjects: ", documentSnapshot.getString("name"));
                            }

                            setSubjects();

                        } else {
                            Log.d("subject list", "Failed");
                        }
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SubjectsActivity.this, ChaptersActivity.class);
        intent.putExtra("subjectId", subjectIds.get(position));
        intent.putExtra("subjectFieldId", subjectFieldIds.get(position));
        intent.putExtra(BaseQuestionPageActivity.QUESTION_TYPE_KEY, questionType);

        if (questionType == BaseQuestionPageActivity.QUESTION_TYPE_STUDY) {
            intent.putExtra(DashboardActivity.INTENT_TITLE_TAG, "Study");
        } else if (questionType == BaseQuestionPageActivity.QUESTION_TYPE_PRACTICE) {
            intent.putExtra(DashboardActivity.INTENT_TITLE_TAG, "Practice");
        }

        startActivity(intent);
    }

    private void setActionBar() {
        CustomActionBar actionBar = new CustomActionBar(this, getSupportActionBar(), title, "Subjects");
        actionBar.setUpButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
