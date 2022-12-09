package com.example.keepgoing.Class;

import java.io.Serializable;

public class ExerciseSet implements Serializable {
    private int SetNumber;
    private int Weight;
    private int Rest;
    private int Reps;
    private String image = "";
    private boolean Finish = false;
    private boolean Play = false;
    public ExerciseSet() { }
    public ExerciseSet(int setNumber, int weight, int rest, int reps) {
        SetNumber = setNumber;
        Weight = weight;
        Rest = rest;
        Reps = reps;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public boolean isPlay() {
        return Play;
    }
    public void setPlay(boolean play) {
        Play = play;
    }
    public boolean isFinish() {
        return Finish;
    }
    public void setFinish(boolean finish) {
        Finish = finish;
    }
    public int getReps() {
        return Reps;
    }
    public void setReps(int reps) {
        Reps = reps;
    }
    public int getSetNumber() {
        return SetNumber;
    }
    public void setSetNumber(int setNumber) {
        SetNumber = setNumber;
    }
    public int getWeight() {
        return Weight;
    }
    public void setWeight(int weight) {
        Weight = weight;
    }
    public int getRest() {
        return Rest;
    }
    public void setRest(int rest) {
        Rest = rest;
    }
}
