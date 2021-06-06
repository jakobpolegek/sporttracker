package com.example.lib;

public class UserTest {
    String username;
    String pathToVideo;
    int age;
    double heartRate, height, weight, burnedCalories;

    //region Constructors
    public UserTest(String username, int age, double height, double weight) {
        this.username = username;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }
    //endregion

    //region Getter
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