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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChaptersActivity extends AppCompatActivity implements ChapterListAdapter.OnItemClickListener {

    private FirebaseFirestore db;

    private RecyclerView chaptersRecycleView;
    private RecyclerView.LayoutManager layoutManager;
    private ChapterListAdapter adapter;

    private ProgressBar progressBar;

    private ArrayList<Integer> chapterNumbers;
    private ArrayList<String> chapterNames;

    private int questionType;

    private String subjectId;
    private int subjectFieldId;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        initialize();
        setActionBar();
        getChapters();

    }

    private void initialize() {
        db = FirebaseFirestore.getInstance();

        subjectId = getIntent().getExtras().getString("subjectId");
        subjectFieldId = getIntent().getExtras().getInt("subjectFieldId");

        chapterNumbers = new ArrayList<>();
        chapterNames = new ArrayList<>();

        chaptersRecycleView = findViewById(R.id.chapters_recycler_view);

        layoutManager = new LinearLayoutManager(this);

        progressBar = findViewById(R.id.chapter_progress);
        progressBar.setVisibility(View.VISIBLE);

        questionType = getIntent().getExtras().getInt(BaseQuestionPageActivity.QUESTION_TYPE_KEY);

        title = getIntent().getExtras().getString(DashboardActivity.INTENT_TITLE_TAG);
    }

    private void setChapters() {
        adapter = new ChapterListAdapter(chapterNumbers, chapterNames);
        adapter.setOnItemClickListener(this);

        chaptersRecycleView.setLayoutManager(layoutManager);
        chaptersRecycleView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

        if (chapterNumbers.size() <= 0) {
            Toast.makeText(this, "No chapters found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getChapters() {
        db.collection("Subjects/" + subjectId + "/Chapters")
                .orderBy("id", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                chapterNumbers.add(documentSnapshot.getLong("id").intValue());
                                chapterNames.add(documentSnapshot.getString("name"));
                                Log.d("Chapters", documentSnapshot.getString("name"));
                            }

                            setChapters();

                        } else {
                            Log.d("Chapters Activity: ", "Chapter loading failed");
                        }
                    }
                });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ChaptersActivity.this, BaseQuestionPageActivity.class);
        intent.putExtra(BaseQuestionPageActivity.SUBJECT_ID_KEY, subjectFieldId);
        intent.putExtra(BaseQuestionPageActivity.CHAPTER_ID_KEY, chapterNumbers.get(position));
        intent.putExtra(BaseQuestionPageActivity.QUESTION_TYPE_KEY, questionType);

        startActivity(intent);
    }

    private void setActionBar() {
        CustomActionBar actionBar = new CustomActionBar(this, getSupportActionBar(), title, "Chapters");
        actionBar.setUpButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
