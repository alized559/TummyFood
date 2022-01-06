package com.example.tummyfood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tummyfood.R;
import com.example.tummyfood.models.CategoriesDataModel;

import java.util.List;

public class CategoriesListAdapter extends ArrayAdapter<CategoriesDataModel> {

    private Context mContext;
    private int resourceLayout;

    public CategoriesListAdapter(Context context, int resource, List<CategoriesDataModel> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        CategoriesDataModel p = getItem(position);

        if (p != null) {
            ImageView image = (ImageView) v.findViewById(R.id.categoryImage);
            TextView title = (TextView) v.findViewById(R.id.categoryTitle);
            TextView seeAll = (TextView) v.findViewById(R.id.seeAll);

            if (image != null) {
                image.setImageResource(p.getImage());
            }
            if (title != null) {
                title.setText(p.getTitle());
            }

//            icon.setOnClickListener(view -> {

//            });
        }
        return v;
    }
}