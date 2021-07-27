package com.example.topjet;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.topjet.Entities.ContentEntity;
import com.example.topjet.Entities.QuizEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Source code is mainly adapted from: https://www.youtube.com/watch?v=tlgrX3HF6AI */
public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    // FireStore Database values to retrieve the questions
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER_ONE = "answerOne";
    private static final String KEY_ANSWER_TWO = "answerTwo";
    private static final String KEY_ANSWER_THREE = "answerThree";
    private static final String KEY_ANSWER_FOUR = "answerFour";
    private static final String KEY_CORRECT_ANSWER = "correctAnswer";

    // FireStore Database to insert user's quiz attempt
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOTAL_SCORE = "score";
    private static final String KEY_DATE = "date";
    private static final String KEY_QUIZ_TOPIC = "quizTopic";
    private static final String KEY_QUIZ = "topicArea";
    private static final String KEY_ID = "docId";

    // FireStore
    private static final String KEY_USER_SCORE = "score";

    QuizEntity currentQuestion;
    private List<QuizEntity> listOfQuizQues;

    private int testScore;
    private int questionCounter = 0;
    private int maxQuestions;
    private boolean answered = false;

    //    private RadioGroup radioGroup;
    private Button answerOne, answerTwo, answerThree, answerFour;
    private TextView quizHeading, questionNum, question, score, showAnswer;
    private Button submitAns;
    private ColorStateList originalTextColour;

    private ProgressBar questionProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate Fragment's view
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

//        radioGroup = view.findViewById(R.id.radio);
        answerOne = view.findViewById(R.id.choiceOne);
        answerTwo = view.findViewById(R.id.choiceTwo);
        answerThree = view.findViewById(R.id.choiceThree);
        answerFour = view.findViewById(R.id.choiceFour);

        quizHeading = view.findViewById(R.id.tvQuizHeading);
        questionNum = view.findViewById(R.id.tvQuizQuesCounter);
        question = view.findViewById(R.id.tvQuestion);
        score = view.findViewById(R.id.tvQuizScore);
        showAnswer = view.findViewById(R.id.tvShowAns); // will show whether the user's answer is correct or incorrect
        submitAns = view.findViewById(R.id.btSubmit);

        questionProgressBar = view.findViewById(R.id.questionProgressBar);
        questionProgressBar.setMax(maxQuestions);



        //set submit answer button to invisible
        submitAns.setVisibility((View.GONE));

        // Retrieve bundles
        String email = getArguments().getString("email");
        Log.d(TAG, "The user's email is: " + email);

        // Retrieve the topicArea's text from ViewContentFragment and retrieve the correct Quiz questions
        String topicAreas = getArguments().getString("topicArea");
        String quizTopicArea = topicAreas + " Quiz"; // quizTopicArea will be used to get the values form the DB
        Log.d(TAG, "Quiz topicArea is: " + quizTopicArea);

        // Setting the values
        score.setText("Score: 0"); // set the initial score to 0
        originalTextColour = answerOne.getTextColors(); // retrieve the default text colour
        quizHeading.setText(quizTopicArea); // Set the quiz heading

        listOfQuizQues = new ArrayList<>();

        database.collection(quizTopicArea).get() // return all the values from the collection
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                // Create QuizEntity
                                QuizEntity quizEntity = new QuizEntity();
                                String question = document.getString(KEY_QUESTION);
                                String answerOne = document.getString(KEY_ANSWER_ONE);
                                String answerTwo = document.getString(KEY_ANSWER_TWO);
                                String answerThree = document.getString(KEY_ANSWER_THREE);
                                String answerFour = document.getString(KEY_ANSWER_FOUR);
                                String correctAnswer = document.getString(KEY_CORRECT_ANSWER);

                                // create an array list to store all the values retrieved from the database
                                listOfQuizQues.add(new QuizEntity(question, answerOne, answerTwo, answerThree, answerFour, correctAnswer));
                                maxQuestions = listOfQuizQues.size(); // get the size of the list and save it as maxQuestions
                                Log.d(TAG, "The number of pages is: " + maxQuestions);
                            }
                            showNextQuestion(email, quizTopicArea, topicAreas);

                        } else {
                            Log.d(TAG, "Unsuccessful in Retrieving the Quiz Data");
                        } // end of if-else statement
                    }
                }); // end of database query
        answerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerOne.getText().toString().equals(currentQuestion.getCorrectAnswer())) {
                    testScore++;
                    score.setText("Score: " + testScore);
                    answerOne.setTextColor(Color.GREEN);
                    //showCorrectAnswer();
                    showAnswer.setText("Good Job! Your answer is correct!");
                } else { // if wrong
                    showAnswer.setText("Sorry, your answer is incorrect");
                    answerOne.setTextColor(Color.RED);
                    if (answerTwo.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerTwo.setTextColor(Color.GREEN);
                    } else if (answerThree.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerThree.setTextColor(Color.GREEN);
                    } else if (answerFour.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerFour.setTextColor(Color.GREEN);
                    }
                    //showCorrectAnswer();
                }
                Log.d(TAG, "The question counter in the submit button: " + questionCounter);
                if (questionCounter < maxQuestions) {
                    submitAns.setVisibility((View.VISIBLE));
                    submitAns.setText("Next");
                    submitAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showNextQuestion(email, quizTopicArea, topicAreas);
                            submitAns.setVisibility((View.GONE));
                        }
                    });
                } else { // if the test has finished
                    submitAns.setVisibility((View.VISIBLE));
                    submitAns.setText("Finished");
                    submitAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishQuiz(email, quizTopicArea, topicAreas);
                        }
                    });
                }
            }
        });
        answerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerTwo.getText().toString().equals(currentQuestion.getCorrectAnswer())) {
                    testScore++;
                    score.setText("Score: " + testScore);
                    answerTwo.setTextColor(Color.GREEN);
                   // showCorrectAnswer();
                    showAnswer.setText("Good Job! Your answer is correct!");
                } else { // if wrong
                    showAnswer.setText("Sorry, your answer is incorrect");
                    answerTwo.setTextColor(Color.RED);
                    if (answerOne.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerOne.setTextColor(Color.GREEN);
                    } else if (answerThree.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerThree.setTextColor(Color.GREEN);
                    } else if (answerFour.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerFour.setTextColor(Color.GREEN);
                    }
                   // showCorrectAnswer();
                }
                Log.d(TAG, "The question counter in the submit button: " + questionCounter);
                if (questionCounter < maxQuestions) {
                    submitAns.setVisibility((View.VISIBLE));
                    submitAns.setText("Next");
                    submitAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showNextQuestion(email, quizTopicArea, topicAreas);
                            submitAns.setVisibility((View.GONE));
                        }
                    });
                } else { // if the test has finished
                    submitAns.setVisibility((View.VISIBLE));
                    submitAns.setText("Finished");
                    submitAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishQuiz(email, quizTopicArea, topicAreas);
                        }
                    });
                }
            }
        });
        answerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerThree.getText().toString().equals(currentQuestion.getCorrectAnswer())) {
                    testScore++;
                    score.setText("Score: " + testScore);
                    answerThree.setTextColor(Color.GREEN);
                    //showCorrectAnswer();
                    showAnswer.setText("Good Job! Your answer is correct!");
                } else { // if wrong
                    showAnswer.setText("Sorry, your answer is incorrect!");
                    answerThree.setTextColor(Color.RED);
                    if (answerTwo.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerTwo.setTextColor(Color.GREEN);
                    } else if (answerOne.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerOne.setTextColor(Color.GREEN);
                    } else if (answerFour.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerFour.setTextColor(Color.GREEN);
                    }
                   // showCorrectAnswer();
                }
                Log.d(TAG, "The question counter in the submit button: " + questionCounter);
                if (questionCounter < maxQuestions) {
                    submitAns.setVisibility((View.VISIBLE));
                    submitAns.setText("Next");
                    submitAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showNextQuestion(email, quizTopicArea, topicAreas);
                            submitAns.setVisibility((View.GONE));
                        }
                    });
                } else { // if the test has finished
                    submitAns.setVisibility((View.VISIBLE));
                    submitAns.setText("Finished");
                    submitAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishQuiz(email, quizTopicArea, topicAreas);
                        }
                    });
                }
            }
        });
        answerFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerFour.getText().toString().equals(currentQuestion.getCorrectAnswer())) {
                    testScore++;
                    score.setText("Score: " + testScore);
                    answerFour.setTextColor(Color.GREEN);
                    //showCorrectAnswer();
                    showAnswer.setText("Good Job! Your answer is correct!");
                } else { // if wrong
                    showAnswer.setText("Sorry, your answer is incorrect");
                    answerFour.setTextColor(Color.RED);
                    if (answerTwo.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerTwo.setTextColor(Color.GREEN);
                    } else if (answerThree.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerThree.setTextColor(Color.GREEN);
                    } else if (answerOne.getText().equals(currentQuestion.getCorrectAnswer())) {
                        answerOne.setTextColor(Color.GREEN);
                    }
                   // showCorrectAnswer();
                }
                Log.d(TAG, "The question counter in the submit button: " + questionCounter);
                if (questionCounter < maxQuestions) {
                    submitAns.setVisibility((View.VISIBLE));
                    submitAns.setText("Next");
                    submitAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showNextQuestion(email, quizTopicArea, topicAreas);
                            submitAns.setVisibility((View.GONE));
                        }
                    });
                } else { // if the test has finished
                    submitAns.setVisibility((View.VISIBLE));
                    submitAns.setText("Finished");
                    submitAns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishQuiz(email, quizTopicArea, topicAreas);
                        }
                    });
                }
            }
        });
//        submitAns.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // We first want to check whether the user has answered the question or not.
//                if (!answered){ // if the user has answered (true)
//                    // check which option of the radio group they have chosen
//                    if (answerOne.isChecked() || answerTwo.isChecked()
//                            || answerThree.isChecked() || answerFour.isChecked()){
//                        // we want to check whether the answer is correct or incorrect
//                        RadioButton selected = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId()); // returns an int of the ID
//                        answered = true;
//                        String getCorrectAns = currentQuestion.getCorrectAnswer();
//                        String btnString = selected.getText().toString();
//
//                        // Check whether the user's answer is correct
//                        if (btnString.equals(getCorrectAns)){ // if answer is correct
//                            testScore++;
//                            score.setText("Score: " + testScore);
//                            showCorrectAnswer();
//                            showAnswer.setText("Good Job! Your answer is correct!");
//                        } else { // if wrong
//                            showAnswer.setText("Sorry, your answer is incorrect");
//                            showCorrectAnswer();
//                        }
//                        Log.d(TAG, "The question counter in the submit button: " + questionCounter);
//                        if (questionCounter < maxQuestions){
//                            submitAns.setText("Next");
//                        } else { // if the test has finished
//                            submitAns.setText("Finished");
//                            finishQuiz(email, quizTopicArea);
//                        }
//
//                    } else {
//                        Toast.makeText(getContext(),"Please select an answer", Toast.LENGTH_SHORT);
//                    }
//                } else {
//                    showNextQuestion(email, quizTopicArea);
//                } // end of outer if-else statement
//            }
//        }); // end of submitAns.setOnClickListener
        return view;
    } // end of onCreateView

//    private void showCorrectAnswer() {
//        String correctAnswer = currentQuestion.getCorrectAnswer();
//
//        if (answerOne.getText().equals(correctAnswer)) {
//            answerOne.setTextColor(Color.GREEN);
//        } else if (!answerOne.getText().equals(correctAnswer)) {
//            answerOne.setTextColor(Color.RED);
//            if (answerTwo.getText().equals(correctAnswer)) {
//                answerTwo.setTextColor(Color.GREEN);
//            } else if (answerThree.getText().equals(correctAnswer)) {
//                answerThree.setTextColor(Color.GREEN);
//            } else if (answerFour.getText().equals(correctAnswer)) {
//                answerFour.setTextColor(Color.GREEN);
//            }
//        }
//        if (answerTwo.getText().equals(correctAnswer)) {
//            answerTwo.setTextColor(Color.GREEN);
//        } else if (!answerTwo.getText().equals(correctAnswer)) {
//            answerTwo.setTextColor(Color.RED);
//            if (answerOne.getText().equals(correctAnswer)) {
//                answerOne.setTextColor(Color.GREEN);
//            } else if (answerThree.getText().equals(correctAnswer)) {
//                answerThree.setTextColor(Color.GREEN);
//            } else if (answerFour.getText().equals(correctAnswer)) {
//                answerFour.setTextColor(Color.GREEN);
//            }
//        }
//        if (answerThree.getText().equals(correctAnswer)) {
//            answerThree.setTextColor(Color.GREEN);
//        } else if (!answerThree.getText().equals(correctAnswer)) {
//            answerThree.setTextColor(Color.RED);
//            if (answerTwo.getText().equals(correctAnswer)) {
//                answerTwo.setTextColor(Color.GREEN);
//            } else if (answerOne.getText().equals(correctAnswer)) {
//                answerOne.setTextColor(Color.GREEN);
//            } else if (answerFour.getText().equals(correctAnswer)) {
//                answerFour.setTextColor(Color.GREEN);
//            }
//        }
//        if (answerFour.getText().equals(correctAnswer)) {
//            answerFour.setTextColor(Color.GREEN);
//        } else if (!answerFour.getText().equals(correctAnswer)) {
//            answerFour.setTextColor(Color.RED);
//            if (answerTwo.getText().equals(correctAnswer)) {
//                answerTwo.setTextColor(Color.GREEN);
//            } else if (answerThree.getText().equals(correctAnswer)) {
//                answerThree.setTextColor(Color.GREEN);
//            } else if (answerOne.getText().equals(correctAnswer)) {
//                answerOne.setTextColor(Color.GREEN);
//            }
//        }
//    } // end of showCorrectAnswer method

    private void showNextQuestion(String email, String quizTopicArea, String topicAreas) {
        // Clear all the text and checks
        showAnswer.setText(""); // we don't want to show whether the user's answer is correct/incorrect yet
        //radioGroup.clearCheck(); // clear all the checked answers
        // Set the colours of the text to black
        answerOne.setTextColor(originalTextColour);
        answerTwo.setTextColor(originalTextColour);
        answerThree.setTextColor(originalTextColour);
        answerFour.setTextColor(originalTextColour);
        Log.d(TAG, "You are in the showNextQuestion method");

        Log.d(TAG, "The question counter is: " + questionCounter);
        if (questionCounter < maxQuestions) { // if user hasn't finished the quiz
            Log.d(TAG, "max questions in showNextQuestion is: " + maxQuestions);

            currentQuestion = listOfQuizQues.get(questionCounter);
            question.setText(currentQuestion.getQuestion());
            answerOne.setText(currentQuestion.getAnswerOne());
            answerTwo.setText(currentQuestion.getAnswerTwo());
            answerThree.setText(currentQuestion.getAnswerThree());
            answerFour.setText(currentQuestion.getAnswerFour());

            questionCounter++; // increment the question number
            questionProgressBar.setProgress(questionCounter);

            answered = false;
            questionNum.setText("Question: " + questionCounter + "/" + maxQuestions);
        } else {
            finishQuiz(email, quizTopicArea, topicAreas);
        }

    } // end of showNextQuestion method

    private void finishQuiz(String email, String quizTopicArea, String topicArea) {
        Log.d(TAG, "Users score is: " + testScore);

        // Get the username of the user
        DocumentReference userRef = database.collection("Users").document(email);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // return the username
                    String currentUsersScore = documentSnapshot.getString(KEY_USER_SCORE);
                    int currentUserScore = Integer.parseInt(currentUsersScore);
                    String username = documentSnapshot.getString(KEY_USERNAME);

                    // get the date & time
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    String getDate = formatter.format(date);

                    // Create a new Collection to store all the Quiz Attempts and create a document that will have the HashMap elements
                    DocumentReference postRef = database.collection("Quiz Attempts").document();
                    String docId = postRef.getId();

                    // Create the new Quiz attempt with the username
                    Map<String, Object> newAttempt = new HashMap<>();
                    newAttempt.put(KEY_USERNAME, username);
                    newAttempt.put(KEY_EMAIL, email);
                    newAttempt.put(KEY_DATE, getDate);
                    newAttempt.put(KEY_TOTAL_SCORE, testScore);
                    newAttempt.put(KEY_QUIZ_TOPIC, quizTopicArea);
                    newAttempt.put(KEY_QUIZ, topicArea);
                    newAttempt.put(KEY_ID, docId);

                    // Add the new attempt into the database
                    database.collection("Quiz Attempts").document(docId).set(newAttempt)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Added new Attempt");
                                    // You want to go back to the search page.
                                    goToSearchFragment(email);

                                    // Now we want to update the user's score.
                                    int updateUserScore = (currentUserScore + testScore);
                                    String updateScore = String.valueOf(updateUserScore);
                                    database.collection("Users").document(email).update(KEY_USER_SCORE, updateScore);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Sorry, there was an Error!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Error! Please Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        }); // end of document reference

    } // end of finishQuiz method

    private void goToSearchFragment(String email) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchFragment searchFragment = new SearchFragment(); // generate a new searchFragment

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        searchFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, searchFragment); // replace the current frame with searchFragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    } // end of goToSearchFragment method

}