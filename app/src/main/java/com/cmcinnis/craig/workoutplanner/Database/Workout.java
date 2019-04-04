package com.cmcinnis.craig.workoutplanner.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "workout_table")
public class Workout{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long mId;

    private String mWorkoutName;

    //List of all exercises associated with workout
    @Ignore
    private List<Exercise> mExercises;

    public Workout(String workoutName){
        mWorkoutName = workoutName;
        mExercises = new ArrayList<>();
    }

    public List<Exercise> getExercises(){
        return mExercises;
    }

    public void addExercise(Exercise exercise){
        mExercises.add(exercise);
    }

    public void removeExercise(Exercise exercise)
    {
        mExercises.remove(exercise);
    }

    /*
     * Removes exercise at given position
     */
    public void removeExercise(int i)
    {
        mExercises.remove(i);
    }

    public void setExercises(List<Exercise> exercises){
        mExercises = exercises;
    }

    public long getId(){ return this.mId; }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getWorkoutName() {
        return this.mWorkoutName;
    }


    public void setWorkoutName(String workoutName) {
        mWorkoutName = workoutName;
    }


}
