package com.example.keepgoing.Class;

import java.io.Serializable;
import java.util.ArrayList;

public class Plan implements Serializable {
    private String PlanName;
    private String Date;
    private String Day;
    private ArrayList<Exercise> exercisers;
    public Plan(){}
    public Plan(String planName, String date, String day) {
        exercisers = new ArrayList<>();
        PlanName = planName;
        Date = date;
        Day = day;
    }
    public String getPlanName() {return PlanName;}
    public void setPlanName(String planName) {
        PlanName = planName;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
    public String getDay() {
        return Day;
    }
    public void setDay(String day) {
        Day = day;
    }
    public ArrayList<Exercise> getExercises() {
        return exercisers;
    }
    public void setExercises(ArrayList<Exercise> exercisers) {
        this.exercisers = exercisers;
    }
}
