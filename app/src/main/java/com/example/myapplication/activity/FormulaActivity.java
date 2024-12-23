package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.adpaters.FormulaAdapter;
import com.example.myapplication.models.Formula.FormulaObject;
import com.example.myapplication.models.Formula.FormulaTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FormulaActivity extends AppCompatActivity {
    ImageButton btnBackFormula, btnDelItem;
    Button btnAddFormula;
    ListView lstFormula;
    FormulaTable formulaTable;
    FormulaAdapter formulaAdapter;
    ArrayList<FormulaObject> formulas;


    int save;

    int userId;
    int subjectId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formula);
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

        btnAddFormula = findViewById(R.id.btnAddFormula);
        lstFormula= findViewById(R.id.lstFormula);
        formulas = new ArrayList<>();
        formulaTable = new FormulaTable(this);
        btnBackFormula = findViewById(R.id.backBtn);
        formulas.addAll(formulaTable.getFormulaBySubjectIdAndUserId(subjectId, userId));
        Log.d("formulas:", formulas.toString());
        formulaAdapter = new FormulaAdapter(FormulaActivity.this, formulas, R.layout.formula_item);

        lstFormula.setAdapter(formulaAdapter);

        formulaAdapter.notifyDataSetChanged();


        btnBackFormula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                } catch (Exception e) {
                    Toast.makeText(FormulaActivity.this, "Không trở về được!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(FormulaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddFormula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FormulaActivity.this, AddFormulaActivity.class);
                i.putExtra("userId", userId);
                i.putExtra("subjectId", subjectId);
                startActivityForResult(i, 104); // Sử dụng requestCode 100
            }
        });

        lstFormula.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    save = i;
                    Intent i2 = new Intent(FormulaActivity.this, AddFormulaActivity.class);
                    i2.putExtra("fromClick", true);
                    i2.putExtra("formulaNameContext", formulas.get(i).getFormulaName());
                    i2.putExtra("formulaContentContext", formulas.get(i).getFormulaContent());
//                    startActivity(i2);
                    startActivityForResult(i2, 103);
                }
                catch (Exception e) {
                    Toast.makeText(FormulaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 104 && resultCode == RESULT_OK) {
            try {
                String formulaName = data.getStringExtra("formulaNameTxt");
                String formulaContent = data.getStringExtra("formulaContentTxt");
                String formulaCreatedDate = data.getStringExtra("formulaCreatedDate");

                // Thêm cong thuc mới vào database
                formulaTable.addNewFormula(
                        formulaName, formulaContent, subjectId, userId, formulaCreatedDate
                );

                formulas.clear();
                formulas.addAll(formulaTable.getFormulaBySubjectIdAndUserId(subjectId, userId));


                formulaAdapter.notifyDataSetChanged();

                Toast.makeText(FormulaActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(FormulaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}