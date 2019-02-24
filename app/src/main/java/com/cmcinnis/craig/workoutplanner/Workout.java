package com.cmcinnis.craig.workoutplanner;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

@Entity
public class Workout{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name= "id")
    private UUID mId;

    @ColumnInfo(name= "name")
    private String mWorkoutName;

    @ColumnInfo(name = "exercise_list")
    private List<Exercise> mExerciseList;

    public Workout(String workoutName){
        mWorkoutName = workoutName;
    }

    public UUID getId(){ return this.mId; }

    public String getWorkoutName() {
        return this.mWorkoutName;
    }


    public void setWorkoutName(String workoutName) {
        mWorkoutName = workoutName;
    }

}
