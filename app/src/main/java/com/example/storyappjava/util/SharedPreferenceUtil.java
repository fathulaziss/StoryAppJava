package com.example.storyappjava.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    public static final String PREF_KEY = "PREF_KEY";
    public static final String PREF_ACCESS_TOKEN = "ACCESS_TOKEN";

    private final Context context;

    public SharedPreferenceUtil(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreference() {
        return context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    public String getToken() {
        SharedPreferences pref = getSharedPreference();
        return pref.getString(PREF_ACCESS_TOKEN, "");
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(PREF_ACCESS_TOKEN, token);
        editor.apply();
    }
}
