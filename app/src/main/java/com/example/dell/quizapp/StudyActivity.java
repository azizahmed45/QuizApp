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

public class StudyActivity extends AppCompatActivity implements SubjectListAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SubjectListAdapter adapter;

    private ArrayList<String> subjectList;
    private ArrayList<String> subjectIds;
    private ArrayList<Integer> subjectFieldIds;

    private FirebaseFirestore db;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        initialize();
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
                .addOnCompleteListener(StudyActivity.this, new OnCompleteListener<QuerySnapshot>() {
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
        Intent intent = new Intent(StudyActivity.this, ChaptersActivity.class);
        intent.putExtra("subjectId", subjectIds.get(position));
        intent.putExtra("subjectFieldId", subjectFieldIds.get(position));

        startActivity(intent);
    }
}
