package com.example.dell.quizapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.quizapp.models.QuestionBank;

import java.util.ArrayList;

public class QuestionBankAdapter extends RecyclerView.Adapter<QuestionBankAdapter.QuestionBankViewHolder> {
    private ArrayList<QuestionBank> questionBanks;
    private OnItemClickListener listener;

    public QuestionBankAdapter(ArrayList<QuestionBank> questionBanks) {
        this.questionBanks = questionBanks;
    }

    @NonNull
    @Override
    public QuestionBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_question_year, parent, false);
        return new QuestionBankViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionBankViewHolder holder, int position) {
        holder.yearText.setText(questionBanks.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        return questionBanks.size();
    }

    public void setOnItemCLickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class QuestionBankViewHolder extends RecyclerView.ViewHolder {

        public TextView yearText;

        public QuestionBankViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            yearText = itemView.findViewById(R.id.question_year_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
