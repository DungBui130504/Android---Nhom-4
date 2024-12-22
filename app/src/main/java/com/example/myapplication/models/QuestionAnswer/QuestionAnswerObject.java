package com.example.myapplication.models.QuestionAnswer;

public class QuestionAnswerObject {
    public int questionAnswerID;
    public String questionContent;
    public String answerContent;
    public String answerUpdateDate;
    public String questionUpdateDate;
    public int subjectID;
    public boolean isAnswer;
    public int userID;
    private boolean isChecked;


    public QuestionAnswerObject() {
        this.questionAnswerID = -1;
        this.questionContent = "none";
        this.answerContent = "none";
        this.answerUpdateDate = "none";
        this.questionUpdateDate = "none";
        this.subjectID = -1;
        this.isAnswer = false;
        this.userID = -1;

    }

    public QuestionAnswerObject(int questionAnswerID, String questionContent, String answerContent, String answerUpdateDate,
                                String questionUpdateDate, int subjectID, boolean isAnswer, int userID) {
        this.questionAnswerID = questionAnswerID;
        this.questionContent = questionContent;
        this.answerContent = answerContent;
        this.answerUpdateDate = answerUpdateDate;
        this.questionUpdateDate = questionUpdateDate;
        this.subjectID = subjectID;
        this.isAnswer = isAnswer;
        this.userID = userID;
    }

    public QuestionAnswerObject(String questionContent, String answerContent, String answerUpdateDate, String questionUpdateDate,
                                int subjectID, boolean isAnswer, int userID) {
        this.questionContent = questionContent;
        this.answerContent = answerContent;
        this.answerUpdateDate = answerUpdateDate;
        this.questionUpdateDate = questionUpdateDate;
        this.subjectID = subjectID;
        this.isAnswer = isAnswer;
        this.userID = userID;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public int getQuestionAnswerID() {
        return questionAnswerID;
    }

    public void setQuestionAnswerID(int questionAnswerID) {
        this.questionAnswerID = questionAnswerID;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString(){
        return "Câu hỏi : " + this.questionContent + "\n" + "Câu trả lời : " + this.answerContent + "\n" + "----------------------------------------------------" + "\n";
    }
}
