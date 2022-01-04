package com.example.tummyfood;

import android.graphics.Bitmap;

import java.util.HashMap;

public class ImageCache {

    static HashMap<Integer, Bitmap> Cache = new HashMap<Integer, Bitmap>();

    public static void SetImage(int id, Bitmap image){
        Cache.put(id, image);
    }

    public static Bitmap GetImage(int id){
        if(Cache.containsKey(id)){
            return Cache.get(id);
        }
        return null;
    }

}
