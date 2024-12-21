package com.example.myapplication.models.Notification;

public class NotificationObject {
    public int notificationID;
    public String notificationDateTime;
    public String description;

    public int userID;

    public NotificationObject() {
        this.notificationID = -1;
        this.userID = -1;
        this.description = "none";
        this.notificationDateTime = "none";
    }

    public NotificationObject(int notificationID, String notificationDateTime, String description, int userID) {
        this.description =description;
        this.notificationID = notificationID;
        this.notificationDateTime = notificationDateTime;
        this.userID = userID;
    }

    public NotificationObject( String notificationDateTime, String description, int userID) {
        this.notificationDateTime = notificationDateTime;
        this.description = description;
        this.userID = userID;
    }
    public int getNotiID() {
        return notificationID;
    }
    public String getDescription() {
        return description;
    }
}
