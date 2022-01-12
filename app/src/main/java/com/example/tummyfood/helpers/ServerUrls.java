package com.example.tummyfood.helpers;

public class ServerUrls {

    private static String WebsiteUrl = "http://toxicscripts.com/college/";

    public static String getRecipes(String category){
        return WebsiteUrl + "getRecipes.php?category=" + category;
    }

    public static String getTrendingRecipes() {
        return WebsiteUrl + "getRecipes.php";
    }

    public static String getImage(int recipeId) {
        return WebsiteUrl + "getImage.php?id=" + recipeId;
    }

    public static String getRecipeDetails(int recipeId) {
        return WebsiteUrl + "getRecipeDetails.php?id=" + recipeId;
    }

    public static String createRecipe = WebsiteUrl + "createRecipe.php";

    public static String authenticate = WebsiteUrl + "authenticate.php";

    public static String getUserLikes() {
        return WebsiteUrl + "getLikes.php?userID=" + UserLogin.CurrentLoginID;
    }

    public static String userLikes = WebsiteUrl + "userLikes.php";

    public static String getAllLikes(int userID) {
        return WebsiteUrl + "getAllLikes.php?userID=" + userID;
    }

    public static String getCategories = WebsiteUrl + "getCategories.php";

    public static String getCategoryImage(int categoryId) {
        return WebsiteUrl + "getCategoryImage.php?id=" + categoryId;
    }

    public static String createCategory = WebsiteUrl + "createCategory.php";

    public static String deleteCategory = WebsiteUrl + "deleteCategory.php";

    public static String deleteRecipe = WebsiteUrl + "deleteRecipe.php";

}
