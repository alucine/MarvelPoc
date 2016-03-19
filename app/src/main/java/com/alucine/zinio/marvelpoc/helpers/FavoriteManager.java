package com.alucine.zinio.marvelpoc.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteManager {
    public static final String PREFS_NAME = "MARVEL";
    public static final String FAVORITES = "FAVORITES";

    private static void storeFavorites(Context context, List favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.commit();
    }

    public static ArrayList loadFavorites(Context context) {
        SharedPreferences settings;
        List favorites;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites,String[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return null;
        return (ArrayList) favorites;
    }

    public static void addFavorite(Context context, String favorite) {
        List favorites = loadFavorites(context);
        if (favorites == null)
            favorites = new ArrayList();
        favorites.add(favorite);
        storeFavorites(context, favorites);
    }

    public static void removeFavorite(Context context, String favorite) {
        ArrayList favorites = loadFavorites(context);
        if (favorites != null) {
            favorites.remove(favorite);
            storeFavorites(context, favorites);
        }
    }
}
