package com.example.kuba.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InsertQuestionActivity extends AppCompatActivity {

    private EditText question;
    private EditText answerNr1;
    private EditText answerNr2;
    private EditText answerNr3;
    private Spinner spinnerAnswers;
    private Spinner spinnerDifficulty;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_question);

        question = findViewById(R.id.edit_text_question);
        answerNr1 = findViewById(R.id.edit_text_option1);
        answerNr2 = findViewById(R.id.edit_text_option2);
        answerNr3 = findViewById(R.id.edit_text_option3);
        spinnerAnswers = findViewById(R.id.spinner_answerNr);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerCategory = findViewById(R.id.spinner_category);

        loadAnswers();
        loadDifficultyLevels();
        loadCategories();

        Button buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
            }
        });
    }

    private void loadAnswers() {
        List<Integer> answers = new ArrayList<Integer>();
        answers.add(1);
        answers.add(2);
        answers.add(3);

        ArrayAdapter<Integer> adapterAnswers = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, answers);
        adapterAnswers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnswers.setAdapter(adapterAnswers);
    }

    private void loadCategories() {
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }

    private void loadDifficultyLevels() {
        String[] difficultyLevels = Question.getAllDifficultyLevels();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }

    private void addQuestion() {
        String questionString = question.getText().toString();
        String answerNr1String = answerNr1.getText().toString();
        String answerNr2String = answerNr2.getText().toString();
        String answerNr3String = answerNr3.getText().toString();
        int correctAnswer = (int) spinnerAnswers.getSelectedItem();
        String difficulty = (String) spinnerDifficulty.getSelectedItem();
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();

        boolean filled = true;
        if (!checkLength(questionString))
            filled = false;
        if (!checkLength(answerNr1String))
            filled = false;
        if (!checkLength(answerNr2String))
            filled = false;
        if (!checkLength(answerNr3String))
            filled = false;

        if (filled) {
            Question newQuestion = new Question(questionString, answerNr1String, answerNr2String, answerNr3String, correctAnswer, difficulty, categoryID);
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            dbHelper.addQuestion(newQuestion);

            clearFields();

            Toast.makeText(this, "Question added!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Please, fill every field.", Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        question.setText("");
        answerNr1.setText("");
        answerNr2.setText("");
        answerNr3.setText("");
    }

    private boolean checkLength(String string) {
        if (string.length() > 0)
            return true;
        else
            return false;
    }
}
