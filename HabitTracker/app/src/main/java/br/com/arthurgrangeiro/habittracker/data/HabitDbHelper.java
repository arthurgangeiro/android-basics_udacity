package br.com.arthurgrangeiro.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.arthurgrangeiro.habittracker.data.HabitContract.EventEntry;
/**
 * Created by Arthur on 23/01/2017.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shelter.db";
    public static final int DATABASE_VERSION = 1;

    private final String CREATE_DATABASE = "CREATE TABLE "+ EventEntry.TABLE_NAME +" ("+
            EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            EventEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL, "+
            EventEntry.COLUMN_EVENT_DATE + " TEXT NOT NULL, "+
            EventEntry.COLUMN_EVENT_HOUR + " TEXT NOT NULL, "+
            EventEntry.COLUMN_EVENT_DONE + " INTEGER NOT NULL DEFAULT 0);";

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(db);
    }
}
