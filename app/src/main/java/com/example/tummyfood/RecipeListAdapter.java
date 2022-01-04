package com.example.tummyfood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

public class RecipeListAdapter extends ArrayAdapter<RecipeDataModel> {

    private int resourceLayout;
    private ArrayList<RecipeDataModel> dataSet;
    private Context mContext;

    private RequestQueue queue;

    public RecipeListAdapter(Context context, int resource, ArrayList<RecipeDataModel> data, RequestQueue queue) {
        super(context, R.layout.recipelistitem, data);
        this.resourceLayout = resource;
        this.dataSet = data;
        this.mContext=context;
        this.queue = queue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        RecipeDataModel p = getItem(position);

        if (p != null) {
            ImageView recipeImage = v.findViewById(R.id.recipeImage);
            TextView recipeTitle = v.findViewById(R.id.recipeTitle);
            TextView recipeCategory = v.findViewById(R.id.recipeCategory);
            TextView recipePrepTime = v.findViewById(R.id.recipePrepTime);
            if (recipeImage != null) {
                recipeImage.setImageBitmap(p.getImage());
            }
            if (recipeTitle != null) {
                recipeTitle.setText(p.getName());
            }
            if (recipeCategory != null) {
                recipeCategory.setText(p.getCategory());
            }
            if (recipePrepTime != null) {
                recipePrepTime.setText(p.getPreperationTime());
            }
        }

        return v;
    }

}
