package com.example.dell.quizapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.quizapp.quiz.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuizPracticeActivity extends AppCompatActivity {

    private static final String TAG = "QuizPracticeActivity";

    private FirebaseFirestore db;
    private CollectionReference questionsRef;

    private boolean questionsLoaded = false;

    private ViewGroup questionView;
    private ProgressBar progressBar;

    private TextView questionText;
    private TextView optionAText;
    private TextView optionBText;
    private TextView optionCText;
    private TextView optionDText;

    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_practice);

        assignViews();
        progressBar.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();
        questionsRef = db.collection("Questions");

        questions = new ArrayList<>();

        questionsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                Question question = documentSnapshot.toObject(Question.class);
                                questions.add(question);

                                Log.d("question", question.getQuestion());
                                Log.d("option a", question.getOption_a());
                                Log.d("option b", question.getOption_b());
                                Log.d("option c", question.getOption_c());
                                Log.d("option d", question.getOption_d());
                                Log.d("answer", question.getAnswer());
                            }
                            questionsLoaded = true;
                            setQuestion();
                            progressBar.setVisibility(View.GONE);
                            questionView.setVisibility(View.VISIBLE);
                            Log.d(TAG, "Successfully loaded questions");
                        } else {
                            Log.d(TAG, "Failed loading questions");
                        }

                    }
                });


    }

    private void assignViews() {
        questionText = findViewById(R.id.question_text);
        optionAText = findViewById(R.id.option_a_text);
        optionBText = findViewById(R.id.option_b_text);
        optionCText = findViewById(R.id.option_c_text);
        optionDText = findViewById(R.id.option_d_text);

        questionView = findViewById(R.id.questionView);
        questionView.setVisibility(View.GONE);

        progressBar = findViewById(R.id.questionLoadProgressbar);
    }

    private void setQuestion() {
        Question question = questions.get(0);

        questionText.setText(question.getQuestion());
        optionAText.setText(question.getOption_a());
        optionBText.setText(question.getOption_b());
        optionCText.setText(question.getOption_c());
        optionDText.setText(question.getOption_d());


    }
}
