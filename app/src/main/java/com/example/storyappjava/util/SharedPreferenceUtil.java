package com.example.storyappjava.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    public static final String PREF_KEY = "PREF_KEY";
    public static final String PREF_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String PREF_IS_ALREADY_HAVE_ACCOUNT = "IS_ALREADY_HAVE_ACCOUNT";

    private final Context context;

    public SharedPreferenceUtil(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreference() {
        return context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    public Boolean isAlreadyHaveAccount() {
        SharedPreferences pref = getSharedPreference();
        return pref.getBoolean(PREF_IS_ALREADY_HAVE_ACCOUNT, false);
    }

    public void setAlreadyHaveAccount(Boolean value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(PREF_IS_ALREADY_HAVE_ACCOUNT, value);
        editor.apply();
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
