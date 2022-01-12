package com.example.tummyfood.helpers;

public class ServerUrls {

    public static String getRecipes(String category){
        return "https://tummyfood2.000webhostapp.com/getRecipes.php?category=" + category;
    }

    public static String getTrendingRecipes() {
        return "https://tummyfood2.000webhostapp.com/getRecipes.php";
    }

    public static String getImage(int recipeId) {
        return "https://tummyfood2.000webhostapp.com/getImage.php?id=" + recipeId;
    }

    public static String getRecipeDetails(int recipeId) {
        return "https://tummyfood2.000webhostapp.com/getRecipeDetails.php?id=" + recipeId;
    }

    public static String createRecipe = "https://tummyfood2.000webhostapp.com/createRecipe.php";

    public static String authenticate = "https://tummyfood2.000webhostapp.com/authenticate.php";

    public static String getUserLikes() {
        return "https://tummyfood2.000webhostapp.com/getLikes.php?userID=" + UserLogin.CurrentLoginID;
    }

    public static String userLikes = "https://tummyfood2.000webhostapp.com/userLikes.php";

    public static String getAllLikes(int userID) {
        return "https://tummyfood2.000webhostapp.com/getAllLikes.php?userID=" + userID;
    }

    public static String getCategories = "https://tummyfood2.000webhostapp.com/getCategories.php";

    public static String getCategoryImage(int categoryId) {
        return "https://tummyfood2.000webhostapp.com/getCategoryImage.php?id=" + categoryId;
    }

}
