package com.example.myapplication.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.adpaters.NotificationAdapter;
import com.example.myapplication.models.Notification.NotificationObject;
import com.example.myapplication.models.Notification.NotificationTable;

import java.util.ArrayList;
import java.util.Locale;

public class NotificationActivity extends AppCompatActivity {
    ImageButton returnDashBoard, addNoti, delBtn;
    ListView notiList;
    LinearLayout addNotiBox, addNotiLayout;
    TimePicker time;
    TextView confBtn, backBtn;
    int check;
    EditText notiName;
    NotificationTable notificationTable;
    ArrayList<Integer> selectedList;
    ArrayList<NotificationObject> notiObject;
    NotificationAdapter notificationAdapter;
    public static ArrayList<Integer> getCheckList = new ArrayList<>();
    private static final String CHANNEL_ID = "time_notification_channel";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        Intent intent = getIntent();
        int userId = 1;

        returnDashBoard = findViewById(R.id.returnDashBoard);
        addNoti = findViewById(R.id.addNoti);
        delBtn = findViewById(R.id.delBtn);
        notiList = findViewById(R.id.notiList);
        addNotiBox = findViewById(R.id.addNotiBox);
        addNotiLayout = findViewById(R.id.addNotiLayout);
        time = findViewById(R.id.time);
        confBtn = findViewById(R.id.confBtn);
        backBtn = findViewById(R.id.backBtn);
        notiName = findViewById(R.id.notiName);
        notificationTable = new NotificationTable(this);
        selectedList = new ArrayList<>();

        notiObject = new ArrayList<>();
        notiObject.addAll(notificationTable.getNotificationsOfUserID(userId));

        notificationAdapter = new NotificationAdapter(NotificationActivity.this, notiObject, R.layout.notification_item);

        notiList.setAdapter(notificationAdapter);

        notificationAdapter.notifyDataSetChanged();

        int hours = time.getHour();
        int minutes = time.getMinute();
        String notificationDateTime = String.format(Locale.getDefault(), "%02d : %02d", hours, minutes);
        returnDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                } catch (Exception e) {
                    Toast.makeText(NotificationActivity.this, "Không thể chuyển hướng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addNotiLayout.setVisibility(View.VISIBLE);
                    addNotiBox.setVisibility(View.VISIBLE);
                    check = 0;
                } catch (Exception e) {
                    Toast.makeText(NotificationActivity.this, "Không thể mở giao diện thêm thông báo!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addNotiLayout.setVisibility(View.GONE);
                    addNotiLayout.setClickable(false);
                    addNotiBox.setVisibility(View.GONE);
                } catch (Exception e) {
                    Toast.makeText(NotificationActivity.this, "Không thể tắt giao diện này!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        confBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (notiName.getText().toString().isEmpty()) {
                        Toast.makeText(NotificationActivity.this, "Bạn phải nhập nội dung thông báo!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (check == 0) {
                        notificationTable.addNewNotification(notificationDateTime, notiName.getText().toString(), userId);
                        Toast.makeText(NotificationActivity.this, "Thêm thông báo thành công!", Toast.LENGTH_SHORT).show();
                    }
                    if (check == 1) {
                        if (selectedList.isEmpty()) {
                            Toast.makeText(NotificationActivity.this, "Bạn phải chọn thông báo cần sửa trước!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                    notiObject.clear();
                    notiObject.addAll(notificationTable.getNotificationsOfUserID(userId));
                    Log.d("add", notiObject.toString());
                    notificationAdapter.notifyDataSetChanged();
                    addNotiLayout.setVisibility(View.GONE);
                    addNotiLayout.setClickable(false);
                    addNotiBox.setVisibility(View.GONE);
                } catch (Exception e) {
                    if (check == 0) {
                        Toast.makeText(NotificationActivity.this, "Thêm thông báo lỗi!", Toast.LENGTH_SHORT).show();
                        Log.d("Nofi:", e.getMessage());
                    }
                }
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d("checkboxes:", getCheckList.toString());
                    for (int i = 0; i < getCheckList.size(); i++) {
                        //Day la truy van xoa
                        Log.d("id:", String.valueOf(notiObject.get(i).getNotiID()));
                        notificationTable.deleteNotificationById(notiObject.get(getCheckList.get(i)).getNotiID(), userId);
                    }
                    getCheckList.clear();
                    notiObject.clear();
                    notiObject.addAll(notificationTable.getNotificationsOfUserID(userId));
                    notificationAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(NotificationActivity.this, "Lỗi khi xóa thông báo!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(NotificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



