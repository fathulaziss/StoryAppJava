package com.example.storyappjava.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    public static final String PREF_KEY = "PREF_KEY";
    public static final String PREF_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String PREF_IS_ALREADY_HAVE_ACCOUNT = "IS_ALREADY_HAVE_ACCOUNT";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPreferenceUtil(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public Boolean isAlreadyHaveAccount() {
        return sharedPreferences.getBoolean(PREF_IS_ALREADY_HAVE_ACCOUNT, false);
    }

    public void setAlreadyHaveAccount(Boolean value) {
        editor.putBoolean(PREF_IS_ALREADY_HAVE_ACCOUNT, value);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(PREF_ACCESS_TOKEN, "");
    }

    public void setToken(String token) {
        editor.putString(PREF_ACCESS_TOKEN, token);
        editor.apply();
    }
}
