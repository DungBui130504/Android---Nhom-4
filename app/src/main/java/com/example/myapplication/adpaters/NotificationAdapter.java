package com.example.myapplication.adpaters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.models.Notification.NotificationObject;
import com.example.myapplication.models.QuestionAnswer.QuestionAnswerObject;
import com.example.myapplication.models.Subject.SubjectObject;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<NotificationObject> {
    private final Activity context;
    private final int ID;
    private final ArrayList<NotificationObject> myList;

    public NotificationAdapter(Activity context, ArrayList<NotificationObject> myList, int ID) {
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
        TextView notiItem = convertView.findViewById(R.id.notiItem);

        // Gán dữ liệu vào TextView
        notiItem.setText(myList.get(position).getDescription()); // Giả sử bạn có phương thức getSubjectName()


        return convertView;
    }
}
