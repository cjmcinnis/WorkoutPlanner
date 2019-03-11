package com.cmcinnis.craig.workoutplanner.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;
/*
 * Dao for both workout and associated exercises
 */
@Dao
abstract class WorkoutDao {
    @Insert
    abstract void insertWorkout(Workout workout);

    @Insert
    abstract void insertExercises(List<Exercise> exercises);

    @Query("DELETE FROM workout_table")
    abstract void deleteWorkouts();

    @Query("DELETE FROM exercise_table")
    abstract void deleteExercises();

    @Query("SELECT * FROM exercise_table WHERE mWorkoutId =:workoutId")
    abstract LiveData<List<Exercise>> getExercises(int workoutId);

    @Query("SELECT * FROM workout_table WHERE mId =:workoutId")
    abstract LiveData<Workout> getWorkout(int workoutId);

    @Query("SELECT * FROM workout_table")
    abstract LiveData<List<Workout>> getAllWorkouts();

    /*
     * Insert all exercises for a given workout
     */
    public void insertWorkoutWithExercises(Workout workout){
        List<Exercise> exercises = workout.exercises;
        for (Exercise exercise : exercises)
        {
            exercise.setWorkoutId(workout.getId());
        }
        insertWorkout(workout);
        insertExercises(exercises);
    }

}
