package br.com.arthurgrangeiro.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by Arthur on 23/01/2017.
 */

public class HabitContract {

    public static final class EventEntry implements BaseColumns{
        public final static String TABLE_NAME = "events";

        public final static String _ID =  BaseColumns._ID;
        public final static String COLUMN_EVENT_NAME = "event";
        public final static String COLUMN_EVENT_DATE = "data";
        public final static String COLUMN_EVENT_HOUR = "hour";
        public final static String COLUMN_EVENT_DONE = "done";

        public final static int PENDING_EVENT = 0;
        public final static int EVENT_COMPLETE = 1;
    }

}
