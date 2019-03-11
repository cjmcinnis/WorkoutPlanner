package com.cmcinnis.craig.workoutplanner.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/*
 * Representrs an exercise object. Each of our Workouts can contain any number of workouts which
 * contain their data representing name, # of sets, etc
 */
@Entity(tableName = "exercise_table")
public class Exercise {
    private static final int DEFAULT_REST = 90;

    @PrimaryKey(autoGenerate = true)
    private long mExerciseId;

    private long mWorkoutId; //ID corresponding to the workout
    private String mExerciseName;
    private int mReps;
    private int mSets;
    private int mRestDuration;

    //private long mTotalDuration; //lifetime time under tension
    //private int mNumberOfWorkouts; //total number of times this exercise has been performed

    //Don't use this
    Exercise(){}

    /*
     * Constructor used for builder pattern
     */
    private Exercise(final Builder builder){
        mExerciseName = builder.mExerciseName;
        mReps = builder.mReps;
        mSets = builder.mSets;
        mRestDuration = builder.mRestDuration;
    }

    public long getWorkoutId() {
        return mWorkoutId;
    }

    public long getExerciseId() {
        return mExerciseId;
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public int getReps() {
        return mReps;
    }

    public int getSets() {
        return mSets;
    }

    public int getRestDuration() {
        return mRestDuration;
    }

    public void setExerciseId(long mExerciseId) {
        this.mExerciseId = mExerciseId;
    }

    public void setExerciseName(String mExerciseName) {
        this.mExerciseName = mExerciseName;
    }

    public void setReps(int mReps) {
        this.mReps = mReps;
    }

    public void setSets(int mSets) {
        this.mSets = mSets;
    }

    public void setRestDuration(int mRestDuration) {
        this.mRestDuration = mRestDuration;
    }

    public void setWorkoutId(long mWorkoutId) {
        this.mWorkoutId = mWorkoutId;
    }

    /*
     * Use builder pattern to build our exercises
     */
    public static class Builder{
        private String mExerciseName;
        private int mReps;
        private int mSets;
        private int mRestDuration;

        public Builder(String name, int sets, int reps){
            mReps = reps;
            mExerciseName = name;
            mSets = sets;
            mRestDuration = DEFAULT_REST;
        }

        public Builder setRestTime(int restTime){
            this.mRestDuration = restTime;
            return this;
        }

        public Exercise build(){
            return new Exercise(this);
        }
    }

}
