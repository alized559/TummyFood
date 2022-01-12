package com.example.tummyfood.models;

import android.graphics.Bitmap;

public class CategoriesDataModel {

    private Bitmap image;
    private int id = 1;
    private String title;

    public CategoriesDataModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {return id;}

}
