package com.example.dell.quizapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dell.quizapp.database.DatabaseHelper;
import com.example.dell.quizapp.quiz.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.github.kexanie.library.MathView;

public class BaseQuestionPageActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "BaseQuestionPage";

    public static final String QUESTION_TYPE_KEY = "QUESTION_TYPE";
    public static final int QUESTION_TYPE_STUDY = 1;
    public static final int QUESTION_TYPE_PRACTICE = 2;
    public static final int QUESTION_TYPE_EXAM = 3;

    public static final String SUBJECT_ID_KEY = "SUBJECT_ID";
    public static final String CHAPTER_ID_KEY = "CHAPTER_ID";

    private int questionType;
    private int subjectId;
    private int chapterId;

    private FirebaseFirestore db;
    private CollectionReference questionsRef;

    private boolean questionsLoaded = false;
    private int nowOnQuestionNumberAt = 0;
    private boolean bookmarked = false;

    private ViewGroup questionView;
    private ProgressBar progressBar;

    private static final int CLICK_ACTION_THRESHOLD = 200;
    private MathView questionText;
    private MathView optionAText;
    private MathView optionBText;
    private MathView optionCText;

    private ViewGroup previousButton;
    private ViewGroup gotoButton;
    private ViewGroup bookmarkButton;
    private ViewGroup nextButton;

    private CustomActionBar actionBar;

    private ImageView bookmarkIcon;

    private AlertDialog gotoDialog;

    private EditText gotoEditText;

    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_question_page);

        db = FirebaseFirestore.getInstance();
        questionsRef = db.collection("Questions");

        questions = new ArrayList<>();

        initialize();

        progressBar.setVisibility(View.VISIBLE);

        loadQuestions();
    }

    private void initialize() {
        questionText = findViewById(R.id.question_text);
        optionAText = findViewById(R.id.option_a_text);
        optionBText = findViewById(R.id.option_b_text);
        optionCText = findViewById(R.id.option_c_text);
        optionDText = findViewById(R.id.option_d_text);

        questionView = findViewById(R.id.questionView);
        questionView.setVisibility(View.GONE);

        progressBar = findViewById(R.id.questionLoadProgressbar);

        previousButton = findViewById(R.id.previous_button);
        previousButton.setOnClickListener(this);

        gotoButton = findViewById(R.id.goto_button);
        gotoButton.setOnClickListener(this);

        bookmarkButton = findViewById(R.id.bookmark_button);
        bookmarkButton.setOnClickListener(this);

        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        bookmarkIcon = findViewById(R.id.bookmark_icon);

        getIntentExtras();
    }

    private void getIntentExtras() {
        questionType = getIntent().getExtras().getInt(QUESTION_TYPE_KEY);

        if (questionType == QUESTION_TYPE_STUDY || questionType == QUESTION_TYPE_PRACTICE) {
            subjectId = getIntent().getExtras().getInt(SUBJECT_ID_KEY);
            chapterId = getIntent().getExtras().getInt(CHAPTER_ID_KEY);
        }

    }

    private MathView optionDText;

    private void startPractice() {
        if (questions.size() > 0) {
            setActionBar();
            setQuestion(nowOnQuestionNumberAt);
            makeQuestionPageAs(questionType);
        } else {
            Toast.makeText(this, "No questions found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuestions() {
        Log.d("SubjectId:", subjectId + "");
        Log.d("ChapterId:", chapterId + "");

        if (questionType == QUESTION_TYPE_STUDY || questionType == QUESTION_TYPE_PRACTICE) {
            questionsRef.whereEqualTo("subjectId", subjectId)
                    .whereEqualTo("chapterId", chapterId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                    Question question = documentSnapshot.toObject(Question.class);
                                    questions.add(question);
                                }
                                questionsLoaded = true;
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "Successfully loaded questions");

                                startPractice();

                            } else {
                                questionsLoaded = false;
                                Toast.makeText(BaseQuestionPageActivity.this, "Failed loading questions", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Failed loading questions");
                            }

                        }
                    });
        } else if (questionType == QUESTION_TYPE_EXAM) {
            DatabaseHelper dbHelp = new DatabaseHelper();
            dbHelp.makeQuestions(DatabaseHelper.EXAM_QUESTION)
                    .setOnCompleteListener(new DatabaseHelper.OnCompleteListener() {
                        @Override
                        public void onComplete(ArrayList<Question> q) {
                            questions = q;
                            questionsLoaded = true;
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "Successfully loaded questions");

                            startPractice();
                        }
                    });
        }

    }

    private void goNext() {
        if (nowOnQuestionNumberAt == questions.size() - 1) {
            nowOnQuestionNumberAt = 0;
        } else {
            nowOnQuestionNumberAt++;
        }
        setQuestion(nowOnQuestionNumberAt);
    }

    private void goPrevious() {
        if (nowOnQuestionNumberAt == 0) {
            nowOnQuestionNumberAt = questions.size() - 1;
        } else {
            nowOnQuestionNumberAt--;
        }
        setQuestion(nowOnQuestionNumberAt);
    }

    private void bookmark() {
        if (bookmarked) {
            bookmarked = false;
            bookmarkIcon.setImageResource(R.drawable.ic_bookmark);
        } else {
            bookmarked = true;
            bookmarkIcon.setImageResource(R.drawable.ic_bookmarked);
        }
    }

    private void gotoAt() {
        if (gotoDialog == null) {
            View dialogView = this.getLayoutInflater().inflate(R.layout.goto_dialog_layout, null);
            gotoEditText = dialogView.findViewById(R.id.goto_number);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(dialogView)
                    .setTitle("Go to")
                    .setPositiveButton(getString(R.string.go), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String numberString = gotoEditText.getText().toString();
                            if (numberString.isEmpty()) {
                                Toast.makeText(BaseQuestionPageActivity.this, "Did not enter a number.", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (Integer.parseInt(numberString) <= 0 || Integer.parseInt(numberString) > questions.size()) {
                                Toast.makeText(BaseQuestionPageActivity.this, "Number must between 1-" + (questions.size()), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            nowOnQuestionNumberAt = Integer.parseInt(numberString) - 1;
                            setQuestion(nowOnQuestionNumberAt);

                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gotoDialog.dismiss();
                        }
                    });
            gotoDialog = dialogBuilder.create();
        }
        gotoDialog.show();

    }

    private void makeQuestionPageAs(int questionType) {
        switch (questionType) {
            case QUESTION_TYPE_STUDY:
                setAnswer();
                break;
            case QUESTION_TYPE_PRACTICE:
                setClickListenerOnOptions();
                break;
            case QUESTION_TYPE_EXAM:
                Log.d(TAG, "makeQuestionPageAs: exam");
                break;
            default:
                break;
        }
    }

    //ClickEvents on touch (WebView)
    private long lastTouchDown;

    private void setAnswer() {
        String answer = questions.get(nowOnQuestionNumberAt).getAnswer();
        getAnswerView(answer).setBackground(getResources().getDrawable(R.drawable.fill_color_shape_green));
    }

    private View getAnswerView(String answer) {
        if (answer.equals("option_a")) {
            return optionAText;
        } else if (answer.equals("option_b")) {
            return optionBText;
        } else if (answer.equals("option_c")) {
            return optionCText;
        } else if (answer.equals("option_d")) {
            return optionDText;
        } else {
            Log.d("Question page: ", " Problem in answer");

            return new View(this);
        }
    }

    private void setQuestion(int i) {
        Question question = questions.get(i);

        questionText.setText(questionFormat(question.getQuestion()));
        optionAText.setText(optionFormat(question.getOption_a()));
        optionBText.setText(optionFormat(question.getOption_b()));
        optionCText.setText(optionFormat(question.getOption_c()));
        optionDText.setText(optionFormat(question.getOption_d()));

        //reset borders
        optionAText.setBackground(getResources().getDrawable(R.drawable.rounded_border_shape));
        optionBText.setBackground(getResources().getDrawable(R.drawable.rounded_border_shape));
        optionCText.setBackground(getResources().getDrawable(R.drawable.rounded_border_shape));
        optionDText.setBackground(getResources().getDrawable(R.drawable.rounded_border_shape));

        //Change question number title
        actionBar.setTitle(nowOnQuestionNumberAt + 1 + "/" + questions.size());

        if (questionType == QUESTION_TYPE_STUDY) {
            setAnswer();
        }

        questionView.setVisibility(View.VISIBLE);
    }

    private void setClickListenerOnOptions() {
        optionAText.setOnTouchListener(this);
        optionBText.setOnTouchListener(this);
        optionCText.setOnTouchListener(this);
        optionDText.setOnTouchListener(this);
    }

    @Override
    public void onClick(final View view) {
        if (questionsLoaded) {
            switch (view.getId()) {
                case R.id.previous_button:
                    goPrevious();
                    break;
                case R.id.next_button:
                    goNext();
                    break;
                case R.id.bookmark_button:
                    bookmark();
                    break;
                case R.id.goto_button:
                    gotoAt();
                    break;
                default:
                    break;
            }
        }
    }

    private String optionFormat(String s) {
        return "<div class=\"box\" style = \"                  \n" +
                "  border : 3px solid green;\n" +
                "  border-radius: 10px;\">\n" +
                "  <p style=\"\n" +
                "     margin-top: 2px;\n" +
                "     margin-bottom: 2px;\n" +
                "     margin-left: 30px;\n" +
                "     color:black;\n" +
                "     font-size: 20px\">" + s + "</p> \n" +
                "</div>";
    }

    private String questionFormat(String s) {
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

    @Override
    public boolean onTouch(final View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchDown = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHOLD) {
                    if (questionType == QUESTION_TYPE_PRACTICE &&
                            (view == optionAText || view == optionBText || view == optionCText || view == optionDText)) {

                        String answer = questions.get(nowOnQuestionNumberAt).getAnswer();

                        if (view == getAnswerView(answer)) {
                            setAnswer();
                        } else if (view != getAnswerView(answer)) {
                            view.setBackground(getResources().getDrawable(R.drawable.fill_color_shape_red));
                            new CountDownTimer(500, 100) {

                                @Override
                                public void onTick(long l) {

                                }

                                @Override
                                public void onFinish() {
                                    view.setBackground(getResources().getDrawable(R.drawable.rounded_border_shape));
                                    setAnswer();
                                }
                            }.start();
                        }

                    }
                }
                break;
        }
        return true;
    }

    private void setActionBar() {
        actionBar = new CustomActionBar(this, getSupportActionBar(), nowOnQuestionNumberAt + "/" + questions.size(), "Testing");
        actionBar.setUpButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}