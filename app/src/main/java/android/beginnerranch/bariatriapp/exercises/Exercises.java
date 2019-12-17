package android.beginnerranch.bariatriapp.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.beginnerranch.bariatriapp.R;
import android.beginnerranch.bariatriapp.exercises.ExercisesFragment;
import android.os.Bundle;

public class Exercises extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ExercisesFragment())
                    .commitNow();
        }
    }
}
