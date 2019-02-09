package com.cmcinnis.craig.workoutplanner;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

public class Workout extends BaseObservable{

    private String mWorkoutName;
    private List<Exercise> mExerciseList;

    public Workout(String workoutName){
        mWorkoutName = workoutName;
    }

    @Bindable
    public String getWorkoutName() {
        return mWorkoutName;
    }

    public void setWorkoutName(String workoutName) {
        mWorkoutName = workoutName;
    }

}
