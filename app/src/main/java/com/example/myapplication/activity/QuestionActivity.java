package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class QuestionActivity extends AppCompatActivity {
    ImageButton questionBack, addBtn;
    ListView questionList;
    TextView numOfQuestion, numOfAnswer, isAnswer;
    ArrayList<QuestionAnswerObject> questions;
    QuestionAdapter questionAdapter;
    QuestionAnswerTable questionAnswerTable;
    public static ArrayList<Integer> getCheckList = new ArrayList<>();
    int save, check;

    Intent intent = getIntent();

    int userId;
    int subjectId;

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

        questions = new ArrayList<>();

        questionAnswerTable = new QuestionAnswerTable(this);

        questions.addAll(questionAnswerTable.getQuestionAnswersOfUserID(subjectId, userId));

        Log.d("questions:", questions.toString());

        questionAdapter = new QuestionAdapter(QuestionActivity.this, questions, R.layout.question_item);

        questionList.setAdapter(questionAdapter);

        questionAdapter.notifyDataSetChanged();

        numOfQuestion.setText(questionAnswerTable.getCountOfQuestions(subjectId, userId));

        numOfAnswer.setText(questionAnswerTable.getCountOfQuestionsIsAnswer(subjectId, userId));

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

        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
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

                numOfAnswer.setText(questionAnswerTable.getCountOfQuestionsIsAnswer(subjectId, userId));

                questionAdapter.notifyDataSetChanged();

                Toast.makeText(QuestionActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(QuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                // Lấy dữ liệu từ Intent trả về
                String questionTxt = data.getStringExtra("questionTxt");
                String answserTxt = data.getStringExtra("answserTxt");
                questionAnswerTable.updateQuestionContent(questions.get(save).getQuestionAnswerID(), questionTxt);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String answerDate = sdf.format(calendar.getTime());

                boolean isAnswer = true;
                if (answserTxt.isEmpty()) {
                    isAnswer = false;
                }

                questionAnswerTable.updateAnswer(questions.get(save).getQuestionAnswerID(), answserTxt, answerDate, isAnswer);

                questions.clear();
                questions.addAll(questionAnswerTable.getQuestionAnswersOfUserID(subjectId, userId));

                numOfQuestion.setText(questionAnswerTable.getCountOfQuestions(subjectId, userId));

                numOfAnswer.setText(questionAnswerTable.getCountOfQuestionsIsAnswer(subjectId, userId));

                questionAdapter.notifyDataSetChanged();

                Toast.makeText(QuestionActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(QuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}