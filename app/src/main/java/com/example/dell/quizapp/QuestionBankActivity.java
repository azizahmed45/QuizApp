package com.example.dell.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dell.quizapp.database.DatabaseHelper;
import com.example.dell.quizapp.models.QuestionBank;

import java.util.ArrayList;

public class QuestionBankActivity extends AppCompatActivity implements QuestionBankAdapter.OnItemClickListener {
    private static final String TAG = "QuestionBankActivity";

    private RecyclerView recyclerView;
    private QuestionBankAdapter questionBankAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<QuestionBank> questionBanks;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);

        recyclerView = findViewById(R.id.recycler_view);

        databaseHelper = new DatabaseHelper();

        databaseHelper.getQuestionYears().setOnCompleteListener(new DatabaseHelper.OnCompleteListener<ArrayList<QuestionBank>>() {
            @Override
            public void onComplete(ArrayList<QuestionBank> qBanks) {
                questionBanks = qBanks;
                Log.d(TAG, "onComplete: Question get done");
                setRecyclerView();
            }
        });

    }

    private void setRecyclerView() {
        questionBankAdapter = new QuestionBankAdapter(questionBanks);

        layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(questionBankAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }
}
