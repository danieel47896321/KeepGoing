package com.example.keepgoing.Class;

import java.io.Serializable;
import java.util.ArrayList;

public class Exercise implements Serializable {
    private String Exercise;
    private String Description;
    private String MuscleType;
    private ArrayList<ExerciseSet> Sets;
    private String Image = "";
    public Exercise() { Sets = new ArrayList<>(); }
    public Exercise(String exercise, String description, String muscleType, String image, int reps) {
        Sets = new ArrayList<>();
        for(int i=0;i<reps;i++)
            Sets.add(new ExerciseSet(i,10,30,8));
        MuscleType = muscleType;
        Exercise = exercise;
        Description = description;
        Image = image;
    }
    public String getMuscleType() {
        return MuscleType;
    }
    public void setMuscleType(String muscleType) {
        MuscleType = muscleType;
    }
    public ArrayList<ExerciseSet> getSets() {
        return Sets;
    }
    public void setSets(ArrayList<ExerciseSet> sets) {
        Sets = sets;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }
    public String getExercise() {
        return Exercise;
    }
    public void setExercise(String exercise) {
        Exercise = exercise;
    }
    public String getImage() {
        return Image;
    }
    public void setImage(String image) {
        Image = image;
    }
}
