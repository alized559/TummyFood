package com.example.tummyfood;

public class CategoriesDataModel {

    private int image;
    private String title;

    public CategoriesDataModel(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

}
