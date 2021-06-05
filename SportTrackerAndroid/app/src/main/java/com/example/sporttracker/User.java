package com.example.sporttracker;

import java.util.Vector;

public class User {

    static class Lokacija{
        double lat;
        double lon;
    }

    String username;
    String email;


    String pathToVideo;
    int age;
    double heartRate, height, weight, burnedCalories;
    Vector<Lokacija> pot;



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

    void addLocation(Lokacija l){
        pot.add(l);
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


}