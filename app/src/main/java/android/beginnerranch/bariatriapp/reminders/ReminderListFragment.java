package android.beginnerranch.bariatriapp.reminders;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.beginnerranch.bariatriapp.reminders.impl.ReminderActivity;
import android.beginnerranch.bariatriapp.utils.AlarmReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import android.beginnerranch.bariatriapp.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Map;

import static android.beginnerranch.bariatriapp.reminders.Reminder.ReminderStruct.COLUMN_NAME_TITLE;

public class ReminderListFragment extends Fragment implements View.OnClickListener {

    ReminderDbHelper dbHelper;
    View root = null;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new ReminderDbHelper(getContext());
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        for(Map<String, String> reminder: dbHelper.getRecords()) {
            Log.d("Reminder ", reminder.toString());
            createNotification(reminder.get(COLUMN_NAME_TITLE));
        }
        FloatingActionButton mClickButton1 = root.findViewById(R.id.addReminderButton);
        mClickButton1.setOnClickListener(this);

        refreshRecords(root);
        return root;

    }

    private void refreshRecords(View root) {
        final ListView listview = (ListView) root.findViewById(R.id.reminder_list);
        final List<Map<String, String>> list = dbHelper.getRecords();
        final StableArrayAdapter adapter = new StableArrayAdapter(getContext(),
                R.layout.reminder_list, list);
        listview.setAdapter(adapter);
    }

    private class StableArrayAdapter extends ArrayAdapter<Map<String, String>> {

        public List<Map<String, String>> values;
        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<Map<String, String>> objects) {
            super(context, textViewResourceId, objects);
            Log.d("objects", objects.toString());
            values = objects;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewGroup parentView = parent;
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.reminder_list, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.name_reminder);
            textView.setText(values.get(position).get(Reminder.ReminderStruct.COLUMN_NAME_TITLE));
            Button button = (Button) rowView.findViewById(R.id.delete_reminder);
            final int itemPos = position;
            button.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ViewGroup parent  = parentView;
                    dbHelper.deleteRecord(values.get(position).get(Reminder.ReminderStruct.COLUMN_NAME_TITLE));
                    parent.animate().setDuration(2000).alpha(0)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    values.remove(itemPos);
                                    notifyDataSetChanged();
                                    parent.setAlpha(1);
                                }
                            });
                }
            });
            textView.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("dua lipa ", Integer.valueOf(position).toString());
                    Intent intent = new Intent(getActivity().getApplicationContext(), ReminderActivity.class);
                    Bundle b = new Bundle();
                    b.putString("name", values.get(position).get(Reminder.ReminderStruct.COLUMN_NAME_TITLE));
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            return rowView;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshRecords(root);
    }

    private void createNotification( String messageBody) {
        long when = System.currentTimeMillis() + 1000L;
        Log.d("request code", Integer.valueOf(messageBody.hashCode()).toString());

        AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("myAction", "mDoNotify");
        intent.putExtra("message", messageBody);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), messageBody.hashCode(), intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, when, 10000, pendingIntent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity().getApplicationContext(), ReminderActivity.class);
        startActivityForResult(intent, 6);
    }
}