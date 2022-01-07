package com.example.tummyfood.helpers;

import android.util.Log;

import com.android.volley.toolbox.StringRequest;

public class ServerUrls {

    public static String GetRecipes(String category){
        return "http://toxicscripts.com/college/getRecipes.php?category=" + category;
    }

    public static String GetTrendingRecipes(){
        return "http://toxicscripts.com/college/getRecipes.php";
    }

    public static String GetImage(int recipe){
        return "http://toxicscripts.com/college/getImage.php?id=" + recipe;
    }

    public static String getRecipeDetails(int recipeId) {
        return "http://toxicscripts.com/college/getRecipeDetails.php?id=" + recipeId;
    }

    public static String CreateRecipe = "http://toxicscripts.com/college/createRecipe.php";

    public static String Authenticate = "http://toxicscripts.com/college/authenticate.php";

    public static String GetUserLikes(){
        return "http://toxicscripts.com/college/getLikes.php?userID=" + UserLogin.CurrentLoginID;
    }

    public static String UserLikes = "http://toxicscripts.com/college/userLikes.php";

}
