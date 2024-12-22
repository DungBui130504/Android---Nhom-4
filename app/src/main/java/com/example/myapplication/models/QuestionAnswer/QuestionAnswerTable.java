package com.example.myapplication.models.QuestionAnswer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.myapplication.database.Database;

import java.util.ArrayList;

public class QuestionAnswerTable {
    private SQLiteDatabase db;
    private Context context;

    public QuestionAnswerTable(Context context) {
        this.context = context;
        try {
            this.db = new Database(context).open();
        } catch (Exception e) {
            Toast.makeText(context, "Có lỗi khi kết nối db tại model QuestionAnswer : " + e, Toast.LENGTH_SHORT).show();
        }
    }

    // Thêm một câu hỏi và câu trả lời mới vào bảng QuestionAnswer
    public boolean addNewQuestionAnswer(String questionContent, String answerContent, String answerUpdateDate,
                                        String questionUpdateDate, int subjectID,  int userID) {
        ContentValues values = new ContentValues();
        values.put("questionContent", questionContent);
        values.put("answerContent", answerContent);
        values.put("answerUpdateDate", answerUpdateDate);
        values.put("questionUpdateDate", questionUpdateDate);
        values.put("subjectID", subjectID);
        values.put("userID", userID);

        try {
            long result = this.db.insert("QuestionAnswer", null, values);
            return result != -1;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi thêm câu hỏi và câu trả lời mới: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Lấy câu hỏi và câu trả lời theo questionAnswerID
    public QuestionAnswerObject getQuestionAnswerById(int questionAnswerID) {
        QuestionAnswerObject questionAnswer = null;

        String query = "SELECT * FROM QuestionAnswer WHERE questionAnswerID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(query, new String[]{String.valueOf(questionAnswerID)});

            if (cursor != null && cursor.moveToFirst()) {
                int questionAnswerIdIndex = cursor.getColumnIndex("questionAnswerID");
                int questionContentIndex = cursor.getColumnIndex("questionContent");
                int answerContentIndex = cursor.getColumnIndex("answerContent");
                int answerUpdateDateIndex = cursor.getColumnIndex("answerUpdateDate");
                int questionUpdateDateIndex = cursor.getColumnIndex("questionUpdateDate");
                int subjectIDIndex = cursor.getColumnIndex("subjectID");
                int isAnswerIndex = cursor.getColumnIndex("isAnswer");
                int userIDIndex = cursor.getColumnIndex("userID");

                if (questionAnswerIdIndex >= 0 && questionContentIndex >= 0 && answerContentIndex >= 0) {
                    int id = cursor.getInt(questionAnswerIdIndex);
                    String questionContent = cursor.getString(questionContentIndex);
                    String answerContent = cursor.getString(answerContentIndex);
                    String answerUpdateDate = cursor.getString(answerUpdateDateIndex);
                    String questionUpdateDate = cursor.getString(questionUpdateDateIndex);
                    int subjectID = cursor.getInt(subjectIDIndex);
                    boolean isAnswer = cursor.getInt(isAnswerIndex) == 1;
                    int userID = cursor.getInt(userIDIndex);

                    questionAnswer = new QuestionAnswerObject(id, questionContent, answerContent, answerUpdateDate,
                            questionUpdateDate, subjectID, isAnswer, userID);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin câu hỏi và câu trả lời: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return questionAnswer;
    }

    // Lấy tất cả câu hỏi và câu trả lời của một subjectID
    public ArrayList<QuestionAnswerObject> getQuestionAnswersOfUserID(int subjectID, int userId) {
        ArrayList<QuestionAnswerObject> questionAnswers = new ArrayList<>();
        String query = "SELECT questionAnswerID FROM QuestionAnswer WHERE subjectID = ? AND userId = ?";
        Cursor cursor = null;

        try {
            // Thực thi truy vấn với cả subjectID và userId
            cursor = this.db.rawQuery(query, new String[]{String.valueOf(subjectID), String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int questionAnswerIDIndex = cursor.getColumnIndex("questionAnswerID");
                    if (questionAnswerIDIndex >= 0) {
                        int questionAnswerID = cursor.getInt(questionAnswerIDIndex);
                        // Thêm kết quả vào danh sách
                        questionAnswers.add(this.getQuestionAnswerById(questionAnswerID));
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy câu hỏi và câu trả lời: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // Đóng con trỏ nếu không null
            if (cursor != null) {
                cursor.close();
            }
        }

        return questionAnswers;
    }

    //Cập nhật nd câu hỏi
    public boolean updateQuestionContent(int questionAnswerID, String newQuestionContent) {
        ContentValues values = new ContentValues();
        values.put("questionContent", newQuestionContent);

        try {
            // Cập nhật câu hỏi dựa trên questionAnswerID
            int rowsAffected = this.db.update("QuestionAnswer", values,
                    "questionAnswerID = ?", new String[]{String.valueOf(questionAnswerID)});

            return rowsAffected > 0;  // Trả về true nếu cập nhật thành công (số dòng bị ảnh hưởng > 0)
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi cập nhật câu hỏi: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Cập nhật câu trả lời của một câu hỏi
    public boolean updateAnswer(int questionAnswerID, String answerContent, String answerUpdateDate, boolean isAnswer) {
        ContentValues values = new ContentValues();
        values.put("answerContent", answerContent);
        values.put("answerUpdateDate", answerUpdateDate);
        values.put("isAnswer", isAnswer ? 1 : 0);

        try {
            int result = this.db.update("QuestionAnswer", values, "questionAnswerID = ?", new String[]{String.valueOf(questionAnswerID)});
            return result > 0;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi cập nhật câu trả lời: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Xóa câu hỏi và câu trả lời theo questionAnswerID
    public boolean deleteQuestionAnswer(int questionAnswerID) {
        try {
            int result = this.db.delete("QuestionAnswer", "questionAnswerID = ?", new String[]{String.valueOf(questionAnswerID)});
            return result > 0;
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi xóa câu hỏi và câu trả lời: " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Lấy số lượng câu hỏi của một subjectID và userID dưới dạng chuỗi
    public String getCountOfQuestions(int subjectID, int userID) {
        String countStr = "0";  // Mặc định là "0" nếu không có câu hỏi
        String query = "SELECT COUNT(*) FROM QuestionAnswer WHERE subjectID = ? AND userID = ?";
        Cursor cursor = null;

        try {
            // Thực thi truy vấn để đếm số lượng câu hỏi
            cursor = this.db.rawQuery(query, new String[]{String.valueOf(subjectID), String.valueOf(userID)});
            if (cursor != null && cursor.moveToFirst()) {
                // Lấy kết quả đếm và chuyển thành chuỗi
                countStr = String.valueOf(cursor.getInt(0)); // Cột đầu tiên chứa giá trị đếm
            }
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy số lượng câu hỏi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // Đóng con trỏ nếu không null
            if (cursor != null) {
                cursor.close();
            }
        }

        return countStr;
    }

    // Lấy số lượng câu hỏi của một subjectID và userID với isAnswer = 1
    public int getCountOfQuestionsIsAnswer(int subjectID, int userID) {
        ArrayList<QuestionAnswerObject> questionAnswers = this.getQuestionAnswersOfUserID(subjectID,userID);
        int count = 0;
        for(QuestionAnswerObject q : questionAnswers){
            if(!q.answerContent.trim().isEmpty()){
                count++;
            }
        }
        return count;
    }


}
