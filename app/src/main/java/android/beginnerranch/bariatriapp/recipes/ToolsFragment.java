package android.beginnerranch.bariatriapp.recipes;

import android.beginnerranch.bariatriapp.recipes.RecipeActivity;
import android.beginnerranch.bariatriapp.reminders.Reminder;
import android.beginnerranch.bariatriapp.reminders.ReminderDbHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class ToolsFragment extends Fragment implements View.OnClickListener {

    ReminderDbHelper dbHelper;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new ReminderDbHelper(getContext());
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        JSONArray recipes = null;
        List<Map<String, String>> list = new ArrayList<>();
        try {
            recipes = new JSONArray(readRecipes());
            for(int i = 0 ; i < recipes.length() ; i++){
                Map<String, String> recipe = new HashMap<>();
                Log.i("json", recipes.get(i).toString());
                recipe.put("name", recipes.getJSONObject(i).getString("name"));
                if(recipes.getJSONObject(i).has("INGREDIENTS"))
                recipe.put("ingredients", recipes.getJSONObject(i).getString("INGREDIENTS"));
                if(recipes.getJSONObject(i).has("DIRECTIONS"))
                recipe.put("directions", recipes.getJSONObject(i).getString("DIRECTIONS"));
                if(recipes.getJSONObject(i).has("NUTRITIONAL ANALYSIS"))
                recipe.put("nutrition", recipes.getJSONObject(i).getString("NUTRITIONAL ANALYSIS"));
                list.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("numOfRecipes", Integer.valueOf(list.size()).toString());
        FloatingActionButton mClickButton1 = root.findViewById(R.id.addReminderButton);

        final ListView listview = root.findViewById(R.id.recipes_list);
        Log.d("listview", listview.toString());
        final StableArrayAdapter adapter = new StableArrayAdapter(getContext(),
                R.layout.recipe, list);
        listview.setAdapter(adapter);
        return root;

    }

    private String readRecipes() {
        InputStream is = getResources().openRawResource(R.raw.recipes);
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
            final View rowView = inflater.inflate(R.layout.recipe, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.name_reminder);
            textView.setText(values.get(position).get(Reminder.ReminderStruct.COLUMN_NAME_TITLE));
            textView.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("dua lipa ", Integer.valueOf(position).toString());
                    Intent intent = new Intent(getActivity().getApplicationContext(), RecipeActivity.class);
                    Bundle b = new Bundle();
                    b.putString("name", values.get(position).get(Reminder.ReminderStruct.COLUMN_NAME_TITLE));
                    b.putString("directions", values.get(position).get("directions"));
                    b.putString("ingredients", values.get(position).get("ingredients"));
                    b.putString("nutrition", values.get(position).get("nutrition"));
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            return rowView;
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity().getApplicationContext(), RecipeActivity.class);
        startActivity(intent);
    }
}