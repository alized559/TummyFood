package com.example.tummyfood.models;

import android.graphics.Bitmap;

public class RecipeDataModel {

    int id = 1;
    String name = "New Recipe";
    String category = "New Category";
    int prepTime = 40;

    Bitmap currentImage = null;

    public RecipeDataModel(int id, String name, String category, int prepTime){
        this.id = id;
        this.name = name;
        this.category = category;
        this.prepTime = prepTime;
    }

    public void setImage(Bitmap image){
        this.currentImage = image;
    }

    public Bitmap getImage(){
        return currentImage;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPreperationTime(){
        return prepTime + " Mins";
    }

}
