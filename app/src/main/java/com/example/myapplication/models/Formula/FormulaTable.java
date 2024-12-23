package com.example.myapplication.models.Formula;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.myapplication.Constants.FormulaConstants;
import com.example.myapplication.Constants.SubjectConstants;
import com.example.myapplication.Constants.UserConstants;
import com.example.myapplication.database.Database;
import com.example.myapplication.models.QuestionAnswer.QuestionAnswerObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class FormulaTable {



    private SQLiteDatabase db;
    Context context;

    public FormulaTable (Context context){
        this.context = context;
        try {
            this.db = new Database(context).open();
        } catch (Exception e) {
            Toast.makeText(context,"Có lỗi khi kết nốt db tại model formula : "+ e , Toast.LENGTH_SHORT).show();
        }
    }

    public boolean addNewFormula(String formulaName, String formulaContent, int userID, Integer subjectID, String updatedDate) {

        String addFormulaStatement = "insert into Formula ( formulaName, formulaContent, userID ,subjectID, updatedDate) values (?,?,?,?)";
        try{
            Cursor add = this.db.rawQuery(addFormulaStatement ,new String[]{formulaName, formulaContent, Integer.toString(userID), subjectID.toString(), updatedDate});
            add.close();
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context,"Có lỗi khi thêm mới công thức : "+ e , Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean deleteFormulaByID(int formulaID){
        try {
            String queryFormula = "DELETE FROM Formula WHERE formulaID = ?";
            Cursor cur = this.db.rawQuery(queryFormula , new String[]{String.valueOf(formulaID)});
            cur.close();
            return true;
        } catch (Exception e) {
            Toast.makeText(this.context,"Có lỗi khi xóa formula : "+ e , Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public FormulaObject getFormulaById(int formulaID) {
        FormulaObject formula = null;

        String queryFormula = "SELECT * FROM Formula WHERE formulaID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(queryFormula, new String[]{String.valueOf(formulaID)});

            if (cursor != null && cursor.moveToFirst()) {
                int formulaIdIndex = cursor.getColumnIndex("formulaID");
                int formulaNameIndex = cursor.getColumnIndex("formulaName");
                int formulaContentIndex = cursor.getColumnIndex("formulaContent");
                int userIDIndex = cursor.getColumnIndex("userID");
                int subjectIDIndex = cursor.getColumnIndex("subjectID");
                int upDateIndex = cursor.getColumnIndex("formulaUpdatedDate");

                if (formulaIdIndex >= 0 && formulaNameIndex >= 0 && formulaContentIndex >= 0 && upDateIndex >= 0) {
                    Integer id = cursor.getInt(formulaIdIndex);
                    String name = cursor.getString(formulaNameIndex);
                    String formulaContent = cursor.getString(formulaContentIndex);
                    String createdDate = cursor.getString(upDateIndex);
                    Integer subjectID = cursor.getInt(subjectIDIndex);
                    Integer userID = cursor.getInt(userIDIndex);

                    formula = new FormulaObject(id, name, formulaContent, userID, subjectID, createdDate);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy thông tin công thức: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return formula;
    }


    public ArrayList<FormulaObject> getFormulaOfSubjectID(int subjectID){
        ArrayList<FormulaObject> listFormula = new ArrayList<>();
        String queryFormula = "SELECT formulaID FROM Formula WHERE subjectID = ?";
        Cursor cursor = null;

        try {
            cursor = this.db.rawQuery(queryFormula, new String[]{String.valueOf(subjectID)});
            if(cursor == null || !cursor.moveToFirst()){
                return listFormula;
            }
            while (cursor.moveToNext()){
                int formulaIDIndex = cursor.getColumnIndex("formulaID");
                if(formulaIDIndex >= 0){
                    int formulaID = cursor.getInt(formulaIDIndex);
                    listFormula.add(this.getFormulaById(formulaID));
                }
            }

        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi getFormulaOfSubjectID " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listFormula;
    }

    public ArrayList<FormulaObject> getFormulaBySubjectIdAndUserId(int subjectID, int userId) {
        ArrayList<FormulaObject> formulas = new ArrayList<>();
        String query = "SELECT formulaID FROM Formula WHERE subjectID = ? AND userId = ?";
        Cursor cursor = null;

        try {
            // Thực thi truy vấn với cả subjectID và userId
            cursor = this.db.rawQuery(query, new String[]{String.valueOf(subjectID), String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int formulaIDIndex = cursor.getColumnIndex("questionAnswerID");
                    if (formulaIDIndex >= 0) {
                        int formulaID = cursor.getInt(formulaIDIndex);
                        // Thêm kết quả vào danh sách
                        formulas.add(this.getFormulaById(formulaID));
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(this.context, "Có lỗi khi lấy công thức: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // Đóng con trỏ nếu không null
            if (cursor != null) {
                cursor.close();
            }
        }

        return formulas;
    }


}
