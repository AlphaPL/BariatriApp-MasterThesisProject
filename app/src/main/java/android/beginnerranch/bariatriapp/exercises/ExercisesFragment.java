package android.beginnerranch.bariatriapp.exercises;

import android.beginnerranch.bariatriapp.Photo;
import android.beginnerranch.bariatriapp.reminders.Reminder;
import android.beginnerranch.bariatriapp.reminders.ReminderDbHelper;
import android.beginnerranch.bariatriapp.recipes.RecipeActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import android.beginnerranch.bariatriapp.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ExercisesFragment extends Fragment implements View.OnClickListener {

    ReminderDbHelper dbHelper;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new ReminderDbHelper(getContext());
        View root = inflater.inflate(R.layout.exercises_fragment, container, false);
        JSONArray recipes = null;
        List<Map<String, String>> list = new ArrayList<>();
        try {
            recipes = new JSONArray(readRecipes());
            for(int i = 0 ; i < recipes.length() ; i++){
                Map<String, String> recipe = new HashMap<>();
                Log.i("json", recipes.get(i).toString());
                recipe.put("name", recipes.getJSONObject(i).getString("name"));;
                list.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("num of exercises", Integer.valueOf(list.size()).toString());
        FloatingActionButton mClickButton1 = root.findViewById(R.id.addReminderButton);

        final GridView listview = root.findViewById(R.id.exercises_list);
        final StableArrayAdapter adapter = new StableArrayAdapter(getContext(),
                R.layout.recipe, list);
        listview.setAdapter(adapter);
        FloatingActionButton photoRedirect = root.findViewById(R.id.fab);
        photoRedirect.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Photo.class);
                startActivityForResult(intent, 100);
            }
        });
        return root;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("photo result", Integer.valueOf(requestCode).toString());
        Log.i("photo resultCode", Integer.valueOf(resultCode).toString());
        Log.i("photo data", data.toString());
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Log.i("Photo ", data.toString());
            }
        }
    }

    private String readRecipes() {
        InputStream is = getResources().openRawResource(R.raw.exercises);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
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
            final View rowView = inflater.inflate(R.layout.exercise, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.name_exercise);
            String name = values.get(position).get(Reminder.ReminderStruct.COLUMN_NAME_TITLE);
            textView.setText(name);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
            Resources res = getResources(); /** from an Activity */
            Log.i("name", name);
            Log.i("sde", name.replace(" ", "_"));
            int resd = getResources().getIdentifier(name.replace(" ", "_"), "drawable", getActivity().getPackageName());
            Log.i("fdsfsf", Integer.valueOf(resd).toString());
            imageView.setImageResource(getResources().getIdentifier(name.replace(" ", "_"), "drawable", getActivity().getPackageName()));

            //imageView.setImageResource(getResources().getIdentifier("benchpress.png", "drawable", getActivity().getPackageName()));


//            textView.setOnClickListener(new AdapterView.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity().getApplicationContext(), RecipeActivity.class);
//                    Bundle b = new Bundle();
//                    b.putString("name", values.get(position).get(Reminder.ReminderStruct.COLUMN_NAME_TITLE));
//                    intent.putExtras(b);
//                    startActivity(intent);
//                }
//            });
            return rowView;
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity().getApplicationContext(), RecipeActivity.class);
        startActivity(intent);
    }
}