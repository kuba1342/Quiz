package com.example.kuba.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        category = findViewById(R.id.edit_text_category);

        Button buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
    }

    private void addCategory() {
        String categoryString = category.getText().toString();

        if (checkLength(categoryString)) {
            Category newCategory = new Category(categoryString);
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            dbHelper.addCategory(newCategory);

            clearField();

            Toast.makeText(this, "Category added!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Category name is too short.", Toast.LENGTH_SHORT).show();
    }

    private void clearField() {
        category.setText("");
    }

    private boolean checkLength(String string) {
        if (string.length() > 0)
            return true;
        else
            return false;
    }
}
