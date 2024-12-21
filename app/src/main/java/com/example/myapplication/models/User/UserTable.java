package com.example.myapplication.models.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.database.Database;

public class UserTable {
    public SQLiteDatabase db;
    Context context;
    public UserTable (Context context){
        this.context = context;
        if (this.context != null) {
            try {
                this.db = new Database(context).open();
            } catch (Exception e) {
                Toast.makeText(context, "Có lỗi khi kết nối db tại model User : " + e, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Nếu context là null, có thể log hoặc xử lý lỗi ở đây
            Log.e("UserTable", "Context is null, cannot open database.");
        }
    }
    public UserObject addNewUser(String userName, String password,String gmail ,String fullName, String phoneNumber){
        if(!gmail.contains("@gmail.com")){
            Toast.makeText(this.context,"Gmail không hợp lệ! " , Toast.LENGTH_SHORT).show();
            return null ;
        }
        try {
            Integer number = Integer.valueOf(phoneNumber);
        } catch (NumberFormatException e) {
            Toast.makeText(this.context,"Số điện thoại không hợp lệ! " , Toast.LENGTH_SHORT).show();
            return null ;
        }
        if(checkUserExisted(userName)){
            Toast.makeText(this.context,"Username đã tồn tại! " , Toast.LENGTH_SHORT).show();
            return null ;
        }
        String addUserStatement = "insert into User (userName, password,gmail,fullName ,phoneNumber) values (?,?,?,?,?) ";
        try {
            this.db.execSQL(addUserStatement, new Object[]{userName, password, gmail, fullName, phoneNumber});
            return this.getUserByUserName(userName);
        } catch (Exception e) {
            Log.d("loi_dang_ky", "Có lỗi khi thêm mới user: " + e.toString());
            Toast.makeText(this.context, "Có lỗi khi thêm mới user: " + e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public boolean checkUserExisted(String userName){
        String queryUser = "select * from User where userName = ?";
        Cursor userFound = this.db.rawQuery(queryUser , new String[]{userName});
        boolean exists = userFound.moveToFirst();
        userFound.close();
        return exists;
    }

    public boolean deleteUser(int userID){
        try {
            String queryUser = "DELETE FROM User WHERE userID=" + userID;
            Cursor cur = this.db.rawQuery(queryUser , null);
            cur.close();
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context,"Có lỗi khi xóa user : "+ e , Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public UserObject getUserByUserID(int userID) {
        UserObject user = new UserObject();
        Cursor cur = null;
        try {
            String queryUser = "SELECT * FROM User WHERE userID = ?";
            cur = this.db.rawQuery(queryUser, new String[]{String.valueOf(userID)});

            if (cur != null && cur.moveToFirst()) {
                // Kiểm tra xem tên cột có hợp lệ không trước khi lấy giá trị
                int userIDIndex = cur.getColumnIndex("userID");
                int userNameIndex = cur.getColumnIndex("userName");
                int passwordIndex = cur.getColumnIndex("password");
                int gmailIndex = cur.getColumnIndex("gmail");
                int fullNameIndex = cur.getColumnIndex("fullName");
                int phoneNumberIndex = cur.getColumnIndex("phoneNumber");


                // Kiểm tra nếu chỉ số cột hợp lệ (>= 0)
                if (userNameIndex >= 0 && passwordIndex >= 0 && gmailIndex >= 0 && fullNameIndex >= 0 && phoneNumberIndex >= 0) {
                    int UserID = cur.getInt(userIDIndex);
                    String userName = cur.getString(userNameIndex);
                    String passWord = cur.getString(passwordIndex);
                    String gmail = cur.getString(gmailIndex);
                    String fullName = cur.getString(fullNameIndex);
                    String phoneNumber = cur.getString(phoneNumberIndex);

                    user = new UserObject(UserID,userName, passWord, gmail, fullName, phoneNumber);
                } else {
                    Toast.makeText(this.context, "Cột không tồn tại trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin user: " + e, Toast.LENGTH_SHORT).show();
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        return user;
    }


    public UserObject getUserByUserName(String user_name){
        UserObject user = new UserObject();
        Cursor cur = null;
        try {
            String queryUser = "SELECT * FROM User WHERE userName = ?";
            cur = this.db.rawQuery(queryUser, new String[]{user_name.trim()});

            if (cur != null && cur.moveToFirst()) {
                int userIDIndex = cur.getColumnIndex("userID");
                int userNameIndex = cur.getColumnIndex("userName");
                int passwordIndex = cur.getColumnIndex("password");
                int gmailIndex = cur.getColumnIndex("gmail");
                int fullNameIndex = cur.getColumnIndex("fullName");
                int phoneNumberIndex = cur.getColumnIndex("phoneNumber");


                // Kiểm tra nếu chỉ số cột hợp lệ (>= 0)
                if (userNameIndex >= 0 && passwordIndex >= 0 && gmailIndex >= 0 && fullNameIndex >= 0 && phoneNumberIndex >= 0) {
                    int UserID = cur.getInt(userIDIndex);
                    String userName = cur.getString(userNameIndex);
                    String passWord = cur.getString(passwordIndex);
                    String gmail = cur.getString(gmailIndex);
                    String fullName = cur.getString(fullNameIndex);
                    String phoneNumber = cur.getString(phoneNumberIndex);

                    user = new UserObject(UserID,userName, passWord, gmail, fullName, phoneNumber);
                } else {
                    Toast.makeText(this.context, "Cột không tồn tại trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin user: " + e, Toast.LENGTH_SHORT).show();
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        return user;
    }

    public int getUserIdByUserName(String userName) {
        int userID = -1; // Nếu không tìm thấy sẽ trả về -1
        Cursor cur = null;
        try {
            String queryUser = "SELECT userID FROM User WHERE userName = ?";
            cur = this.db.rawQuery(queryUser, new String[]{userName.trim()});

            if (cur != null && cur.moveToFirst()) {
                int userIDIndex = cur.getColumnIndex("userID");

                // Kiểm tra nếu chỉ số cột hợp lệ (>= 0)
                if (userIDIndex >= 0) {
                    userID = cur.getInt(userIDIndex);
                } else {
                    Toast.makeText(this.context, "Cột không tồn tại trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy userID: " + e, Toast.LENGTH_SHORT).show();
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        return userID;
    }


}
