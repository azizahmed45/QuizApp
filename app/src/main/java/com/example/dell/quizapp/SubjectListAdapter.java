package com.example.dell.quizapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectListViewHolder> {
    private ArrayList<String> subjectList;
    private OnItemClickListener listener;

    public SubjectListAdapter(ArrayList<String> subjectList) {
        this.subjectList = subjectList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubjectListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_item, parent, false);
        SubjectListViewHolder viewHolder = new SubjectListViewHolder(view, listener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectListViewHolder holder, int position) {
        holder.subjectText.setText(subjectList.get(position));
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class SubjectListViewHolder extends RecyclerView.ViewHolder {

        public TextView subjectText;

        public SubjectListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            subjectText = itemView.findViewById(R.id.subject_name);

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
