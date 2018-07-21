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

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        ChapterViewHolder chapterViewHolder = new ChapterViewHolder(view, listener);
        return chapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        holder.chapterNumber.setText(getChapterNumber(chapterNumbers.get(position)));
        holder.chapterName.setText(chapterNames.get(position));
    }

    public ChapterListAdapter(ArrayList<Integer> chapterNumber, ArrayList<String> chapterName) {
        this.chapterNumbers = chapterNumber;
        this.chapterNames = chapterName;
    }

    private String getChapterNumber(int number) {
        String chapterNumber;
        switch (number) {
            case 1:
                chapterNumber = "১ম";
                break;
            case 2:
                chapterNumber = "২য়";
                break;
            case 3:
                chapterNumber = "৩য়";
                break;
            case 4:
                chapterNumber = "৪র্থ";
                break;
            case 5:
                chapterNumber = "৫ম";
                break;
            case 6:
                chapterNumber = "৬ষ্ঠ";
                break;
            case 7:
                chapterNumber = "৭ম";
                break;
            case 8:
                chapterNumber = "৮ম";
                break;
            case 9:
                chapterNumber = "৯ম";
                break;
            case 10:
                chapterNumber = "১০ম";
                break;
            case 11:
                chapterNumber = "১১তম";
                break;
            case 12:
                chapterNumber = "১২তম";
                break;
            case 13:
                chapterNumber = "১৩তম";
                break;
            case 14:
                chapterNumber = "১৪তম";
                break;
            case 15:
                chapterNumber = "১৫তম";
                break;
            case 16:
                chapterNumber = "১৬তম";
                break;
            case 17:
                chapterNumber = "১৭তম";
                break;
            case 18:
                chapterNumber = "১৮তম";
                break;
            case 19:
                chapterNumber = "১৯তম";
                break;
            case 20:
                chapterNumber = "২০তম";
                break;
            default:
                chapterNumber = number + " তম";
        }
        return chapterNumber;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return chapterNumbers.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        private TextView chapterNumber;
        private TextView chapterName;

        public ChapterViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            chapterNumber = itemView.findViewById(R.id.chapter_number);
            chapterName = itemView.findViewById(R.id.chapter_name);

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
