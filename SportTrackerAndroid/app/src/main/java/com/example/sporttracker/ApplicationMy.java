package com.example.sporttracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class ApplicationMy extends Application {
    public static final String APP_ID = "ID_KEY";


    public static final String HEIGHT = "HEIGHT";
    public static final String WEIGHT = "WEIGHT";
    public static final String USERNAME = "USERNAME";
    public static final String AGE = "AGE";

    SharedPreferences sp;
    String appUUID;

    User thisUser;
    private String userID;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    public void onCreate() {
        super.onCreate();


        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //createUser();
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
    public void createUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userPRofile = snapshot.getValue(User.class);

                if(userPRofile != null){
                    thisUser = new User("username","email");
                    thisUser.setUsername(userPRofile.username);
                    thisUser.setEmail(userPRofile.email);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(USERNAME,userPRofile.username);
                    editor.apply();
                    System.out.println(userPRofile.username + userPRofile.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Something wrong happened!");
            }
        });


        try{
            float height = sp.getFloat(HEIGHT, (float) 1.60);
            float weight = sp.getFloat(WEIGHT, (float) 60);
            int age = sp.getInt(HEIGHT, 18);
            thisUser.setAge(age);
            thisUser.setHeight(height);
            thisUser.setWeight(weight);
            System.out.println(age + " . " + height);
        }catch (Exception e){
            System.out.println("Error");
        }




    }

    public void addLocation(User.Lokacija a){
        thisUser.addLocation(a);
    }
    public void setTime(int sec){
        thisUser.setTimeInSeconds(sec);
    }
    public void updateUser(){
        try{
            thisUser.setHeight(sp.getFloat(HEIGHT,0));
            thisUser.setWeight(sp.getFloat(WEIGHT,0));
            thisUser.setAge(sp.getInt(AGE,0));
        }catch (Exception e){
            System.out.println(e);
        }
        thisUser.addToFirebase();

    }

    private void onAddItem(){
        //CollectionReference restaurants = mFirestore.collection("restaurants");
    }

}
