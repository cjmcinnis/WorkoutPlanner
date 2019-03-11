package com.cmcinnis.craig.workoutplanner.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private static final String TAG= "WorkoutViewModel";
    private WorkoutRepository mWorkoutRepository;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        mWorkoutRepository = new WorkoutRepository(application);
    }

    LiveData<List<Workout>> getAllWorkouts() {return mWorkoutRepository.getAllWorkouts();}

    public void insertWorkoutWithExercises(Workout workout){
        // Check that the list is not empty
        if (workout.exercises == null)
        {
            //TODO: Add error msg to user?
            Log.e(TAG, "Cannot submit a workout with no exercises.");
        }else {
            mWorkoutRepository.insertWorkoutAndExercises(workout);
        }
    }
}
