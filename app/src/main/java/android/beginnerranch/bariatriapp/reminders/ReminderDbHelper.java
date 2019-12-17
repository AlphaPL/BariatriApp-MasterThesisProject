package android.beginnerranch.bariatriapp.reminders;

import android.beginnerranch.bariatriapp.reminders.Reminder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Reminder.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Reminder.ReminderStruct.TABLE_NAME + " (" +
                    Reminder.ReminderStruct.COLUMN_NAME_TITLE + " TEXT PRIMARY KEY," +
                    Reminder.ReminderStruct.COLUMN_NAME_TIME_FROM + " TEXT," +
                    Reminder.ReminderStruct.COLUMN_NAME_TIME_TO + " TEXT," +
                    Reminder.ReminderStruct.COLUMN_NAME_INTERVAL+ " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Reminder.ReminderStruct.TABLE_NAME;

    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void deleteRecord(String name) {
        Log.d("TAG", "getTableAsString called");
        getWritableDatabase().delete(Reminder.ReminderStruct.TABLE_NAME,Reminder.ReminderStruct.COLUMN_NAME_TITLE + " = '" + name +"'", null);
    }

    public List<Map<String, String>> getRecords() {
        Log.d("TAG", "getTableAsString called");
        List<Map<String, String>> reminders = new ArrayList<>();
        String tableString = String.format("Table %s:\n", DATABASE_NAME);
        Cursor allRows  = getReadableDatabase().rawQuery("SELECT * FROM " + Reminder.ReminderStruct.TABLE_NAME , null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                HashMap<String, String> object = new HashMap<>();
                for (String name : columnNames) {
                    object.put(name, allRows.getString(allRows.getColumnIndex(name)));
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";
                reminders.add(object);
            } while (allRows.moveToNext());
        }

        return reminders;
    }

    public Map<String, String> getRecord(String name) {
        Log.d("TAG", "getTableAsString called");
        List<Map<String, String>> reminders = new ArrayList<>();
        String tableString = String.format("Table %s:\n", DATABASE_NAME);
        Cursor allRows  = getReadableDatabase().rawQuery("SELECT * FROM " + Reminder.ReminderStruct.TABLE_NAME + " where name = '" + name + "'", null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                HashMap<String, String> object = new HashMap<>();
                for (String colname : columnNames) {
                    object.put(colname, allRows.getString(allRows.getColumnIndex(colname)));
                    tableString += String.format("%s: %s\n", colname,
                            allRows.getString(allRows.getColumnIndex(colname)));
                }
                tableString += "\n";
                reminders.add(object);
            } while (allRows.moveToNext());
        }

        return reminders.get(0);
    }
}
