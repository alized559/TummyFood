package com.example.tummyfood;

public class ServerUrls {

    public static String GetRecipe = "http://toxicscripts.com/college/getRecipes.php";

    public static String GetImage(int recipe){
        return "http://toxicscripts.com/college/getImage.php?id=" + recipe;
    }

    public static String getRecipeDetails(int recipeId) {
        return "http://toxicscripts.com/college/getRecipeDetails.php?id=" + recipeId;
    }

}
