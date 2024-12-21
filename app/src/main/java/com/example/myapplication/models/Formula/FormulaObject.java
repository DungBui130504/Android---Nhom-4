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
}
