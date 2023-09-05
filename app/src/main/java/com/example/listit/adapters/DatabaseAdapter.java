package com.example.listit.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.listit.data.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {

    static DatabaseHelper databaseHelper;

    public DatabaseAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertRow(TaskModel task) {
        SQLiteDatabase readable = databaseHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.BODY_COL, task.getBody());
        contentValues.put(DatabaseHelper.TIME_COL, task.getTime());
        contentValues.put(DatabaseHelper.DATE_COL, task.getDate());

        return readable.insert(DatabaseHelper.TABLE_NAME, null, contentValues);

    }

    static public List<TaskModel> getAllData() {
        SQLiteDatabase writable = databaseHelper.getWritableDatabase();
        List<TaskModel> contacts = new ArrayList<>();

        String[] cols = {DatabaseHelper.ID_COL, DatabaseHelper.BODY_COL, DatabaseHelper.TIME_COL, DatabaseHelper.DATE_COL};
        Cursor cursor = writable.query(DatabaseHelper.TABLE_NAME, cols, null, null, null, null, null);

        cursor.moveToFirst();

        if (cursor.move(0)) {
            do {
                String id = cursor.getString(0);
                String body = cursor.getString(1);
                String time = cursor.getString(2);
                String date = cursor.getString(3);

                TaskModel task = new TaskModel(id, body, time, date);
                contacts.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contacts;

    }

    public void remove(String id) {
        SQLiteDatabase writable = databaseHelper.getWritableDatabase();

        writable.delete(DatabaseHelper.TABLE_NAME, "id=?", new String[]{id});

    }

    public TaskModel getRow(String id) {
        SQLiteDatabase readable = databaseHelper.getReadableDatabase();
        TaskModel task = null;

        String[] cols = {DatabaseHelper.ID_COL, DatabaseHelper.BODY_COL, DatabaseHelper.TIME_COL, DatabaseHelper.DATE_COL};
        Cursor cursor = readable.query(DatabaseHelper.TABLE_NAME, cols, "id=?", new String[]{id}, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()){
            task = new TaskModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
        }

        return task;

    }

    public void updateRow(String id, String newBody, String time, String date) {

        SQLiteDatabase writeable = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BODY", newBody);
        contentValues.put("TIME", time);
        contentValues.put("DATE", date);

        writeable.update(DatabaseHelper.TABLE_NAME, contentValues, "id=?", new String[]{id});

    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_NAME = "TASKS";

        private static final String TABLE_NAME = "TABLE_TASKS";

        private static final String ID_COL = "id";

        private static final String BODY_COL = "body";

        private static final String TIME_COL = "time";

        private static final String DATE_COL = "date";

        String query = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BODY_COL + " TEXT, " +
                TIME_COL + " TEXT, " +
                DATE_COL + " TEXT );";

        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

}
