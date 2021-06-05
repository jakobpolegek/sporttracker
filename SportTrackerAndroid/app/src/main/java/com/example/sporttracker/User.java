package com.example.sporttracker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class User {

    static class Lokacija{
        double lat;
        double lon;



        public Lokacija(double a, double b){
            lat = a;
            lon = b;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }

    String username;
    String email;


    String pathToVideo;
    int age;
    double heartRate, height, weight, burnedCalories;
    Vector<Lokacija> pot;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public User(){}


    //region Constructors
    public User(String username, String email){
        this.username = username;
        this.email = email;
        pot = new Vector<Lokacija>();
    }

    public User(String username, int age, double height, double weight) {
        this.username = username;
        this.age = age;
        this.height = height;
        this.weight = weight;
        pot = new Vector<Lokacija>();
    }
    //endregion

    void addLocation(Lokacija a){
        pot.add(a);
    }

    //region Getter


    public Vector<Lokacija> getPot() {
        return pot;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPathToVideo() {
        return pathToVideo;
    }

    public int getAge() {
        return age;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public double getBurnedCalories() {
        return burnedCalories;
    }
    //endregion

    //region Setter


    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPathToVideo(String pathToVideo) {
        this.pathToVideo = pathToVideo;
    }

    public void setHeartRate(double heartRate) {
        this.heartRate = heartRate;
    }

    public void setBurnedCalories(double burnedCalories) {
        this.burnedCalories = burnedCalories;
    }
    //endregion


    void addToFirebase(){
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("weight", weight);
        user.put("height", height);
        user.put("pot", pot);
        user.put("age", age);

// Add a new document with a generated ID
        db.collection("posts")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error adding document" + e);
                    }
                });

    }

    void getDoc(){
        DocumentReference docRef = db.collection("posts").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("DocumentSnapshot data: " + document.getData());

                    } else {
                        System.out.println( "No such document");
                    }
                } else {
                    System.out.println("get failed with " + task.getException());
                }
            }
        });

    }
}