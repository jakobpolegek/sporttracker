package com.example.sporttracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ApplicationMy extends Application {
    public static final String APP_ID = "ID_KEY";

    SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

}
