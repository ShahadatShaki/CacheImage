/*
 * Copyright (c) 2018.
 * Sadiq Md. Asif.
 * asifsadiqmd@gmail.com
 *
 * All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 * the property of Sadiq Md. Asif.
 * The intellectual and technical concepts contained
 * herein are proprietary to Sadiq Md. Asif and are protected by copyright law.
 * Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Sadiq Md. Asif.
 *
 *
 *
 */

package com.shaki.image_cache;

import android.content.Context;
import android.content.SharedPreferences;


class SharedPref {
    private static final String SETTINGS_NAME = "Cache-image_default_settings";
    private static SharedPref sSharedPrefs;
    private static SharedPreferences mPref;
    private static SharedPreferences.Editor mEditor;
    private static boolean mBulkUpdate = false;


    private SharedPref(Context context) {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static void initSharedPref(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new SharedPref(context.getApplicationContext());
        }
    }

    public static SharedPref getInstance() {
        if (sSharedPrefs != null) {
            return sSharedPrefs;
        }

        //Option 1:
        throw new IllegalArgumentException("Should use ImageCache.setupCacheImage(context, 1000 * 60 * 5) at least once before using this method.");

        //Option 2:
        // Alternatively, you can create a new instance here
        // with something like this:
        // getInstance(MyCustomApplication.getContext());
    }

    public static void put(Key key, String val) {
        doEdit();
        mEditor.putString(key.name(), val);
        doCommit();
    }

    private static void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private static void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }

    public static void put(Key key, int val) {
        doEdit();
        mEditor.putInt(key.name(), val);
        doCommit();
    }

    public static void put(String key, int val) {
        doEdit();
        mEditor.putInt(key, val);
        doCommit();
    }

    public static void put(Key key, boolean val) {
        doEdit();
        mEditor.putBoolean(key.name(), val);
        doCommit();
    }

    public static void put(Key key, float val) {
        doEdit();
        mEditor.putFloat(key.name(), val);
        doCommit();
    }

    /**
     * Convenience method for storing doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The enum of the preference to store.
     * @param val The new value for the preference.
     */
    public static void put(Key key, double val) {
        doEdit();
        mEditor.putString(key.name(), String.valueOf(val));
        doCommit();
    }

    public static void put(Key key, long val) {
        doEdit();
        mEditor.putLong(key.name(), val);
        doCommit();
    }

    public static String getString(Key key, String defaultValue) {
        return mPref.getString(key.name(), defaultValue);
    }

    public static String getString(Key key) {
        return mPref.getString(key.name(), "");
    }

    public static int getInt(Key key) {
        return mPref.getInt(key.name(), 0);
    }

    public static int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    public static int getInt(Key key, int defaultValue) {
        return mPref.getInt(key.name(), defaultValue);
    }

    public static long getLong(Key key) {
        return mPref.getLong(key.name(), 0);
    }

    public static long getLong(Key key, long defaultValue) {
        return mPref.getLong(key.name(), defaultValue);
    }

    public static float getFloat(Key key) {
        return mPref.getFloat(key.name(), 0);
    }

    public static float getFloat(Key key, float defaultValue) {
        return mPref.getFloat(key.name(), defaultValue);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The enum of the preference to fetch.
     */
    public static double getDouble(Key key) {
        return getDouble(key, 0);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The enum of the preference to fetch.
     */
    public static double getDouble(Key key, double defaultValue) {
        try {
            return Double.valueOf(mPref.getString(key.name(), String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(Key key, boolean defaultValue) {
        return mPref.getBoolean(key.name(), defaultValue);
    }

    public static boolean getBoolean(Key key) {
        return mPref.getBoolean(key.name(), false);
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    public static void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    /**
     * Remove keys from SharedPreferences.
     *
     * @param keys The enum of the key(s) to be removed.
     */
    public void remove(Key... keys) {
        doEdit();
        for (Key key : keys) {
            mEditor.remove(key.name());
        }
        doCommit();
    }

    /**
     * Enum representing your setting names or key for your setting.
     */

    public void edit() {
        mBulkUpdate = true;
        mEditor = mPref.edit();
    }

    public void commit() {
        mBulkUpdate = false;
        mEditor.commit();
        mEditor = null;
    }

    public enum Key {
        LIFETIME, PLACEHOLDER

    }


}
