package com.cmcinnis.craig.workoutplanner.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

/*
 * Representrs an exercise object. Each of our Workouts can contain any number of workouts which
 * contain their data representing name, # of sets, etc
 */
@Entity(tableName = "exercise_table")
public class Exercise implements Parcelable {
    private static final int DEFAULT_REST = 90;
    public static final int MIN_REST = 0;
    public static final int MAX_REST = 120; //multiplied by REST_INCREMENT for max value
    public static final int REST_INCREMENT = 5; //used for changing step value in numberpickers
    public static final int MIN_REPS = 1;
    public static final int MAX_REPS = 20;
    public static final int MIN_SETS = 1;
    public static final int MAX_SETS = 10;
    private static final int MIN_NAME_LENGTH = 3; //Minimum length for exercise name
    private static final int MAX_NAME_LENGTH = 18; //Minimum length for exercise name

    @PrimaryKey(autoGenerate = true)
    private long mExerciseId;

    private long mWorkoutId; //ID corresponding to the workout

    @NonNull
    private String mExerciseName;
    private int mReps;
    private int mSets;
    private int mRestDuration;

    //private long mTotalDuration; //lifetime time under tension
    //private int mNumberOfWorkouts; //total number of times this exercise has been performed

    //Don't use this ever (only here to allow builder class in an entity)
    Exercise(){}

    /*
     * Constructor used for builder pattern
     */
    private Exercise(final Builder builder){
        mExerciseName = builder.mExerciseName;
        mReps = builder.mReps;
        mSets = builder.mSets;
        mRestDuration = builder.mRestDuration;
        mWorkoutId = builder.mWorkoutId;
    }

    /*
     * We check if two exercises are the same by their unique exercise ID
     */
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Exercise)) return false;
        Exercise o = (Exercise) obj;
        return o.mExerciseId == this.mExerciseId;
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

    @Override
    public String toString() {
        return "Exercise{" +
                "mExerciseId=" + mExerciseId +
                ", mWorkoutId=" + mWorkoutId +
                ", mExerciseName='" + mExerciseName + '\'' +
                ", mReps=" + mReps +
                ", mSets=" + mSets +
                ", mRestDuration=" + mRestDuration +
                '}';
    }

    /*
     * Validates that the input is a valid exercise name
     */
    public static boolean validateExerciseName(CharSequence input){
        return (!TextUtils.isEmpty(input) && input.length() >= MIN_NAME_LENGTH &&
                input.length() <= MAX_NAME_LENGTH );
    }

    /*
     * Use builder pattern to build our exercises
     */
    public static class Builder{
        private String mExerciseName;
        private int mReps;
        private int mSets;
        private int mRestDuration;
        private long mWorkoutId;

        public Builder(String name, int sets, int reps){
            mReps = reps;
            mExerciseName = name;
            mSets = sets;
            mRestDuration = DEFAULT_REST;
            mWorkoutId = -1;
        }

        public Builder setRestTime(int restTime){
            this.mRestDuration = restTime;
            return this;
        }

        public Builder setWorkoutId(long workoutId){
            this.mWorkoutId = workoutId;
            return this;
        }

        public Exercise build(){
            return new Exercise(this);
        }
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
        parcel.writeLong(mExerciseId);
        parcel.writeLong(mWorkoutId);
        parcel.writeString(mExerciseName);
        parcel.writeInt(mReps);
        parcel.writeInt(mRestDuration);
        parcel.writeInt(mSets);


    }

    public static final Parcelable.Creator<Exercise> CREATOR
            = new Parcelable.Creator<Exercise>() {
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    /*
     * Cosntructor for implement parcelable
     */
    private Exercise(Parcel in) {
        mExerciseId = in.readLong();
        mWorkoutId = in.readLong();
        mExerciseName = in.readString();
        mReps = in.readInt();
        mRestDuration = in.readInt();
        mSets = in.readInt();
    }
}
