package com.example.dell.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dell.quizapp.quiz.Question;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private CustomActionBar actionBar;
    private ArrayList<Question> questions;
    private String givenAnswers[];

    private RecyclerView resultView;
    private RecyclerView.LayoutManager layoutManager;
    private ResultAdapter adapter;

    private TextView correctCountView;
    private TextView inCorrectCountView;
    private TextView unAnsweredCountView;
    private TextView totalMarksView;

    private int correct = 0;
    private int inCorrect = 0;
    private int unAnswered = 0;

    private Double totalMarks = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initialize();

        getIntentData();

        counter();

        setAdapter();

        setActionBar();
    }

    private void initialize() {
        correctCountView = findViewById(R.id.correct_count);
        inCorrectCountView = findViewById(R.id.incorrect_count);
        unAnsweredCountView = findViewById(R.id.unanswered_count);
        totalMarksView = findViewById(R.id.total_marks);

        resultView = findViewById(R.id.answers_recycler_view);
    }

    private void getIntentData() {
        questions = getIntent().getExtras().getParcelableArrayList("questions");
        givenAnswers = getIntent().getExtras().getStringArray("givenAnswers");
    }

    private void setAdapter() {
        adapter = new ResultAdapter(questions, givenAnswers);

        layoutManager = new LinearLayoutManager(this);

        resultView.setLayoutManager(layoutManager);
        resultView.setAdapter(adapter);
    }

    private void counter() {
        for (int i = 0; i < questions.size(); i++) {
            if (givenAnswers[i] == null) {
                unAnswered += 1;
            } else if (givenAnswers[i].equals(questions.get(i).getAnswer())) {
                correct += 1;
            } else if (!givenAnswers[i].equals(questions.get(i).getAnswer())) {
                inCorrect += 1;
            }
        }

        totalMarks = correct - (inCorrect * 0.25);

        correctCountView.setText("Correct: " + correct);
        inCorrectCountView.setText("Incorrect: " + inCorrect);
        unAnsweredCountView.setText("Not Answered: " + unAnswered);
        totalMarksView.setText("Total Marks: " + new DecimalFormat("##.##").format(totalMarks));

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
