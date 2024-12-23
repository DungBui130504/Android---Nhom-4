package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddFormulaActivity extends AppCompatActivity {
    private ImageButton backBtn;
    private EditText formulaNameEditTxt, formulaContentEditTxt;
    private ImageButton submit;

    private String formulaNameText;
    private String formulaContentText;

    private int subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_formula);

        // Handle system bars insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        backBtn = findViewById(R.id.backBtn);
        formulaNameEditTxt = findViewById(R.id.formulaNameTxt);
        formulaContentEditTxt = findViewById(R.id.formulaContentTxt);
        submit = findViewById(R.id.submit);

        // Retrieve intent data
        Intent intent = getIntent();
        subjectId = intent.getIntExtra("subjectId", -1);

        boolean fromClick = intent.getBooleanExtra("fromClick", false);
        if (fromClick) {
            formulaNameText = intent.getStringExtra("formulaNameContext");
            formulaContentText = intent.getStringExtra("formulaContentContext");

            // Populate fields if editing
            formulaNameEditTxt.setText(formulaNameText);
            formulaContentEditTxt.setText(formulaContentText);
        }

        // Handle back button click
        backBtn.setOnClickListener(view -> finish());

        // Handle submit button click
        submit.setOnClickListener(view -> {
            try {
                // Validate input
                if (formulaNameEditTxt.getText().toString().trim().isEmpty()) {
                    showToast("Formula name invalid");
                    return;
                }

                if (formulaContentEditTxt.getText().toString().trim().isEmpty()) {
                    showToast("Formula name invalid");
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formulaUpdatedDate = sdf.format(Calendar.getInstance().getTime());

                // Prepare result
                Intent resultIntent = new Intent();
                resultIntent.putExtra("formulaNameTxt", formulaNameEditTxt.getText().toString());
                resultIntent.putExtra("formulaContentTxt", formulaContentEditTxt.getText().toString());
                resultIntent.putExtra("formulaUpdatedDate", sdf.format(formulaUpdatedDate));

                setResult(RESULT_OK, resultIntent);
                finish();

            } catch (Exception e) {
                showToast(e.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(AddFormulaActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
