package com.example.myapplication.models.Subject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.myapplication.database.Database;

import java.util.ArrayList;

public class SubjectTable {
    public SQLiteDatabase db;
    Context context;

    // Constructor to initialize database connection
    public SubjectTable(Context context) {
        this.context = context;
        try {
            this.db = new Database(context).open();
        } catch (Exception e) {
            Toast.makeText(context, "Có lỗi khi kết nối db tại model Subject : " + e, Toast.LENGTH_SHORT).show();
        }
    }

    // Thêm một subject mới vào bảng Subject
    public boolean addNewSubject(String subjectName, int userID) {

        // Kiểm tra xem subjectName có hợp lệ không (subjectName không thể rỗng)
        if (subjectName == null || subjectName.trim().isEmpty()) {
            Toast.makeText(this.context, "Tên môn học không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (checkSubjectExisted(subjectName,userID)) {
            Toast.makeText(this.context, "Môn học này đã tồn tại trong danh sách môn học của bạn!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra xem userID có hợp lệ không (userID phải tồn tại trong bảng User)
        if (!isUserIDValid(userID)) {
            Toast.makeText(this.context, "userID: " + userID, Toast.LENGTH_SHORT).show();
            Toast.makeText(this.context, "userID không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Thêm subject vào cơ sở dữ liệu
        String addSubjectStatement = "INSERT INTO Subject (subjectName, userID) VALUES (?, ?)";
        try {
            this.db.execSQL(addSubjectStatement, new Object[]{subjectName, userID});
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi thêm mới môn học: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Kiểm tra xem subject đã tồn tại trong hệ thống chưa (dựa trên subjectName và userID)
    public boolean checkSubjectExisted(String subjectName, int userID) {
        String querySubject = "SELECT * FROM Subject WHERE subjectName = ? AND userID = ?";
        Cursor subjectFound = this.db.rawQuery(querySubject, new String[]{subjectName, String.valueOf(userID)});
        boolean exists = subjectFound.moveToFirst();
        subjectFound.close();
        return exists;
    }

    // Kiểm tra tính hợp lệ của userID (kiểm tra xem userID có tồn tại trong bảng User không)
    public boolean isUserIDValid(int userID) {
        String queryUser = "SELECT * FROM User WHERE userID = ?";
        Cursor userFound = this.db.rawQuery(queryUser, new String[]{String.valueOf(userID)});
        boolean exists = userFound.moveToFirst();
        userFound.close();
        return exists;
    }

    // Xóa một subject theo subjectID
    public boolean deleteSubject(int subjectID) {
        try {
            String querySubject = "DELETE FROM Subject WHERE subjectID = ?";
            this.db.execSQL(querySubject, new String[]{String.valueOf(subjectID)});
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi xóa môn học: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Xóa một subject theo tên môn học
    public boolean deleteSubjectByName(String subjectName, int userId) {
        try {
            // Câu lệnh SQL xóa môn học theo tên và userId
            String querySubject = "DELETE FROM Subject WHERE subjectName = ? AND userId = ?";
            this.db.execSQL(querySubject, new Object[]{subjectName, userId}); // Dùng Object[] thay vì String[]
            return true;
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            Toast.makeText(this.context, "Có lỗi khi xóa môn học: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    // Lấy thông tin subject theo subjectID
    public SubjectObject getSubjectById(int subjectID) {
        SubjectObject subject = null;

        String querySubject = "SELECT * FROM Subject WHERE subjectID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(querySubject, new String[]{String.valueOf(subjectID)});

            if (cursor != null && cursor.moveToFirst()) {
                int subjectIdIndex = cursor.getColumnIndex("subjectID");
                int subjectNameIndex = cursor.getColumnIndex("subjectName");
                int userIDIndex = cursor.getColumnIndex("userID");

                if (subjectIdIndex >= 0 && subjectNameIndex >= 0 && userIDIndex >= 0) {
                    int subjectId = cursor.getInt(subjectIdIndex);
                    String subjectName = cursor.getString(subjectNameIndex);
                    int userID = cursor.getInt(userIDIndex);

                    subject = new SubjectObject(subjectId, subjectName, userID);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin môn học: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return subject;
    }

    // Lấy tất cả các subject của một userID
    public ArrayList<SubjectObject> getSubjectsOfUserID(int userID) {
        ArrayList<SubjectObject> listSubject = new ArrayList<>();
        String querySubject = "SELECT * FROM Subject WHERE userID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(querySubject, new String[]{String.valueOf(userID)});
            if (cursor == null || !cursor.moveToFirst()) {
                return listSubject;
            }
            int subjectIDIndex0 = cursor.getColumnIndex("subjectID");
            if (subjectIDIndex0 >= 0) {
                int subjectID = cursor.getInt(subjectIDIndex0);
                listSubject.add(this.getSubjectById(subjectID));
            }
            while (cursor.moveToNext()) {
                int subjectIDIndex = cursor.getColumnIndex("subjectID");
                if (subjectIDIndex >= 0) {
                    int subjectID = cursor.getInt(subjectIDIndex);
                    listSubject.add(this.getSubjectById(subjectID));
                }
            }

        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin môn học của userID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listSubject;
    }

    // Sửa tên môn học theo subjectID
    public boolean updateSubjectName(int subjectID, String newSubjectName, int userId) {
        // Kiểm tra xem tên môn học mới có hợp lệ không (tên môn học không thể rỗng)
        if (newSubjectName == null || newSubjectName.trim().isEmpty()) {
            Toast.makeText(this.context, "Tên môn học không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Cập nhật tên môn học trong bảng Subject
        String updateSubjectStatement = "UPDATE Subject SET subjectName = ? WHERE subjectID = ?";
        try {
            this.db.execSQL(updateSubjectStatement, new Object[]{newSubjectName, subjectID});
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi cập nhật tên môn học: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Xóa hết dữ liệu trong bảng Subject
    public boolean deleteAllSubjects() {
        try {
            // Câu lệnh SQL để xóa tất cả các dòng trong bảng Subject
            String querySubject = "DELETE FROM Subject";
            this.db.execSQL(querySubject);
            return true;
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            Toast.makeText(this.context, "Có lỗi khi xóa tất cả môn học: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
