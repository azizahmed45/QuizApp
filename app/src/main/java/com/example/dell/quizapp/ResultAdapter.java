package com.example.dell.quizapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.quizapp.quiz.Question;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.AnswersViewHolder> {
    private ArrayList<Question> questions;
    private String[] givenAnswers;

    public ResultAdapter(ArrayList<Question> questions, String[] givenAnswers) {
        this.questions = questions;
        this.givenAnswers = givenAnswers.clone();
    }


    @NonNull
    @Override
    public AnswersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_single_view, parent, false);
        AnswersViewHolder viewHolder = new AnswersViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersViewHolder holder, int position) {
        holder.question.setText(format(questions.get(position).getQuestion()));
        holder.answer.setText(format(questions.get(position).getActualAnswer()));
        if (givenAnswers[position] == null) {
            holder.result.setText("Not Answered");
        } else if (givenAnswers[position].equals(questions.get(position).getAnswer())) {
            holder.result.setText("Correct");
        } else if (!givenAnswers[position].equals(questions.get(position).getAnswer())) {
            holder.result.setText("Incorrect");
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    private String format(String s) {
        return "<div class=\"box\" style = \"                  \n" +
                "  border : 3px solid green;\n" +
                "  border-radius: 10px;\">\n" +
                "  <p style=\"\n" +
                "     margin-top: 30px;\n" +
                "     margin-bottom: 2px;\n" +
                "     margin-left: 10px;\n" +
                "     color:black;\n" +
                "     font-size: 20px\">" + s + "</p> \n" +
                "</div>";
    }

    public static class AnswersViewHolder extends RecyclerView.ViewHolder {

        public MathView question;
        public MathView answer;
        public TextView result;

        public AnswersViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question_text_in_result);
            answer = itemView.findViewById(R.id.answer_text_in_result);
            result = itemView.findViewById(R.id.result);
        }
    }

}
