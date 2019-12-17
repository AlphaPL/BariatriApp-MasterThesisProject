package android.beginnerranch.bariatriapp.recipes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final String[] TAB_TITLES = new String[]{"Photo", "Ingredients", "Directions", "Nutrition"};
    private final Context mContext;
    private final Bundle recipe;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Bundle name) {
        super(fm);
        mContext = context;
        this.recipe = name;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new Photo(recipe.getString("name"));
        } else if (position == 1) {
            return new IngredientFragment(recipe.getString("ingredients"));
        } else if (position == 2) {
            return new DirectionsFragment(recipe.getString("directions"));
        } else {
            return new NutritionFragment(recipe.getString("nutrition"));
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }
}