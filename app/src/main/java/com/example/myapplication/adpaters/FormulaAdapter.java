package com.example.myapplication.adpaters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.activity.AddFormulaActivity;
import com.example.myapplication.models.Formula.FormulaObject;

import java.util.ArrayList;

public class FormulaAdapter extends ArrayAdapter {
    private final Context context;
    private final int resourceId;
    private final ArrayList<FormulaObject> formulaObjects;
    private static final int EDIT_FORMULA_REQUEST_CODE = 103;

    public FormulaAdapter(Context context, ArrayList<FormulaObject> formulaObjects, int resourceId) {
        super(context, resourceId, formulaObjects);
        this.context = context;
        this.resourceId = resourceId;
        this.formulaObjects = formulaObjects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();
            holder.formulaItem = convertView.findViewById(R.id.formulaItem);
            holder.updateBtn = convertView.findViewById(R.id.formulaEdtBtn);
            holder.deleteBtn = convertView.findViewById(R.id.formulaDelBtn);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind data to the views
        FormulaObject formula = formulaObjects.get(position);
        holder.formulaItem.setText(formula.getFormulaName());

        // Handle update button click
        holder.updateBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddFormulaActivity.class);
            intent.putExtra("fromClick", true);
            intent.putExtra("formulaNameContext", formula.getFormulaName());
            intent.putExtra("formulaContentContext", formula.getFormulaContent());
            intent.putExtra("position", position); // Pass position to identify which formula was edited

            // Ensure the context is cast to Activity for `startActivityForResult`
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, EDIT_FORMULA_REQUEST_CODE);
            }
        });

        // Handle delete button click
        holder.deleteBtn.setOnClickListener(view -> {
            formulaObjects.remove(position);
            notifyDataSetChanged(); // Notify adapter about data change
        });

        return convertView;
    }


    public void updateFormula(int position, String newName, String newContent) {
        FormulaObject formula = formulaObjects.get(position);
        formula.setFormulaName(newName);
        formula.setFormulaContent(newContent);
        notifyDataSetChanged(); // Refresh the list
    }

    static class ViewHolder {
        TextView formulaItem;
        ImageButton updateBtn;
        ImageButton deleteBtn;
    }
}
