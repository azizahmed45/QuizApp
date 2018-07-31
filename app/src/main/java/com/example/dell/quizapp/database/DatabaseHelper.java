package com.example.dell.quizapp.database;

import android.os.CountDownTimer;
import android.util.Log;

import com.example.dell.quizapp.models.Profile;
import com.example.dell.quizapp.models.Question;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper {
    public static final int EXAM_QUESTION = 1;
    private static final String TAG = "DatabaseHelper";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference questionRef = db.collection("Questions");
    private OnCompleteListener listener;

    public DatabaseHelper() {
        Log.d(TAG, "DatabaseHelper: object called");
    }

    public DatabaseHelper makeQuestions(int questionType) {

        final ArrayList<Question> questions = new ArrayList<>();

        if (questionType == EXAM_QUESTION) {

            Log.d(TAG, "makeQuestions: Question collect start");

            //countDown for debug
            new CountDownTimer(120000, 1000) {
                int i = 0;

                @Override
                public void onTick(long l) {
                    i++;
                    Log.d(TAG, "onTick: " + i);
                }

                @Override
                public void onFinish() {

                }
            }.start();

            final Task<QuerySnapshot> phy1 = questionRef.whereEqualTo("subjectId", 1).get();
            final Task<QuerySnapshot> phy2 = questionRef.whereEqualTo("subjectId", 2).get();

            final Task<QuerySnapshot> chem1 = questionRef.whereEqualTo("subjectId", 3).get();
            final Task<QuerySnapshot> chem2 = questionRef.whereEqualTo("subjectId", 4).get();

            final Task<QuerySnapshot> bio1 = questionRef.whereEqualTo("subjectId", 5).get();
            final Task<QuerySnapshot> bio2 = questionRef.whereEqualTo("subjectId", 6).get();

            final Task<QuerySnapshot> eng = questionRef.whereEqualTo("subjectId", 7).get();

            final Task<QuerySnapshot> genK = questionRef.whereEqualTo("subjectId", 8).get();

            Tasks.whenAllSuccess(phy1, phy2, chem1, chem2, bio1, bio2, eng, genK)
                    .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {

                            Log.d(TAG, "onSuccess: question collected now just making");

                            if (phy1.getResult().size() > 10 && phy2.getResult().size() > 10 &&
                                    chem1.getResult().size() > 15 && chem2.getResult().size() > 15 &&
                                    bio1.getResult().size() > 15 && bio2.getResult().size() > 15 &&
                                    eng.getResult().size() > 15 && genK.getResult().size() > 15) {


                                List<DocumentSnapshot> phy1Doc = phy1.getResult().getDocuments();
                                List<DocumentSnapshot> phy2Doc = phy2.getResult().getDocuments();

                                List<DocumentSnapshot> chem1Doc = chem1.getResult().getDocuments();
                                List<DocumentSnapshot> chem2Doc = chem2.getResult().getDocuments();

                                List<DocumentSnapshot> bio1Doc = bio1.getResult().getDocuments();
                                List<DocumentSnapshot> bio2Doc = bio2.getResult().getDocuments();

                                List<DocumentSnapshot> engDoc = eng.getResult().getDocuments();
                                List<DocumentSnapshot> genKDoc = genK.getResult().getDocuments();

                                Collections.shuffle(phy1Doc);
                                for (int i = 0; i < 10; i++) {
                                    questions.add(phy1Doc.get(i).toObject(Question.class));
                                }

                                Collections.shuffle(phy2Doc);
                                for (int i = 0; i < 10; i++) {
                                    questions.add(phy2Doc.get(i).toObject(Question.class));
                                }

                                Collections.shuffle(chem1Doc);
                                for (int i = 0; i < 10; i++) {
                                    questions.add(chem1Doc.get(i).toObject(Question.class));
                                }

                                Collections.shuffle(chem2Doc);
                                for (int i = 0; i < 15; i++) {
                                    questions.add(chem2Doc.get(i).toObject(Question.class));
                                }

                                Collections.shuffle(bio1Doc);
                                for (int i = 0; i < 15; i++) {
                                    questions.add(bio1Doc.get(i).toObject(Question.class));
                                }

                                Collections.shuffle(bio2Doc);
                                for (int i = 0; i < 15; i++) {
                                    questions.add(bio2Doc.get(i).toObject(Question.class));
                                }

                                Collections.shuffle(engDoc);
                                for (int i = 0; i < 15; i++) {
                                    questions.add(engDoc.get(i).toObject(Question.class));
                                }

                                Collections.shuffle(genKDoc);
                                for (int i = 0; i < 10; i++) {
                                    questions.add(genKDoc.get(i).toObject(Question.class));
                                }
                            } else {
                                Log.d(TAG, "onSuccess: Not enough questions");
                            }
                            Log.d(TAG, "onSuccess: " + phy1.getResult().size() + " " +
                                    phy2.getResult().size() + " " +
                                    chem1.getResult().size() + " " +
                                    chem2.getResult().size() + " " +
                                    bio1.getResult().size() + " " +
                                    bio2.getResult().size() + " " +
                                    eng.getResult().size() + " " +
                                    genK.getResult().size() + " ");
                            listener.onComplete(questions);
                        }
                    });
        }
        return this;
    }

    public DatabaseHelper getProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DocumentReference profileRef = db.document("Users/" + user.getUid());

        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "onEvent: Loading profile from cloud");
                Profile profile = new Profile();
                profile.setName(documentSnapshot.get("name").toString());
                profile.setPhoneNumber(documentSnapshot.get("phone").toString());

                listener.onComplete(profile);
            }
        });

        return this;
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.listener = listener;
    }

    public interface OnCompleteListener<T> {
        void onComplete(T var);
    }
}
