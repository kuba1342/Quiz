package com.example.kuba.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kuba.quiz.QuizContract.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    private ArrayList<Integer> preparedQuestionList;

    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();

        preparedQuestionList = new ArrayList<>();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Programming");
        insertCategory(c1);
        Category c2 = new Category("Geography");
        insertCategory(c2);
        Category c3 = new Category("Math");
        insertCategory(c3);
    }

    public void addCategory(Category category) {
        List<Category> categories = getAllCategories();

        boolean exists = false;

        for (Category existingCategory : categories) {
            if (category == existingCategory) {
                exists = true;
            }
        }

        if (!exists) {
            db = getWritableDatabase();
            insertCategory(category);
        }
    }

    public void addCategories(List<Category> categories) {
        db = getWritableDatabase();

        for (Category category : categories) {
            insertCategory(category);
        }
    }

    private void insertCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("Which of the following is not a stable sorting algorithm in its typical implementation?",
                "Quick Sort", "Merge Sort", "Insertion Sort", 1,
                Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q1);
        Question test1 = new Question("The time complexity of computing the transitive closure of a binary relation on a set of n elements is known to be:",
                "O(n)", "O(n ^ (3/2))", "O(n^3)", 3,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(test1);
        Question test2 = new Question("Which of the following statements is/are TRUE for an undirected graph? P: Number of odd degree vertices is even Q: Sum of degrees of all vertices is even?",
                "Neither P nor Q", "Both P and Q", "Q Only", 2,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(test2);
        Question test3 = new Question("Given an undirected graph G with V vertices and E edges, the sum of the degrees of all vertices is:",
                "2V", "V", "2E", 3,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(test3);
        Question qP3 = new Question("Which of these is not a Python core data type?",
                "Class", "Dictionary", "Tuples", 1,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(qP3);
        Question q2 = new Question("What is Earth's largest continent?",
                "Africa", "Europe", "Asia", 3,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(q2);
        Question qG2 = new Question("What river runs through Baghdad?",
                "Karun", "Jordan", "Tigris", 3,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(qG2);
        Question qG3 = new Question("What percentage of the River Nile is located in Egypt?",
                "22%", "83%", "9%", 1,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(qG3);
        Question q4 = new Question("What is the square root of 49?",
                "7", "16", "24,5", 1,
                Question.DIFFICULTY_HARD, Category.MATH);
        insertQuestion(q4);
        Question q5 = new Question("If I have a circle with a radius of 50yds, what is the diameter?",
                "25yds", "314yds", "100yds", 3,
                Question.DIFFICULTY_HARD, Category.MATH);
        insertQuestion(q5);
        Question q6 = new Question("Sinθ / cosθ = ?",
                "1", "Tanθ", "Secθ", 2,
                Question.DIFFICULTY_HARD, Category.MATH);
        insertQuestion(q6);
    }

    public void addQuestion(Question question) {

        List<Question> questions = getAllQuestions();

        boolean exists = false;

        for (Question existingQuestion : questions) {
            if (existingQuestion.getQuestion() == question.getQuestion()) {
                exists = true;
            }
        }

        if (!exists) {
            db = getWritableDatabase();
            insertQuestion(question);
        }
    }

    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();

        for (Question question : questions) {
            insertQuestion(question);
        }
    }

    private void insertQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        db = getReadableDatabase();

        String selection = QuestionTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.getCount() < 5)
            return getLeftQuestions(categoryID, difficulty);
        else
            return getRandomQuestions(categoryID, difficulty);
    }

    private ArrayList<Question> getLeftQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    private ArrayList<Question> getRandomQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        prepareQuestionList(categoryID, difficulty);

        ArrayList<Integer> questionIDs = new ArrayList<>();
        boolean notUsed;
        while (questionIDs.size() < 5) {
            notUsed = true;
            Random rand = new Random();
            int randID = preparedQuestionList.get(rand.nextInt(preparedQuestionList.size()));
            for (int ID : questionIDs) {
                if (randID == ID)
                    notUsed = false;
            }
            if (notUsed)
                questionIDs.add(randID);
        }

        if (c.moveToFirst()) {
            do {
                for (int questionID : questionIDs) {
                    int cursorID = c.getInt(c.getColumnIndex(QuestionTable._ID));
                    if (cursorID == questionID) {
                        Question question = new Question();
                        question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                        question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                        question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                        question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                        question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                        question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                        question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                        question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                        questionList.add(question);
                    }
                }
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    private void prepareQuestionList(int categoryID, String difficulty) {
        db = getReadableDatabase();

        String selection = QuestionTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (preparedQuestionList.size() != 0)
            preparedQuestionList.clear();

        if (c.moveToFirst()) {
            do {
                int questionID = c.getInt(c.getColumnIndex(QuestionTable._ID));
                preparedQuestionList.add(questionID);
            } while (c.moveToNext());
        }
    }
}