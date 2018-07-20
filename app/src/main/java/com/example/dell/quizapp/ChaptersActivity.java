package com.example.dell.quizapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChaptersActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private RecyclerView chaptersRecycleView;
    private RecyclerView.LayoutManager layoutManager;
    private ChapterListAdapter adapter;

    private ArrayList<Integer> chapterNumbers;
    private ArrayList<String> chapterNames;

    private String subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        initialize();
        getChapters();

    }

    private void initialize() {
        db = FirebaseFirestore.getInstance();

        subjectId = getIntent().getStringExtra("subjectId");

        chapterNumbers = new ArrayList<>();
        chapterNames = new ArrayList<>();

        chaptersRecycleView = findViewById(R.id.chapters_recycler_view);

        layoutManager = new LinearLayoutManager(this);


    }

    private void setChapters() {
        adapter = new ChapterListAdapter(chapterNumbers, chapterNames);

        chaptersRecycleView.setAdapter(adapter);
        chaptersRecycleView.setLayoutManager(layoutManager);
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

}
