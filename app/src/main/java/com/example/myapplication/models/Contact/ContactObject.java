package com.example.myapplication.models.Contact;

public class ContactObject {

    public int contactID;
    public String gmail;
    public String name;
    public boolean isTeacher;
    public int userID;

    public ContactObject(int contactID, String gmail, String name, boolean isTeacher, int userID) {
        this.contactID = contactID;
        this.gmail = gmail;
        this.name = name;
        this.isTeacher = isTeacher;
        this.userID = userID;
    }

    public ContactObject(String gmail, String name, boolean isTeacher, int userID) {
        this.gmail = gmail;
        this.name = name;
        this.isTeacher = isTeacher;
        this.userID = userID;
    }

    public ContactObject() {
        this.contactID = -1;
        this.gmail = "none";
        this.name = "none";
        this.isTeacher = false;
        this.userID = -1;
    }
}
