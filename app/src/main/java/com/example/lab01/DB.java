package com.example.lab01; // Zaharov 493

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    // Создание таблицы
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create Table Dialogs(IP text, Port text, Name text, Message text)";
        db.execSQL(sql);
    }

    //Сохранение данных об отрпавителе и его сообщение
    public void OnSaveDialogs(msg m)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Insert Into Dialogs Values('" + m.IP + "','" + m.Port + "','" + m.Name + "'," + m.Text + ");";
        db.execSQL(sql);
    }
    //Очистка таблицы
    public void ClearTable(SQLiteDatabase db)
    {
        String sql = "Delete from Dialogs";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
