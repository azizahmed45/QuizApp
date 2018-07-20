package com.example.dell.quizapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder> {
    private ArrayList<Integer> chapterNumbers;
    private ArrayList<String> chapterNames;

    public ChapterListAdapter(ArrayList<Integer> chapterNumber, ArrayList<String> chapterName) {
        this.chapterNumbers = chapterNumber;
        this.chapterNames = chapterName;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        ChapterViewHolder chapterViewHolder = new ChapterViewHolder(view);
        return chapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        holder.chapterNumber.setText(chapterNumbers.get(position).toString());
        holder.chapterName.setText(chapterNames.get(position));
    }

    @Override
    public int getItemCount() {
        return chapterNumbers.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        private TextView chapterNumber;
        private TextView chapterName;

        public ChapterViewHolder(View itemView) {
            super(itemView);

            chapterNumber = itemView.findViewById(R.id.chapter_number);
            chapterName = itemView.findViewById(R.id.chapter_name);
        }
    }

}
