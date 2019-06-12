package com.cmcinnis.craig.workoutplanner.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.util.StringUtil;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "workout_table")
public class Workout implements Parcelable {

    //Error msg constants thrown when validation fails
    private static final String VALIDATION_FAIL_NULL_NAME = "Workout name cannot be null";
    private static final String VALIDATION_FAIL_TOO_SHORT = "Workout name must be at least 3 characters";
    private static final String VALIDATION_FAIL_TOO_LONG = "Workout name must be shorter than 20 characters";
    private static final String VALIDATION_FAIL_INVALID_CHARACTERS = "Workout name must only contain Alphanumeric characters";


    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long mId;

    private String mWorkoutName;

    //List of all exercises associated with workout
    @Ignore
    private List<Exercise> mExercises;

    public Workout(String workoutName) {
        mWorkoutName = workoutName;
        mExercises = new ArrayList<>();
    }

    public List<Exercise> getExercises() {
        return mExercises;
    }

    public void addExercise(Exercise exercise) {
        mExercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        mExercises.remove(exercise);
    }

    /*
     * Removes exercise at given position
     */
    public void removeExercise(int i) {
        mExercises.remove(i);
    }

    public void setExercises(List<Exercise> exercises) {
        mExercises = exercises;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getWorkoutName() {
        return this.mWorkoutName;
    }


    public void setWorkoutName(String workoutName) {
        mWorkoutName = workoutName;
    }


    @Override
    public String toString() {
        return "Workout{" +
                "mId=" + mId +
                ", mWorkoutName" + mWorkoutName +
                '}';
    }

    /*
     * Validates information in a workout. If it fails validation then this returns a string with
     * error msg else returns null;
     */
    public static String validateWorkout(Workout workout)
    {
        //Workout name cannot be null
        if(workout.getWorkoutName() == null)
        {
            return VALIDATION_FAIL_NULL_NAME;
        }
        //Workout name must have a minimum of 3 characters
        else if (workout.getWorkoutName().length() < 4)
        {
            return VALIDATION_FAIL_TOO_SHORT;
        }
        //Workout name be shorter than 20 characters
        else if (workout.getWorkoutName().length() < 4)
        {
            return VALIDATION_FAIL_TOO_LONG;
        }
        //check string is only alphanumeric
        else if (workout.getWorkoutName().matches("^.*[^a-zA-Z0-9 ].*$"))
        {
            return VALIDATION_FAIL_INVALID_CHARACTERS;
        }

        return null;
    }

    /*
     * Implement parcelable methods to allow passing between activities/fragments
     */

    @Override
    public int describeContents() {
        return 0;
    }


    /*
     * Write our variables to the parcel
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mWorkoutName);
    }

    public static final Parcelable.Creator<Workout> CREATOR
            = new Parcelable.Creator<Workout>() {
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    /*
     * Cosntructor for implementing parcelable
     */
    private Workout(Parcel in) {
        mId = in.readLong();
        mWorkoutName = in.readString();
    }
}
