package com.example.myapplication.models.Notification;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.myapplication.database.Database;

import java.util.ArrayList;

public class NotificationTable {
    private SQLiteDatabase db;
    Context context;

    public NotificationTable(Context context) {
        this.context = context;
        try {
            this.db = new Database(context).open();
        } catch (Exception e) {
            Toast.makeText(context, "Có lỗi khi kết nối db tại model Notification : " + e, Toast.LENGTH_SHORT).show();
        }
    }

    // Thêm một notification mới vào bảng Notification
    public boolean addNewNotification(String notificationDateTime, String description, int userID) {

        String addNotificationStatement = "INSERT INTO Notification ( notificationDateTime, description, userID) VALUES ( ?, ?, ?)";
        try {
            this.db.execSQL(addNotificationStatement, new Object[]{notificationDateTime, description, userID});
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi thêm mới thông báo: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Kiểm tra xem thông báo đã tồn tại trong cơ sở dữ liệu chưa (dựa trên notificationID)
    public boolean isNotificationExistedByID(int notificationID) {
        String queryNotification = "SELECT * FROM Notification WHERE notificationID = ?";
        Cursor notificationFound = this.db.rawQuery(queryNotification, new String[]{String.valueOf(notificationID)});
        boolean exists = notificationFound.moveToFirst();
        notificationFound.close();
        return exists;
    }

    // Lấy thông báo theo notificationID
    public NotificationObject getNotificationById(int notificationID) {
        NotificationObject notification = null;

        String queryNotification = "SELECT * FROM Notification WHERE notificationID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(queryNotification, new String[]{String.valueOf(notificationID)});

            if (cursor != null && cursor.moveToFirst()) {
                int notificationIdIndex = cursor.getColumnIndex("notificationID");
                int notificationDateTimeIndex = cursor.getColumnIndex("notificationDateTime");
                int descriptionIndex = cursor.getColumnIndex("description");
                int userIdIndex = cursor.getColumnIndex("userID");

                if (notificationIdIndex >= 0 && notificationDateTimeIndex >= 0 && descriptionIndex >= 0) {
                    int id = cursor.getInt(notificationIdIndex);
                    String dateTime = cursor.getString(notificationDateTimeIndex);
                    String description = cursor.getString(descriptionIndex);
                    int userID = cursor.getInt(userIdIndex);

                    notification = new NotificationObject(id, dateTime, description, userID);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return notification;
    }

    // Lấy tất cả thông báo của một userID
    public ArrayList<NotificationObject> getNotificationsOfUserID(int userID) {
        ArrayList<NotificationObject> listNotifications = new ArrayList<>();
        String queryNotification = "SELECT notificationID FROM Notification WHERE userID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(queryNotification, new String[]{String.valueOf(userID)});
            if (cursor == null || !cursor.moveToFirst()) {
                return listNotifications;
            }

            while (cursor.moveToNext()) {
                int notificationIDIndex = cursor.getColumnIndex("notificationID");
                if (notificationIDIndex >= 0) {
                    int notificationID = cursor.getInt(notificationIDIndex);
                    listNotifications.add(this.getNotificationById(notificationID));
                }
            }

        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin thông báo của userID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listNotifications;
    }

    // Xóa thông báo theo notificationID
    public boolean deleteNotificationById(int notificationID, int userID) {
        try {
            String deleteNotificationStatement = "DELETE FROM Notification WHERE notificationID = ? AND userId = ?";
            this.db.execSQL(deleteNotificationStatement, new Object[]{notificationID, userID}); // Dùng Object[] thay vì String[]
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi xóa thông báo: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean updateDescription(int notificationID, String newDescription, int userId) {
        // Kiểm tra xem tên môn học mới có hợp lệ không (tên môn học không thể rỗng)
        if (newDescription == null || newDescription.trim().isEmpty()) {
            Toast.makeText(this.context, "Tên thông báo không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Cập nhật tên môn học trong bảng Subject
        String updateNotificationStatement = "UPDATE Notification SET description = ? WHERE notificationID = ?";
        try {
            this.db.execSQL(updateNotificationStatement, new Object[]{newDescription, notificationID});
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi cập nhật thông báo: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
