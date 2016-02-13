package com.georgeroman.tasky.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskListDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Tasks.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASKS = "Tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TASK = "task";

    public TaskListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TASKS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TASK + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public void deleteAllTasks() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TASKS);
    }

    public void insertAllTasks(List<String> items) {
        SQLiteDatabase db = getWritableDatabase();

        for (int i = 0; i < items.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TASK, items.get(i));

            db.insert(TABLE_TASKS, null, values);
        }
    }

    public List<String> getAllTasks() {
        List<String> items = new ArrayList<String>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);

        if (cursor.moveToFirst()) {
            do {
                String task = cursor.getString(1);
                items.add(task);
            } while (cursor.moveToNext());
        }

        return items;
    }
}
