package br.com.arthurgrangeiro.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.arthurgrangeiro.habittracker.data.HabitContract.EventEntry;
import br.com.arthurgrangeiro.habittracker.data.HabitDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertData();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String [] projection = {
                EventEntry._ID,
                EventEntry.COLUMN_EVENT_NAME,
                EventEntry.COLUMN_EVENT_DATE,
                EventEntry.COLUMN_EVENT_HOUR,
                EventEntry.COLUMN_EVENT_DONE
        };

        Cursor cursor = db.query(EventEntry.TABLE_NAME, projection, null, null, null, null, null);

        TextView displayView = (TextView) findViewById(R.id.text_view_event);

        try {
            displayView.setText("The events table contains " + cursor.getCount() + " Events.\n\n");
            displayView.append(EventEntry._ID + " - "+
                    EventEntry.COLUMN_EVENT_NAME + " - " +
                    EventEntry.COLUMN_EVENT_DATE + " - " +
                    EventEntry.COLUMN_EVENT_HOUR + " - " +
                    EventEntry.COLUMN_EVENT_DONE + "\n");

            int idColumnIndex = cursor.getColumnIndex(EventEntry._ID);
            int eventColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_NAME);
            int dateColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_DATE);
            int hourColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_HOUR);
            int doneColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_DONE);

            while (cursor.moveToNext()){
                int currentID = cursor.getInt(idColumnIndex);
                String currentEvent = cursor.getString(eventColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentHour = cursor.getString(hourColumnIndex);
                int currentDone = cursor.getInt(doneColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentEvent + " - " +
                        currentDate + " - " +
                        currentHour + " - " +
                        currentDone));
            }

        } finally {
            cursor.close();
        }
    }

    private void insertData(){

        HabitDbHelper mDbHelper = new HabitDbHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(EventEntry.COLUMN_EVENT_NAME, "Walking the dog");
        values.put(EventEntry.COLUMN_EVENT_DATE, "01.23.2016");
        values.put(EventEntry.COLUMN_EVENT_HOUR, "18:30");
        values.put(EventEntry.COLUMN_EVENT_DONE, EventEntry.EVENT_COMPLETE);

        ContentValues values2 = new ContentValues();
        values2.put(EventEntry.COLUMN_EVENT_NAME, "Reading the book");
        values2.put(EventEntry.COLUMN_EVENT_DATE, "01.23.2016");
        values2.put(EventEntry.COLUMN_EVENT_HOUR, "23:00");
        values2.put(EventEntry.COLUMN_EVENT_DONE, EventEntry.PENDING_EVENT);

        // Insert the news rows, returning the primary key value of the new row
        long firstRow = db.insert(EventEntry.TABLE_NAME, null, values);
        long secondRow = db.insert(EventEntry.TABLE_NAME, null, values2);
    }
}
