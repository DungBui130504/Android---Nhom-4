package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class DashBoardActivity extends AppCompatActivity {
    LinearLayout subjectBtn, notificationBtn;
    ImageButton loginBackBtn;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash_board);

        subjectBtn = findViewById(R.id.subjectBtn);
        loginBackBtn = findViewById(R.id.loginBackBtn);
        notificationBtn = findViewById(R.id.notificationBtn);
    SharedPreferences mySharedPrefer = getSharedPreferences("mySharedPrefer", MODE_PRIVATE);

        Intent intent = getIntent();
        int userId = mySharedPrefer.getInt("userID" , -1);


        //Xem danh sách môn học
        subjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(DashBoardActivity.this, SubjectActivity.class);
                    i.putExtra("userId", userId);
                    startActivity(i);
                }
                catch (Exception e) {
                    Toast.makeText(DashBoardActivity.this, "Không thể chuyển hướng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent j = new Intent(DashBoardActivity.this, NotificationActivity.class);
                    j.putExtra("userId", userId);
                    startActivity(j);
                }
                catch (Exception e) {
                    Toast.makeText(DashBoardActivity.this, "Không thể chuyển hướng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Trở về đăng nhập
        loginBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences mySharedPrefer = getSharedPreferences("mySharedPrefer", MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPrefer.edit();

                    editor.clear();
                    editor.apply();

                    Intent Logini = new Intent(DashBoardActivity.this, LoginActivity.class);
                    startActivity(Logini);
                    finish();
                }
                catch (Exception e) {
                    Toast.makeText(DashBoardActivity.this, "Không thể chuyển hướng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}