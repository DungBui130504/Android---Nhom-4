package com.example.myapplication.adpaters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.activity.SubjectActivity;
import com.example.myapplication.models.Subject.SubjectObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubjectAdapter extends ArrayAdapter<SubjectObject> {
    private final Activity context;
    private final int ID;
    private final ArrayList<SubjectObject> myList;

    public SubjectAdapter(Activity context, ArrayList<SubjectObject> myList, int ID) {
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
            TextView subjectItem = convertView.findViewById(R.id.subjectItem);
            CheckBox checkBox = convertView.findViewById(R.id.subjectChk);

            // Gán dữ liệu vào TextView
            subjectItem.setText(myList.get(position).getSubjectName()); // Giả sử bạn có phương thức getSubjectName()

            // Thiết lập trạng thái checkbox từ đối tượng SubjectObject
            checkBox.setChecked(myList.get(position).isChecked()); // Sử dụng trạng thái lưu trong đối tượng

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
                            SubjectActivity.getCheckList.add(position);
                        } else {
                            // Nếu checkbox uncheck, loại bỏ chỉ số khỏi mảng
                            SubjectActivity.getCheckList.remove(Integer.valueOf(position));
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        return convertView;
    }

}



