package com.example.myapplication.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.MainActivity;

public class Database extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "app_database.db";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE User (" +
                "userID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userName TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "gmail TEXT NOT NULL, " +
                "fullName TEXT NOT NULL, " +
                "phoneNumber TEXT);");

        db.execSQL("CREATE TABLE Contact (" +
                "ContactID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "gmail TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "isTeacher BOOLEAN NOT NULL, " +
                "userID INTEGER, " +
                "FOREIGN KEY (userID) REFERENCES User(userID));");

        db.execSQL("CREATE TABLE Subject (" +
                "subjectID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subjectName TEXT NOT NULL," +
                "userID INTEGER, " +
                "FOREIGN KEY (userID) REFERENCES User(userID));");

        db.execSQL("CREATE TABLE Formula (" +
                "formulaID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "formulaContent TEXT NOT NULL, " +
                "formulaName TEXT NOT NULL, " +
                "formulaUpdatedDate TEXT, " +
                "subjectID INTEGER NOT NULL, " +
                "userID INTEGER NOT NULL, " +
                "FOREIGN KEY (subjectID) REFERENCES Subject(subjectID), " +
                "FOREIGN KEY (userID) REFERENCES User(userID));");

        db.execSQL("CREATE TABLE QuestionAnswer (" +
                "questionAnswerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "questionContent TEXT NOT NULL, " +
                "answerContent TEXT, " +
                "answerUpdateDate TEXT, " +
                "questionUpdateDate TEXT, " +
                "subjectID INTEGER NOT NULL, " +
                "isAnswer boolean default False, " +
                "userID INTEGER NOT NULL, " +
                "FOREIGN KEY (subjectID) REFERENCES Subject(subjectID), " +
                "FOREIGN KEY (userID) REFERENCES User(userID));");

        db.execSQL("CREATE TABLE Notification ("+
                "notificationID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "notificationDateTime TEXT NOT NULL, " +
                "description TEXT NOT NULL," +
                "userID INTEGER, " +
                "FOREIGN KEY (userID) REFERENCES User(userID));");

        //insert data
        db.execSQL("INSERT INTO User (userName, password, gmail,fullName, phoneNumber) VALUES ('test', 'test', 'test@gmail.com','Nguyễn Hữu Đức' ,'091288321')");
        db.execSQL("INSERT INTO User (userName, password, gmail,fullName, phoneNumber) VALUES ('Ark', '1235', 'Ark@gmail.com','Ark Zero' ,'000000000')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Contact");
        db.execSQL("DROP TABLE IF EXISTS Subject");
        db.execSQL("DROP TABLE IF EXISTS Formula");
        db.execSQL("DROP TABLE IF EXISTS QuestionAnswer");
        db.execSQL("DROP TABLE IF EXISTS Notification");
        onCreate(db);
    }
    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }
}