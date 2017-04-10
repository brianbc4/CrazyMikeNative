package com.crazymike.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;

import java.lang.reflect.Type;

public class PreferencesTool {

    private static final String NAME = "Tokoten";

    private static SharedPreferences preferences;
    private static PreferencesTool instance;

    private PreferencesTool(Context context) {
        preferences = context.getSharedPreferences(NAME, 0);
    }

    public static PreferencesTool getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new PreferencesTool(context);
    }

    public <T> T get(PreferencesKey key, Class<T> clazz) {
        clazz = Primitives.wrap(clazz);
        if (clazz.equals(String.class)) {
            return clazz.cast(preferences.getString(key.name(), ""));
        } else if (clazz.equals(Boolean.class)) {
            return clazz.cast(preferences.getBoolean(key.name(), false));
        } else if (clazz.equals(Integer.class)) {
            return clazz.cast(preferences.getInt(key.name(), 0));
        } else if (clazz.equals(Float.class)) {
            return clazz.cast(preferences.getFloat(key.name(), 0.0f));
        } else if (clazz.equals(Long.class)) {
            return clazz.cast(preferences.getLong(key.name(), 0));
        } else {
            return new Gson().fromJson(preferences.getString(key.name(), ""), clazz);
        }
    }

    public  <T> T get( PreferencesKey key, Type type) {
        return new Gson().fromJson(preferences.getString(key.name(), ""), type);
    }

    public void put(PreferencesKey key, Object object) {
        if (object.getClass().equals(String.class)) {
            preferences.edit().putString(key.name(), (String) object).apply();
        } else if (object.getClass().equals(Boolean.class)) {
            preferences.edit().putBoolean(key.name(), (boolean) object).apply();
        } else if (object.getClass().equals(Integer.class)) {
            preferences.edit().putInt(key.name(), (int) object).apply();
        } else if (object.getClass().equals(Float.class)) {
            preferences.edit().putFloat(key.name(), (float) object).apply();
        } else if (object.getClass().equals(Long.class)) {
            preferences.edit().putLong(key.name(), (long) object).apply();
        } else {
            preferences.edit().putString(key.name(), new Gson().toJson(object)).apply();
        }
    }


    public void clear() {
        preferences.edit().clear().apply();
    }
}