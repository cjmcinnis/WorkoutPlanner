package com.cmcinnis.craig.workoutplanner;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;
import com.cmcinnis.craig.workoutplanner.Database.Workout;
import com.cmcinnis.craig.workoutplanner.Models.WorkoutViewModel;

import java.util.List;

public class WorkoutFragment extends Fragment {
    private static final String TAG = "WorkoutFragment";

    private WorkoutViewModel mWorkoutViewModel;
    private Workout mWorkout;

    /*
     * Creates an instance of this fragment with a supplied workout Id
     */
    public static WorkoutFragment newInstance(long workoutId)
    {
        Bundle args = new Bundle();
        args.putLong(WorkoutListFragment.WORKOUT_ID_REQUEST, workoutId);

        WorkoutFragment fragment = new WorkoutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        //get workout ID from passed in ID
        long workoutId = getArguments().getLong(WorkoutListFragment.WORKOUT_ID_REQUEST);
        if(workoutId == -1)
        {
            Log.e(TAG, "Passed in null workout ID, this should never happen");
        }

        //temporarily setup our Workout as a blank one; it will update to real workout once the
        //the observer is setup
        mWorkout = new Workout("");
        setupObservers(workoutId);

        updateUI();

        return view;
    }

    /*
     * Setup an observer that will watch whenever a change it made to the workout and update the UI
     * accordingly
     */
    private void setupObservers(long workoutId){
        //monitor whenever a change is made to our workout and update the object
        mWorkoutViewModel.getWorkout(workoutId).observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                mWorkout = workout;
                Log.d(TAG, "Setting workout to: " + workout.getWorkoutName());
            }
        });

        //monitor whenever a change is made to our workout's exercises and update the object
        mWorkoutViewModel.getExercises(workoutId).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                mWorkout.setExercises(exercises);
                Log.d(TAG, "Setting workout exercise to list of size: " + mWorkout.getExercises().size());
            }
        });
    }

    private void updateUI(){

    }
}
