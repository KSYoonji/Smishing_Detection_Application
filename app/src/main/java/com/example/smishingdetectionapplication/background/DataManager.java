package com.example.smishingdetectionapplication.background;

import android.content.Context;
import android.content.SharedPreferences;

public class DataManager {

    public static void putDataString(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences("SMSCAPTUREHELPER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putDataInt(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences("SMSCAPTUREHELPER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static String getDataString(Context context, String key, String def) {
        SharedPreferences prefs = context.getSharedPreferences("SMSCAPTUREHELPER", Context.MODE_PRIVATE);
        return prefs.getString(key, def);
    }

    public static int getDataInt(Context context, String key, int def) {
        SharedPreferences prefs = context.getSharedPreferences("SMSCAPTUREHELPER", Context.MODE_PRIVATE);
        return prefs.getInt(key, def);
    }

}
