package com.example.dell.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.dell.quizapp.quiz.Question;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private CustomActionBar actionBar;
    private ArrayList<Question> questions;
    private String givenAnswer[];

    private RecyclerView resultView;
    private RecyclerView.LayoutManager layoutManager;
    private ResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultView = findViewById(R.id.answers_recycler_view);

        questions = getIntent().getExtras().getParcelableArrayList("questions");
        givenAnswer = getIntent().getExtras().getStringArray("givenAnswers");


        adapter = new ResultAdapter(questions, givenAnswer);

        layoutManager = new LinearLayoutManager(this);
        resultView.setLayoutManager(layoutManager);

        resultView.setAdapter(adapter);


        setActionBar();


    }

    private void setActionBar() {
        actionBar = new CustomActionBar(this, getSupportActionBar(), "Result", " ");
        actionBar.setUpButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
