package com.example.kidsappfyp.Activities.ML;

public class QuestionsModel {
    private String question, answer, questionImage;

    public QuestionsModel() {
    }

    public QuestionsModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public QuestionsModel(String question, String answer, String questionImage) {
        this.question = question;
        this.answer = answer;
        this.questionImage = questionImage;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }
}
