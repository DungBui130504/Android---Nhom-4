package com.example.myapplication.models.Formula;


public class FormulaObject {

    public int formulaID;
    public String formulaName;
    public String formulaContent;
    public int userID;
    public int subjectID;
    public String updatedDate;

    public FormulaObject() {
        this.formulaID = -1;
        this.userID = -1;
        this.subjectID = -1;
        this.formulaContent = "none";
        this.formulaName = "none";
        this.updatedDate = "none";
    }

    public FormulaObject(int formulaID, String formulaName, String formulaContent, int userID, int subjectID, String updatedDate) {
        this.formulaID = formulaID;
        this.formulaName = formulaName;
        this.formulaContent = formulaContent;
        this.userID = userID;
        this.subjectID = subjectID;
        this.updatedDate = updatedDate;
    }

    public FormulaObject( String formulaName, String formulaContent, int userID, int subjectID, String updatedDate) {
        this.formulaName = formulaName;
        this.formulaContent = formulaContent;
        this.userID = userID;
        this.subjectID = subjectID;
        this.updatedDate = updatedDate;
    }
    @Override
    public String toString() {
        return String.format("User ID: %s | Formula Name: %s | Formula: %s ", userID, formulaName, formulaContent);
    }

    public int getFormulaID() {
        return formulaID;
    }

    public void setFormulaID(int formulaID) {
        this.formulaID = formulaID;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getFormulaContent() {
        return formulaContent;
    }

    public void setFormulaContent(String formulaContent) {
        this.formulaContent = formulaContent;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
