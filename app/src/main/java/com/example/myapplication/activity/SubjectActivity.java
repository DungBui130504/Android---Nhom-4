package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuWrapperICS;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.adpaters.SubjectAdapter;
import com.example.myapplication.models.Subject.SubjectObject;
import com.example.myapplication.models.Subject.SubjectTable;

import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity {
    ImageButton subjectBack, editBtn, delBtn;
    Button addSubjectBtn;
    TextView cancelBtn, submitBtn;
    EditText subjectName;
    ListView subjectList;
    LinearLayout addSubjectLayout, addSubjectBox, subjectItemLayout;
    SubjectAdapter subjectAdapter;
    ArrayList<SubjectObject> subjects;
    SubjectTable subjectTable;
    ArrayList<Integer> selectedList;
    ArrayList<Integer> checkboxes;
    int editSubject;
    int check;
    public static ArrayList<Integer> getCheckList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subject);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
//        int userId = intent.getIntExtra("userId", -2);
//        Log.d("Id of me:", userId + "");
        int userId = 1;

        //Anh xa
        subjectBack = findViewById(R.id.subjectBack);
        subjectList = findViewById(R.id.subjectList);
        addSubjectBtn = findViewById(R.id.addSubjectBtn);
        addSubjectLayout = findViewById(R.id.addSubjectLayout);
        addSubjectBox = findViewById(R.id.addSubjectBox);
        cancelBtn = findViewById(R.id.cancelBtn);
        submitBtn = findViewById(R.id.submitBtn);
        subjectName = findViewById(R.id.subjectName);
        subjectItemLayout = findViewById(R.id.subjectItemLayout);
        editBtn = findViewById(R.id.editBtn);
        delBtn = findViewById(R.id.delBtn);

        selectedList = new ArrayList<>();

        subjectTable = new SubjectTable(this);
        subjects = new ArrayList<>();
        subjects.addAll(subjectTable.getSubjectsOfUserID(userId));

        Log.d("subject:", subjects.toString());

        subjectAdapter = new SubjectAdapter(SubjectActivity.this, subjects, R.layout.subject_item);
        subjectList.setAdapter(subjectAdapter);

//        subjectTable.deleteAllSubjects();

//        Nhấn nút trở về DashBoard
        subjectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                }
                catch (Exception e) {
                    Toast.makeText(SubjectActivity.this, "Không thể chuyển hướng", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Nhấn nút thêm môn học
        addSubjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addSubjectLayout.setVisibility(View.VISIBLE);
                    addSubjectBox.setVisibility(View.VISIBLE);
                    check = 0;
                }
                catch (Exception e) {
                    Toast.makeText(SubjectActivity.this, "Không thể mở giao diện thêm môn học!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Tắt layout thêm/ sửa môn học
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addSubjectLayout.setVisibility(View.GONE);
                    addSubjectLayout.setClickable(false);
                    addSubjectBox.setVisibility(View.GONE);
                }
                catch (Exception e) {
                    Toast.makeText(SubjectActivity.this, "Không thể tắt giao diện này!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Xác nhận thêm/ sửa môn học mới
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (subjectName.getText().toString().isEmpty()) {
                        Toast.makeText(SubjectActivity.this, "Bạn phải nhập tên môn học!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (check == 0) {
                        subjectTable.addNewSubject(subjectName.getText().toString(), userId);
                        Log.d("onClick:", "add subject!");
                        Toast.makeText(SubjectActivity.this, "Thêm môn học thành công!", Toast.LENGTH_SHORT).show();
                    }
                    if (check == 1) {
                        if (selectedList.isEmpty()) {
                            Toast.makeText(SubjectActivity.this, "Bạn phải chọn môn học cần sửa trước!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        subjectTable.updateSubjectName(subjects.get(editSubject).getSubjectID(), subjectName.getText().toString(), userId);
                        Log.d("onClick:", "update subject!");
                        Toast.makeText(SubjectActivity.this, "Sửa môn học thành công!", Toast.LENGTH_SHORT).show();
                    }
                    subjects.clear();
                    subjects.addAll(subjectTable.getSubjectsOfUserID(userId));
                    Log.d("add", subjects.toString());
                    subjectAdapter.notifyDataSetChanged();
                    addSubjectLayout.setVisibility(View.GONE);
                    addSubjectLayout.setClickable(false);
                    subjectName.setText("");
                    addSubjectBox.setVisibility(View.GONE);
                }
                catch (Exception e) {
                    if (check == 0) {
                        Toast.makeText(SubjectActivity.this, "Thêm môn học lỗi!", Toast.LENGTH_SHORT).show();
                    }
                    if (check == 1) {
                        Toast.makeText(SubjectActivity.this, "Sửa môn học lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        LongClick chọn môn học cần làm việc
        subjectList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    // Tạo PopupMenu với điểm neo là view (item được nhấn)
                    PopupMenu popupMenu = new PopupMenu(SubjectActivity.this, view);
                    popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());

                    // Xử lý sự kiện chọn menu
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            try {
                                int itemId = menuItem.getItemId();
                                if (itemId == R.id.option_1) {
                                    Intent iQuestion = new Intent(SubjectActivity.this, QuestionActivity.class);
                                    iQuestion.putExtra("userId", userId);
                                    iQuestion.putExtra("subjectId", subjects.get(i).getSubjectID());
                                    startActivity(iQuestion);
                                    return true;
                                } else if (itemId == R.id.option_2) {
                                    Intent iQuestion2 = new Intent(SubjectActivity.this, MyFormulaActivity.class);
                                    startActivity(iQuestion2);
                                    return true;
                                } else {
                                    return false;
                                }
                            } catch (Exception e) {
                                Toast.makeText(SubjectActivity.this, "Lỗi khi xử lý menu!", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }
                    });

                    // Hiển thị menu
                    popupMenu.show();
                } catch (Exception e) {
                    Toast.makeText(SubjectActivity.this, "Không chọn được môn học!", Toast.LENGTH_SHORT).show();
                }
                return true; // Xử lý sự kiện long click xong
            }
        });


//        Chọn môn học cần sửa
        subjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    selectedList.add(i);
                    editSubject = i;
                    if (selectedList.size() > 1) {
                        for (int j = 0; j < subjectList.getCount(); j++) {
                            View reBackgroundView = subjectList.getChildAt(j);
                            if (reBackgroundView != null) {
                                reBackgroundView.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                    }
                    view.setBackgroundColor(Color.parseColor("#00BCD4"));
                    Log.d("selectedList:", selectedList.toString());
                }
                catch (Exception e) {
                    Toast.makeText(SubjectActivity.this, "Không chọn được môn cần sửa!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Mở giao diện sửa môn học
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addSubjectLayout.setVisibility(View.VISIBLE);
                    addSubjectBox.setVisibility(View.VISIBLE);
                    check = 1;
                }
                catch (Exception e) {
                    Toast.makeText(SubjectActivity.this, "Không mở được giao diện sửa!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Xóa môn học
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d("checkboxes:", getCheckList.toString());
                    for (int i = 0; i < getCheckList.size(); i++) {
                        //Day la truy van xoa
                        Log.d("name:", subjects.get(getCheckList.get(i)).getSubjectName());
                        subjectTable.deleteSubjectByName(subjects.get(getCheckList.get(i)).getSubjectName(), userId);
                    }
                    getCheckList.clear();
                    subjects.clear();
                    subjects.addAll(subjectTable.getSubjectsOfUserID(userId));
                    subjectAdapter.notifyDataSetChanged();
                    subjectName.setText("");
                }
                catch (Exception e) {
                    Toast.makeText(SubjectActivity.this, "Lỗi khi xóa môn học!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(SubjectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}