package com.example.sporttracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

public class ApplicationMy extends Application {
    public static final String APP_ID = "ID_KEY";

    SharedPreferences sp;
    String appUUID;

    @Override
    public void onCreate() {
        super.onCreate();

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        getUUID();
    }

    public void getUUID(){
        if (sp.contains(APP_ID))
            appUUID = sp.getString(APP_ID,"DEFAULT VALUE ");
        else { //FIRST TIME GENERATE ID AND SAVE IT
            appUUID = UUID.randomUUID().toString().replace("-", "");
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(APP_ID,appUUID);
            editor.apply();
        }
    }

    private void onAddItem(){
        //CollectionReference restaurants = mFirestore.collection("restaurants");
    }

}
