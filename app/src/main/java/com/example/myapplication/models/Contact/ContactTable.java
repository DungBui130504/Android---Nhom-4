package com.example.myapplication.models.Contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.myapplication.database.Database;

import java.util.ArrayList;

public class ContactTable {

    public SQLiteDatabase db;
    Context context;
    public ContactTable (Context context){
        this.context = context;
        try {
            this.db = new Database(context).open();
        } catch (Exception e) {
            Toast.makeText(context,"Có lỗi khi kết nốt db tại model Contact : "+ e , Toast.LENGTH_SHORT).show();
        }
    }

    // Thêm một contact mới vào bảng Contact
    public boolean addNewContact(String gmail, String name, boolean isTeacher, int userID) {
        // Kiểm tra xem gmail có hợp lệ không
        if (!gmail.contains("@gmail.com")) {
            Toast.makeText(this.context, "Gmail không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra xem contact đã tồn tại chưa
        if (checkContactExisted(gmail , userID)) {
            Toast.makeText(this.context, "Gmail này đã tồn tại trong danh sach cua ban", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra xem userID có hợp lệ không (userID phải tồn tại trong bảng User)
        if (!isUserIDValid(userID)) {
            Toast.makeText(this.context, "userID không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Thêm contact vào cơ sở dữ liệu
        String addContactStatement = "INSERT INTO Contact (gmail, name, isTeacher, userID) VALUES (?, ?, ?, ?)";
        try {
            this.db.execSQL(addContactStatement, new Object[]{gmail, name, isTeacher ? 1 : 0, userID});
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi thêm mới contact: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Kiểm tra xem contact đã tồn tại trong danh sach cua user chua
    public boolean checkContactExisted(String gmail , int userID) {
        String queryContact = "SELECT * FROM Contact WHERE gmail = ? and userID = ?";
        Cursor contactFound = this.db.rawQuery(queryContact, new String[]{gmail, String.valueOf(userID)});
        boolean exists = contactFound.moveToFirst();
        contactFound.close();
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

    // Xóa một contact theo ContactID
    public boolean deleteContact(int contactID) {
        try {
            String queryContact = "DELETE FROM Contact WHERE ContactID = ?";
            this.db.execSQL(queryContact, new String[]{String.valueOf(contactID)});
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi xóa contact: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Lấy thông tin contact theo ContactID
    public ContactObject getContactById(int contactID) {
        ContactObject contact = null;

        String queryContact = "SELECT * FROM Contact WHERE ContactID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(queryContact, new String[]{String.valueOf(contactID)});

            if (cursor != null && cursor.moveToFirst()) {
                int contactIdIndex = cursor.getColumnIndex("ContactID");
                int gmailIndex = cursor.getColumnIndex("gmail");
                int nameIndex = cursor.getColumnIndex("name");
                int isTeacherIndex = cursor.getColumnIndex("isTeacher");
                int userIDIndex = cursor.getColumnIndex("userID");

                if (contactIdIndex >= 0 && gmailIndex >= 0 && nameIndex >= 0 && isTeacherIndex >= 0 && userIDIndex >= 0) {
                    int contactId = cursor.getInt(contactIdIndex);
                    String gmail = cursor.getString(gmailIndex);
                    String name = cursor.getString(nameIndex);
                    boolean isTeacher = cursor.getInt(isTeacherIndex) > 0;
                    int userID = cursor.getInt(userIDIndex);

                    contact = new ContactObject(contactId, gmail, name, isTeacher, userID);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin liên hệ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contact;
    }

    public ArrayList<ContactObject> getContactOfUserID(int userID){
        ArrayList<ContactObject> listContact = new ArrayList<>();
        String queryContact = "SELECT contactID FROM Contact WHERE userID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(queryContact, new String[]{String.valueOf(userID)});
            if(cursor == null || !cursor.moveToFirst()){
                return listContact;
            }
            while (cursor.moveToNext()){
                int contactIDIndex = cursor.getColumnIndex("contactID");
                if(contactIDIndex >= 0){
                    int contactID = cursor.getInt(contactIDIndex);
                    listContact.add(this.getContactById(contactID));
                }
            }

        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin liên hệ của userID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listContact;
    }

}
