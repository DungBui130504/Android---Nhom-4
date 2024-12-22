package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.adpaters.QuestionAdapter;

import com.example.myapplication.models.QuestionAnswer.QuestionAnswerObject;
import com.example.myapplication.models.QuestionAnswer.QuestionAnswerTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;


public class QuestionActivity extends AppCompatActivity {
    ImageButton questionBack, addBtn;
    ListView questionList;
    TextView numOfQuestion, numOfAnswer, isAnswer;
    ArrayList<QuestionAnswerObject> questions;
    QuestionAdapter questionAdapter;
    QuestionAnswerTable questionAnswerTable;
    ImageButton deleteBtn ;
    ImageButton btnMail;
    ImageButton btnSms;
    public static ArrayList<Integer> getCheckList = new ArrayList<>();
    int save, check;

    Intent intent = getIntent();

    int userId;
    int subjectId;

    public interface SmsInputDialogCallback {
        void onSmsEntered(String phoneNumber, String message);
    }
    public interface InputDialogCallback {
        void onEmailEntered(String email);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Regex đơn giản để kiểm tra số điện thoại
        String phoneRegex = "^[+]?[0-9]{10,13}$";  // Số điện thoại có thể bắt đầu bằng dấu "+" và có từ 10 đến 13 chữ số

        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }


    public boolean isValidEmail(String email) {
        // Regex để kiểm tra email hợp lệ
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }


    public void showInputDialog(final InputDialogCallback callback) {
        final EditText inputField = new EditText(this);

        // Tạo AlertDialog để nhận email
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập địa chỉ email")
                .setMessage("Vui lòng nhập email muốn gửi:")
                .setView(inputField)
                .setCancelable(false)
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String emailInput = inputField.getText().toString();
                        // Gọi callback để trả về email nhập vào sau khi nhấn "Xác nhận"
                        callback.onEmailEntered(emailInput);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        // Hiển thị dialog
        builder.create().show();
    }


    public void showSmsInputDialog(final SmsInputDialogCallback callback) {
        final EditText phoneField = new EditText(this);
        final EditText messageField = new EditText(this);

        // Tạo AlertDialog để nhận số điện thoại và nội dung SMS
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập số điện thoại và nội dung SMS")
                .setMessage("Vui lòng nhập số điện thoại và nội dung tin nhắn:")
                .setView(phoneField)
                .setView(messageField)
                .setCancelable(false)
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phoneNumber = phoneField.getText().toString().trim();
                        String message = messageField.getText().toString().trim();
                        Toast.makeText(QuestionActivity.this,message,Toast.LENGTH_SHORT).show();

                        // Gọi callback để trả về số điện thoại và nội dung SMS
                        callback.onSmsEntered(phoneNumber, message);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        // Hiển thị dialog
        builder.create().show();
    }


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent2 = getIntent();

        userId = intent2.getIntExtra("userId", -2);
        subjectId = intent2.getIntExtra("subjectId", -2);

        Log.d("myUserId:", userId + "");
        Log.d("mySubjectId:", subjectId + "");

        questionBack = findViewById(R.id.questionBack);
        addBtn = findViewById(R.id.addBtn);
        questionList = findViewById(R.id.questionList);
        numOfQuestion = findViewById(R.id.numOfQuestion);
        numOfAnswer = findViewById(R.id.numOfAnswer);
        isAnswer = findViewById(R.id.isAnswer);
        btnMail = findViewById(R.id.btnMail);
        btnSms = findViewById(R.id.btnSms);

        questions = new ArrayList<>();

        questionAnswerTable = new QuestionAnswerTable(this);

        questions.addAll(questionAnswerTable.getQuestionAnswersOfUserID(subjectId, userId));

        Log.d("questions:", questions.toString());

        questionAdapter = new QuestionAdapter(QuestionActivity.this, questions, R.layout.question_item);

        questionList.setAdapter(questionAdapter);

        questionAdapter.notifyDataSetChanged();

        numOfQuestion.setText(questionAnswerTable.getCountOfQuestions(subjectId, userId));

        numOfAnswer.setText(String.valueOf(questionAnswerTable.getCountOfQuestionsIsAnswer(subjectId, userId)));

        deleteBtn = findViewById(R.id.trashBtn);


        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị dialog để người dùng nhập số điện thoại và nội dung SMS
                showSmsInputDialog(new SmsInputDialogCallback() {
                    String smsContent = "";
                    @Override
                    public void onSmsEntered(String message, String phoneNumber) {
                        // Kiểm tra tính hợp lệ của số điện thoại
                        if (!isValidPhoneNumber(phoneNumber)) {
                            Toast.makeText(QuestionActivity.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (getCheckList.isEmpty()) {
                            Toast.makeText(QuestionActivity.this, "Bạn phải chọn câu hỏi để gửi SMS!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = QuestionActivity.getCheckList.size() - 1; i >= 0; i--) {
                            Integer e = QuestionActivity.getCheckList.get(i);
                            if (e >= 0 && e < questions.size()) {
                                QuestionAnswerObject q = questions.get(e);
                                smsContent += q.toString();
                            }
                        }
//                         Tạo Intent để gửi SMS
                        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                        smsIntent.setData(Uri.parse("smsto:" + phoneNumber));  // Đặt số điện thoại
                        smsIntent.putExtra("sms_body", smsContent);  // Đặt nội dung SMS

                        try {
                            startActivity(smsIntent);  // Mở ứng dụng SMS để người dùng gửi tin nhắn
                        } catch (android.content.ActivityNotFoundException ex) {
                            // Xử lý khi không có ứng dụng SMS nào được cài đặt
                            Toast.makeText(QuestionActivity.this, "Không có ứng dụng SMS nào được cài đặt.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showInputDialog(new InputDialogCallback() {
                    @Override
                    public void onEmailEntered(String email) {
                        String emailContent = "";
                        email = email.trim();  // Loại bỏ khoảng trắng ở đầu và cuối

                        // Kiểm tra tính hợp lệ của email
                        if (!isValidEmail(email)) {
                            Toast.makeText(QuestionActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                            return;
                        }



                        if (getCheckList.isEmpty()) {
                            Toast.makeText(QuestionActivity.this, "Bạn phải chọn câu hỏi để gửi mail!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = QuestionActivity.getCheckList.size() - 1; i >= 0; i--) {
                            Integer e = QuestionActivity.getCheckList.get(i);
                            if (e >= 0 && e < questions.size()) {
                                QuestionAnswerObject q = questions.get(e);
                                emailContent += q.toString();
                            }
                        }
                        getCheckList.clear();

                        // Tạo Intent để gửi email
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("message/rfc822");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});  // Sử dụng email người dùng nhập vào
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Gửi nội dung câu hỏi");  // Tiêu đề email
                        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);    // Nội dung email

                        try {
                            // Mở ứng dụng email cho người dùng chọn
                            startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng Email:"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            // Xử lý trường hợp không có ứng dụng email nào được cài đặt
                            Toast.makeText(QuestionActivity.this, "Không có ứng dụng email nào được cài đặt.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });


        questionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                } catch (Exception e) {
                    Toast.makeText(QuestionActivity.this, "Không trở về được!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(QuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 0;
                Intent i = new Intent(QuestionActivity.this, AddQuestionActivity.class);
                i.putExtra("userId", userId);
                i.putExtra("subjectId", subjectId);
                i.putExtra("Check:" , check);
                startActivityForResult(i, 100); // Sử dụng requestCode 100
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCheckList.isEmpty()) {
                    Toast.makeText(QuestionActivity.this, "Bạn phải chọn câu hỏi để xóa!", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = QuestionActivity.getCheckList.size() - 1; i >= 0; i--) {
                    Integer e = QuestionActivity.getCheckList.get(i);
                    if (e >= 0 && e < questions.size()) {
                        Integer questionAnswerID = questions.get(e).questionAnswerID;
                        questions.remove(questions.get(e));
                        questionAdapter.notifyDataSetChanged();
                        questionAnswerTable.deleteQuestionAnswer(questionAnswerID);
                        numOfQuestion.setText(questionAnswerTable.getCountOfQuestions(subjectId, userId));
                        numOfAnswer.setText(String.valueOf(questionAnswerTable.getCountOfQuestionsIsAnswer(subjectId, userId)));
                    }
                }
                getCheckList.clear();
                Toast.makeText(QuestionActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    getCheckList.clear();
                    check = 1;
                    save = i;
                    Intent i2 = new Intent(QuestionActivity.this, AddQuestionActivity.class);
                    i2.putExtra("fromClick", true);
                    i2.putExtra("questionContext", questions.get(i).getQuestionContent());
                    i2.putExtra("answerContext", questions.get(i).getAnswerContent());
//                    startActivity(i2);
                    startActivityForResult(i2, 101);
                }
                catch (Exception e) {
                    Toast.makeText(QuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            try {
                getCheckList.clear();
                // Lấy dữ liệu từ Intent trả về
                String questionTxt = data.getStringExtra("questionTxt");
                String answserTxt = data.getStringExtra("answserTxt");
                String answerDate = data.getStringExtra("answerDate");
                String questionDate = data.getStringExtra("questionDate");

                // Thêm câu hỏi mới vào database
                questionAnswerTable.addNewQuestionAnswer(
                        questionTxt, answserTxt, answerDate, questionDate, subjectId, userId
                );

                // Cập nhật danh sách câu hỏi
                questions.clear();
                questions.addAll(questionAnswerTable.getQuestionAnswersOfUserID(subjectId, userId));

                numOfQuestion.setText(questionAnswerTable.getCountOfQuestions(subjectId, userId));

                numOfAnswer.setText(String.valueOf(questionAnswerTable.getCountOfQuestionsIsAnswer(subjectId, userId)));

                questionAdapter.notifyDataSetChanged();

                Toast.makeText(QuestionActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(QuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 101 && resultCode == RESULT_OK) {
            getCheckList.clear();
            try {
                // Lấy dữ liệu từ Intent trả về
                String questionTxt = data.getStringExtra("questionTxt");
                String answserTxt = data.getStringExtra("answserTxt");
                questionAnswerTable.updateQuestionContent(questions.get(save).getQuestionAnswerID(), questionTxt);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String answerDate = sdf.format(calendar.getTime());

                boolean isAnswer = true;
                if (answserTxt.trim().isEmpty()) {
                    isAnswer = false;
                }

                questionAnswerTable.updateAnswer(questions.get(save).getQuestionAnswerID(), answserTxt, answerDate, isAnswer);

                questions.clear();
                questions.addAll(questionAnswerTable.getQuestionAnswersOfUserID(subjectId, userId));

                numOfQuestion.setText(questionAnswerTable.getCountOfQuestions(subjectId, userId));

                numOfAnswer.setText(String.valueOf(questionAnswerTable.getCountOfQuestionsIsAnswer(subjectId, userId)));

                questionAdapter.notifyDataSetChanged();

                Toast.makeText(QuestionActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(QuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}