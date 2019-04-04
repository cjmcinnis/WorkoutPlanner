package com.cmcinnis.craig.workoutplanner.Models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;
import com.cmcinnis.craig.workoutplanner.Database.Workout;
import com.cmcinnis.craig.workoutplanner.Database.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private static final String TAG= "WorkoutViewModel";
    private WorkoutRepository mWorkoutRepository;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        mWorkoutRepository = new WorkoutRepository(application);
    }

    public LiveData<Workout> getWorkout(long workoutId){ return mWorkoutRepository.getWorkout(workoutId);}

    public LiveData<List<Workout>> getAllWorkouts() {return mWorkoutRepository.getAllWorkouts();}

    public LiveData<List<Exercise>> getExercises(long workoutId) { return mWorkoutRepository.getExercises(workoutId);}

    public void insertWorkoutWithExercises(Workout workout){
        // Check that the list is not empty
        if (workout.getExercises().size() == 0)
        {
            //TODO: Add error msg to user?
            Log.e(TAG, "Cannot submit a workout with no exercises.");
        }else {
            mWorkoutRepository.insertWorkoutAndExercises(workout);
        }
    }

    public void insertExercise(Exercise exercise){
        mWorkoutRepository.insertExercise(exercise);
    }

}
