package android.beginnerranch.bariatriapp.reminders.impl;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.widget.NumberPicker;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.beginnerranch.bariatriapp.utils.AlarmReceiver;
import android.beginnerranch.bariatriapp.reminders.Reminder;
import android.beginnerranch.bariatriapp.reminders.ReminderDbHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.beginnerranch.bariatriapp.R;

import java.util.Calendar;
import java.util.Map;

import static android.beginnerranch.bariatriapp.reminders.Reminder.ReminderStruct.COLUMN_NAME_TIME_FROM;
import static android.beginnerranch.bariatriapp.reminders.Reminder.ReminderStruct.COLUMN_NAME_TIME_TO;
import static android.beginnerranch.bariatriapp.reminders.Reminder.ReminderStruct.COLUMN_NAME_TITLE;

public class ReminderActivity extends AppCompatActivity {

    private ReminderViewModel loginViewModel;
    /** Private members of the class */
    private Button pickTime;
    private Button pickTimeFrom;
    private Button pickTimeTo;
    private boolean to = true;
    Button saveButton;
    private TextView reminderName;
    private ReminderDbHelper reminderDbHelper;
    private int pHour;
    private int pMinute;
    private int pHourTo;
    private int pMinuteTo;
    private int pHourFrom;
    private int pMinuteFrom;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID = 0;


    /** Callback received when the user "picks" a time in the dialog */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    updateDisplay(pickTime, pHour, pMinute);
                    displayToast();
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListenerTo =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHourTo = hourOfDay;
                    pMinuteTo = minute;
                    updateDisplay(pickTimeTo, pHourTo, pMinuteTo);
                    displayToast();
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListenerFrom =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHourFrom = hourOfDay;
                    pMinuteFrom = minute;
                    updateDisplay(pickTimeFrom, pHourFrom, pMinuteFrom);
                    displayToast();
                }
            };
    /** Displays a notification when the time is updated */
    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(pHour + " : " + pMinute),   Toast.LENGTH_SHORT).show();

    }

    /** Add padding to numbers less than ten */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    /** Updates the time in the TextView */
    private void updateDisplay(Button pickTime, int hour, int minutes) {
        pickTime.setText(hour + " : " + minutes);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 8:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                }
                break;

            default:
                break;
        }
        Log.i("Permission ", grantResults.toString());
        Log.i("Permission ", Integer.valueOf(grantResults[0]).toString());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        reminderDbHelper = new ReminderDbHelper(getBaseContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup();
        saveButton = findViewById(R.id.save_reminder);
        reminderName = findViewById(R.id.username);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Daily").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Repeating").setContent(R.id.tab2));
        loginViewModel = ViewModelProviders.of(this, new ReminderViewModelFactory())
                .get(ReminderViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final Button loginButton = findViewById(R.id.save_reminder);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<ReminderFormState>() {
            @Override
            public void onChanged(@Nullable ReminderFormState reminderFormState) {
            }
        });



        loginViewModel.getLoginResult().observe(this, new Observer<ReminderResult>() {
            @Override
            public void onChanged(@Nullable ReminderResult reminderResult) {
                if (reminderResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (reminderResult.getError() != null) {
                    showLoginFailed(reminderResult.getError());
                }
                if (reminderResult.getSuccess() != null) {
                    updateUiWithUser(reminderResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(reminderName.getText().length() < 3) {
                    loginButton.setVisibility(View.INVISIBLE);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(reminderName.getText().length() < 3) {
                    loginButton.setVisibility(View.INVISIBLE);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("input", reminderName.getText().toString());
                if(reminderName.getText().length() < 3) {
                    loginButton.setVisibility(View.INVISIBLE);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                }
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = reminderDbHelper.getWritableDatabase();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 14);

                long time = calendar.getTimeInMillis()+5000;


                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, time,
                        AlarmManager.INTERVAL_DAY, alarmIntent);
// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(Reminder.ReminderStruct.COLUMN_NAME_TITLE, reminderName.getText().toString());
                values.put(COLUMN_NAME_TIME_FROM, pickTime.getText().toString());
                Log.i("values", values.toString());
// Insert the new row, returning the primary key value of the new row
                long newRowId = db.insertWithOnConflict(Reminder.ReminderStruct.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                Log.i("values", Long.valueOf(newRowId).toString());
                finish();
            }
        });

        /** Capture our View elements */
        pickTime = (Button) findViewById(R.id.pickTime);
        pickTimeFrom = (Button) findViewById(R.id.pickTimeFrom);
        pickTimeTo = (Button) findViewById(R.id.pickTimeTo);
        /** Listener for click event of the button */
        pickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
        pickTimeFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID + 1);
            }
        });
        pickTimeTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID + 2);
            }
        });
        /** Get the current time */
        final Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);


        /** Display the current time in the TextView */
        updateDisplay(pickTime, pHour, pMinute);
        updateDisplay(pickTimeTo, pHourTo, pMinuteTo);
        updateDisplay(pickTimeFrom, pHourFrom, pMinuteFrom);
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(getIntent().getStringExtra("name") != null) {
            reminderName.setText(getIntent().getStringExtra("name"));
            Map<String, String> record = reminderDbHelper.getRecord(getIntent().getStringExtra("name"));
            Log.i("ReminderActivity", record.toString());
            if(record.get(COLUMN_NAME_TIME_FROM) != null) {
                pickTime.setText(record.get(COLUMN_NAME_TIME_FROM));
                pickTimeFrom.setText(record.get(COLUMN_NAME_TIME_FROM));
                pHour = Integer.valueOf(record.get(COLUMN_NAME_TIME_FROM).split(":")[0].replace(" ", ""));
                pMinute = Integer.valueOf(record.get(COLUMN_NAME_TIME_FROM).split(":")[1].replace(" ", ""));
                pHourFrom = Integer.valueOf(record.get(COLUMN_NAME_TIME_FROM).split(":")[0].replace(" ", ""));
                pMinuteFrom = Integer.valueOf(record.get(COLUMN_NAME_TIME_FROM).split(":")[1].replace(" ", ""));
                createNotification(reminderName.getText().toString());

            }
            if(record.get(COLUMN_NAME_TIME_TO) != null) {
                pickTimeTo.setText(record.get(COLUMN_NAME_TIME_TO));
                pHourTo = Integer.valueOf(record.get(COLUMN_NAME_TIME_TO).split(":")[0].replace(" ", ""));
                pMinuteTo = Integer.valueOf(record.get(COLUMN_NAME_TIME_TO).split(":")[1].replace(" ", ""));
                // Set the alarm to start at approximately 2:00 p.m.
                createNotification(reminderName.getText().toString());


// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.

            }
        }


        NumberPicker number_picker = findViewById(R.id.numberPicker);
        String[] values = new String[13];
        for (int i = 0; i < values.length; i++) {
            String number = Integer.toString(i * 5 + 5);
            values[i] = number;
        }
        number_picker.setMinValue(5);
        number_picker.setMaxValue(values.length+3);
        number_picker.setWrapSelectorWheel(false);
        number_picker.setDisplayedValues(values);
        number_picker.setValue(0);
    }

    private void updateUiWithUser(ReminderView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void createNotification( String messageBody) {
        long when = System.currentTimeMillis() + 1000L;
        Log.d("request code", Integer.valueOf(messageBody.hashCode()).toString());

        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("myAction", "mDoNotify");
        intent.putExtra("message", messageBody);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, messageBody.hashCode(), intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, when, 10000, pendingIntent);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);
            case TIME_DIALOG_ID + 1:
                return new TimePickerDialog(this,
                        mTimeSetListenerFrom, pHourFrom, pMinuteFrom, false);
            case TIME_DIALOG_ID + 2:
                return new TimePickerDialog(this,
                        mTimeSetListenerTo, pHourTo, pMinuteTo, false);
        }
        return null;
    }
}
