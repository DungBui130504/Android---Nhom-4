package com.example.myapplication.models.Subject;

public class SubjectObject {
    public int subjectID;
    public String subjectName;
    public int userID;
    private boolean isChecked;


    public SubjectObject(int subjectID, String subjectName ,int userID) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.userID = userID;
        this.isChecked = false;

    }
    public SubjectObject( String subjectName) {
        this.subjectName = subjectName;
        this.userID = userID;
    }

    public SubjectObject() {
        this.subjectID = -1;
        this.subjectName = "none";
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "SubjectObject{" +
                "subjectID=" + subjectID +
                ", subjectName='" + subjectName + '\'' +
                ", userID=" + userID +
                '}';
    }
}
