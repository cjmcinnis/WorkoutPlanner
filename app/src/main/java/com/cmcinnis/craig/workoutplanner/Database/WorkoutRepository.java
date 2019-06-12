package com.cmcinnis.craig.workoutplanner.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WorkoutRepository {
    private WorkoutDao mWorkoutDao;
    private LiveData<List<Workout>> mAllWorkouts;

    public WorkoutRepository (Application application){
        WorkoutRoomDatabase db = WorkoutRoomDatabase.getDatabase(application);
        mWorkoutDao = db.workoutDao();
        mAllWorkouts = mWorkoutDao.getAllWorkouts();
    }

    public LiveData<List<Workout>> getAllWorkouts(){
        return mWorkoutDao.getAllWorkouts();
    }

    public LiveData<Workout> getWorkout(long workoutId) { return mWorkoutDao.getWorkout(workoutId); }
    public LiveData<List<Exercise>> getExercises(long workoutId) { return mWorkoutDao.getExercises(workoutId); }

    //Inset or updates an exercise
    public void insertExercise(Exercise exercise){
        new insertExerciseAsyncTask(mWorkoutDao).execute(exercise);
    }

    public void insertWorkoutAndExercises(Workout workout){
        new insertAsyncTask(mWorkoutDao).execute(workout);
    }

    /*
     * Asynctask for inserting a workout with all its exercises
     */
    private static class insertAsyncTask extends AsyncTask<Workout, Void, Void>{
        private WorkoutDao mAsyncTaskDao;

        insertAsyncTask(WorkoutDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Workout... workouts) {
            mAsyncTaskDao.insertWorkoutWithExercises(workouts[0]);
            return null;
        }
    }

    /*
     * Asynctask for inserting a lone exercise
     */
    private static class insertExerciseAsyncTask extends AsyncTask<Exercise, Void, Void>{
        private WorkoutDao mAsyncTaskDao;

        insertExerciseAsyncTask(WorkoutDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            mAsyncTaskDao.insertExercise(exercises[0]);
            return null;
        }
    }

}
