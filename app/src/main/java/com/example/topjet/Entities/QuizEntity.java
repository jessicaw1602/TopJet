package com.example.topjet.Entities;

// all the info regarding the quiz questions...
public class QuizEntity {

    private String questionNo, question, answerOne, answerTwo, answerThree, answerFour, correctAnswer;

    // Constructor
    public QuizEntity(){}

    public QuizEntity(String questionNo, String question, String answerOne, String answerTwo, String answerThree, String answerFour, String correctAnswer) {
        this.questionNo = questionNo;
        this.question = question;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.answerFour = answerFour;
        this.correctAnswer = correctAnswer;
    }

    // Setters
    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswerOne(String answerOne) {
        this.answerOne = answerOne;
    }

    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    public void setAnswerThree(String answerThree) {
        this.answerThree = answerThree;
    }

    public void setAnswerFour(String answerFour) {
        this.answerFour = answerFour;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    // Getters
    public String getQuestionNo() {
        return questionNo;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerOne() {
        return answerOne;
    }

    public String getAnswerTwo() {
        return answerTwo;
    }

    public String getAnswerThree() {
        return answerThree;
    }

    public String getAnswerFour() {
        return answerFour;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
