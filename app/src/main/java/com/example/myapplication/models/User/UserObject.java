package com.example.myapplication.models.User;

public class UserObject {
    public int userID;
    public String userName;
    public String passWord;
    public String gmail;
    public String fullName;
    public String phoneNumber;
    public UserObject(String userName, String password,String gmail ,String fullName, String phoneNumber){
        this.userName = userName;
        this.passWord = password;
        this.gmail = gmail;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public UserObject(int userID,String userName, String password,String gmail ,String fullName, String phoneNumber){
        this.userID = userID;
        this.userName = userName;
        this.passWord = password;
        this.gmail = gmail;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public UserObject(){
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
