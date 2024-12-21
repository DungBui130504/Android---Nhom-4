package com.example.myapplication.adpaters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.activity.QuestionActivity;
import com.example.myapplication.models.QuestionAnswer.QuestionAnswerObject;
import com.example.myapplication.models.Subject.SubjectObject;

import java.util.ArrayList;
import java.util.Random;


public class QuestionAdapter extends ArrayAdapter {
    private final Activity context;
    private final int ID;
    private final ArrayList<QuestionAnswerObject> myList;

    public QuestionAdapter(Activity context, ArrayList<QuestionAnswerObject> myList, int ID) {
        super(context, ID, myList);
        this.context = context;
        this.myList = myList;
        this.ID = ID;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Kiểm tra nếu convertView == null để tạo mới
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(ID, parent, false);  // Inflate layout

        // Khởi tạo các view trong convertView
        TextView subjectItem = convertView.findViewById(R.id.questionItem);

        TextView isAnswer = convertView.findViewById(R.id.isAnswer);

        if (myList.get(position).getAnswerContent().trim().isEmpty()) {
            isAnswer.setTextColor(Color.parseColor("#F44336"));
            isAnswer.setText("Chưa trả lời");
        }
        else {

            isAnswer.setTextColor(Color.parseColor("#50C878"));
            isAnswer.setText("Đã trả lời");
        }

        LinearLayout decor = convertView.findViewById(R.id.decor);

        String[] colors = {"#0BD0F2", "#39BF67", "#F02222", "#5C2D98", "#9D9D9D"};

        Random random = new Random();
        int randomIndex = random.nextInt(colors.length);

        // Lấy phần tử màu ngẫu nhiên
        String randomColor = colors[randomIndex];

        // Đặt màu nền cho LinearLayout
        decor.setBackgroundColor(Color.parseColor(randomColor));
        CheckBox checkBox = convertView.findViewById(R.id.questionChk);



        // Gán dữ liệu vào TextView
        subjectItem.setText(myList.get(position).getQuestionContent()); // Giả sử bạn có phương thức getSubjectName()

        // Thiết lập trạng thái checkbox từ đối tượng SubjectObject
//        checkBox.setChecked(myList.get(position).isChecked());

        // Xử lý sự kiện khi checkbox thay đổi trạng thái
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                try {
                    // Cập nhật trạng thái checkbox vào đối tượng dữ liệu
                    myList.get(position).setChecked(isChecked);

                    // Cập nhật mảng checkedItems khi checkbox được chọn hoặc bỏ chọn
                    if (isChecked) {
                        // Nếu checkbox được check, thêm chỉ số vào mảng
                        QuestionActivity.getCheckList.add(position);
                    } else {
                        // Nếu checkbox uncheck, loại bỏ chỉ số khỏi mảng
                        QuestionActivity.getCheckList.remove(Integer.valueOf(position));
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return convertView;
    }
}
