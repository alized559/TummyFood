package com.example.tummyfood.helpers;

import android.graphics.Bitmap;

import java.util.HashMap;

public class ImageCache {

    static HashMap<Integer, Bitmap> cache = new HashMap<>();

    static HashMap<Integer, Bitmap> categoriesCache = new HashMap<>();

    public static void SetImage(int id, Bitmap image){
        cache.put(id, image);
    }

    public static Bitmap GetImage(int id) {
        if (cache.containsKey(id)){
            return cache.get(id);
        }
        return null;
    }

    public static void SetCategoriesImage(int id, Bitmap image){
        categoriesCache.put(id, image);
    }

    public static Bitmap GetCategoriesImage(int id) {
        if (categoriesCache.containsKey(id)){
            return categoriesCache.get(id);
        }
        return null;
    }

}
