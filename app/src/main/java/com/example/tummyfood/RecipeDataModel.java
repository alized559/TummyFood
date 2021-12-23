package com.example.tummyfood;

public class RecipeDataModel {

    int id = 1;
    String name = "New Recipe";
    String category = "New Category";
    int prepTime = 40;
    String imageLink = "https://i.imgur.com/897VeyK.jpeg";

    public RecipeDataModel(int id, String name, String category, int prepTime, String imageLink){
        this.id = id;
        this.name = name;
        this.category = category;
        this.prepTime = prepTime;
        this.imageLink = imageLink;
    }

    public RecipeDataModel(int id, String name, String category, int prepTime){
        this.id = id;
        this.name = name;
        this.category = category;
        this.prepTime = prepTime;
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

    public String getImageLink(){
        return imageLink;
    }

    public String getPreperationTime(){
        return prepTime + " Mins";
    }

}
