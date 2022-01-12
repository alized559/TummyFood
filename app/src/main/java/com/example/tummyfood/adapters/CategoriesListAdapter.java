package com.example.tummyfood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
            TextView title = v.findViewById(R.id.categoryTitle);
            ImageView categoryImage = v.findViewById(R.id.categoryImage);

            if (categoryImage != null) {
                categoryImage.setImageBitmap(p.getImage());
            }
            if (title != null) {
                title.setText(p.getTitle());
            }
        }
        return v;
    }
}