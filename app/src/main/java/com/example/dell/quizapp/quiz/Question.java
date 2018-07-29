package com.example.dell.quizapp.quiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private String question;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String answer;

    public Question() {

    }

    public Question(String question, String option_a, String option_b, String option_c, String option_d, String answer) {
        this.question = question;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.answer = answer;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    protected Question(Parcel in) {
        question = in.readString();
        option_a = in.readString();
        option_b = in.readString();
        option_c = in.readString();
        option_d = in.readString();
        answer = in.readString();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(option_a);
        parcel.writeString(option_b);
        parcel.writeString(option_c);
        parcel.writeString(option_d);
        parcel.writeString(answer);
    }

    public String getActualAnswer() {
        if (answer.equals("option_a")) {
            return option_a;
        } else if (answer.equals("option_b")) {
            return option_b;
        } else if (answer.equals("option_c")) {
            return option_c;
        } else if (answer.equals("option_d")) {
            return option_d;
        } else {
            return "Unknown error in answer";
        }

    }
}
