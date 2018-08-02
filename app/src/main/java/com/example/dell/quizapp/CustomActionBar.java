package com.example.dell.quizapp;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomActionBar {
    private Context context;
    private ActionBar actionBar;
    private String title;
    private String subTitle;
    private View view;

    private TextView titleView;
    private TextView subTitleView;

    private TextView timerView;

    private ViewGroup submitView;

    private ViewGroup upButtonView;

    private static final String TAG = "CustomActionBar";

    public CustomActionBar() {

    }

    public CustomActionBar(Context context, ActionBar actionBar, String title, String subTitle) {
        this.context = context;
        this.actionBar = actionBar;
        this.title = title;
        this.subTitle = subTitle;

        bindView();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setCustomView(getView());
        actionBar.setDisplayShowCustomEnabled(true);
        Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
        parent.setContentInsetsAbsolute(0, 0);

        titleView.setText(title);
        subTitleView.setText(subTitle);

    }

    private void bindView() {
        view = LayoutInflater.from(context).inflate(R.layout.action_bar, null);

        titleView = view.findViewById(R.id.app_bar_title);
        subTitleView = view.findViewById(R.id.app_bar_subtitle);

        timerView = view.findViewById(R.id.app_bar_timer);

        upButtonView = view.findViewById(R.id.app_bar_up_button);
        submitView = view.findViewById(R.id.app_bar_submit_button);
    }

    public CustomActionBar setTimer(int min) {
        Log.d(TAG, "setTimer: set");
        new CountDownTimer(min * 60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timerView.setText(l / 60000 + "min. " + (l % 60000) / 1000 + "sec");
            }

            @Override
            public void onFinish() {

            }
        }.start();

        return this;
    }

    public CustomActionBar setAsExam() {
        timerView.setVisibility(View.VISIBLE);
        submitView.setVisibility(View.VISIBLE);
        return this;
    }

    public CustomActionBar setSubmitListener(View.OnClickListener listener) {
        submitView.setOnClickListener(listener);
        return this;
    }

    public CustomActionBar setTitle(String title) {
        this.title = title;
        titleView.setText(this.title);
        return this;
    }

    public View getView() {
        return view;
    }

    public CustomActionBar setUpButtonListener(View.OnClickListener listener) {
        upButtonView.setOnClickListener(listener);
        return this;
    }
}
