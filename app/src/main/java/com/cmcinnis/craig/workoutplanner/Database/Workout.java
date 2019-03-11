package com.cmcinnis.craig.workoutplanner.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;

import java.util.List;
import java.util.UUID;

@Entity(tableName = "workout_table")
public class Workout{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long mId;

    private String mWorkoutName;

    //List of all exercises associated with workout
    @Ignore
    public List<Exercise> exercises;

    public Workout(String workoutName){
        mWorkoutName = workoutName;
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
