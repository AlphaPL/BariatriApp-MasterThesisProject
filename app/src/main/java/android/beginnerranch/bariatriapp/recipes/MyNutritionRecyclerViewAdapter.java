package android.beginnerranch.bariatriapp.recipes;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.beginnerranch.bariatriapp.R;

import android.beginnerranch.bariatriapp.recipes.NutritionFragment.OnListFragmentInteractionListener;

import java.util.List;


public class MyNutritionRecyclerViewAdapter extends RecyclerView.Adapter<MyNutritionRecyclerViewAdapter.ViewHolder> {


    private List<String> key;
    private List<String> values;
    private final OnListFragmentInteractionListener mListener;

    public MyNutritionRecyclerViewAdapter(List<String> key, List<String> values, OnListFragmentInteractionListener listener) {
        this.key = key;
        this.values = values;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_nutrition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(key.get(position));
        holder.mContentView.setText(values.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return key.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
