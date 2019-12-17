package android.beginnerranch.bariatriapp.recipes;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.beginnerranch.bariatriapp.R;

import android.beginnerranch.bariatriapp.recipes.IngredientFragment.OnListFragmentInteractionListener;

import java.net.URLEncoder;
import java.util.List;

public class MyIngredientRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;

    public MyIngredientRecyclerViewAdapter(List<String> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String content = mValues.get(position);
        holder.mIdView.setText(Integer.valueOf(position + 1).toString());
        holder.mContentView.setText(mValues.get(position));

        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String escapedQuery = URLEncoder.encode(content, "UTF-8");
                    Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery+"&tbm=shop");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.getApplicationContext().startActivity(intent);
                } catch (Exception e)
                {
                    Log.e("Error retr ingredient", e.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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
