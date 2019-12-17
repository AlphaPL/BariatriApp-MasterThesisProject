package android.beginnerranch.bariatriapp.reminders;

import android.provider.BaseColumns;

public final class Reminder {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Reminder() {}

    /* Inner class that defines the table contents */
    public static class ReminderStruct implements BaseColumns {
        public static final String TABLE_NAME = "reminders";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_TIME_FROM = "timefrom";
        public static final String COLUMN_NAME_TIME_TO = "timeto";
        public static final String COLUMN_NAME_INTERVAL = "interval";
        public String name;
        public String timefrom;
        public String timeto;
        public String interval;
    }
}
