package com.example.myapplication;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.activity.LoginActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        deleteDatabase("app_database.db");
        SharedPreferences mySharedPrefer = getSharedPreferences("mySharedPrefer", MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPrefer.edit();

//        editor.clear();
//        editor.apply();
        int userID = LoginActivity.userID;
//        Toast.makeText(MainActivity.this,String.valueOf(userID),Toast.LENGTH_SHORT).show();

        if(userID <= 0){
            userID = mySharedPrefer.getInt("userID" , -1);
        }
//        Toast.makeText(MainActivity.this,String.valueOf(userID),Toast.LENGTH_SHORT).show();

        if(userID <= 0){
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }else {

        }


    }
}