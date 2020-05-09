package com.cmcinnis.craig.workoutplanner;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmcinnis.craig.workoutplanner.database.Exercise;
import com.cmcinnis.craig.workoutplanner.database.Workout;
import com.cmcinnis.craig.workoutplanner.models.WorkoutViewModel;
import com.cmcinnis.craig.workoutplanner.databinding.FragmentWorkoutBinding;

import java.util.List;

/*
 * Fragment which displays the main workout screen where users can start/stop workouts
 */
public class WorkoutFragment extends Fragment {
    private static final String TAG = WorkoutFragment.class.getSimpleName();

    private WorkoutViewModel mWorkoutViewModel;
    private Workout mWorkout;
    private Exercise mCurrentExercise;
    private TextView mExerciseNameTextView, mExerciseRepsTextView, mExerciseTimerView,
            mExerciseSetsTextView, mExerciseWeightTextView;

    private Button mStartButton;

    private long mWorkoutStartTime, mWorkoutElapsedTime;
    private Handler handler;
    private boolean mTimerStarted = false;

    private FragmentWorkoutBinding mBinding;

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
        mWorkout.setCurrentSet(1);
        setupObservers(workoutId);


        mBinding = FragmentWorkoutBinding.inflate(inflater, container, false);
        mBinding.setWorkout(mWorkout);

        Exercise exercise = new Exercise.Builder("Jogging", 3, 50).setRestTime(90).build();
        mBinding.setExercise(exercise);

        mBinding.setElapsedTime("00:00:00");

        //View view = inflater.inflate(R.layout.fragment_workout, container, false);
        View view = mBinding.getRoot();

        handler = new Handler();

        // Setup onclick listener to start a workout or a rest sequence
        mStartButton = view.findViewById(R.id.fragment_workout_start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mTimerStarted) {
                    //start next exercise and time how long it takes
                    mWorkoutStartTime = System.currentTimeMillis();
                    handler.postDelayed(runnable, 0);

                    mTimerStarted = true;

                    mStartButton.setText(R.string.finish_exercise);
                }else{
                    //
                    handler.removeCallbacks(runnable);
                    mTimerStarted = false;
                    mStartButton.setText(R.string.start_exercise);
                }
            }
        });

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


                //mExerciseNameTextView.setText(mWorkout.getWorkoutName());
            }
        });

        //monitor whenever a change is made to our workout's exercises and update the object
        mWorkoutViewModel.getExercises(workoutId).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                mWorkout.setExercises(exercises);
                Log.d(TAG, "Setting workout exercise to list of size: " + mWorkout.getExercises().size());

                updateUI();
            }
        });
    }

    private void updateUI(){
        Log.d(TAG, "Current ExerciseID: " + mWorkout.getCurrentExercise());

        //check if this workout was already started. If so, set current exercise to the one that is active
        if(mWorkout.getCurrentExercise() != -1)
        {
            //retreive exercise with matching ID
            for(Exercise e : mWorkout.getExercises())
            {
                if(e.getExerciseId() == mWorkout.getCurrentExercise())
                {
                    mCurrentExercise = e;
                }
            }
        }else{
            // If we are just starting a workout then default to first exercise in the list
            mWorkout.setCurrentExerciseIndex(0);
            mCurrentExercise = mWorkout.getExercises().get(mWorkout.getCurrentExerciseIndex());

            //save current exercise to DB
            Log.d(TAG, "Updating workout to: " + mCurrentExercise.getExerciseId() +
                    " for Workout: " + mWorkout.getWorkoutName());
            mWorkout.setCurrentExercise(mCurrentExercise.getExerciseId());
            mWorkout.setCurrentSet(1);

            mWorkoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
            mWorkoutViewModel.insertWorkoutWithExercises(mWorkout);

            //TODO: may throw exception if workout has no exercises; however, page should be unreachable if that were the case
        }

        mBinding.setExercise(mCurrentExercise);
        mBinding.setWorkout(mWorkout);

    }

    /*
     * Starts the next set, switch to next exercise if needed
     */
    private void nextSet(){
        if(mCurrentExercise.getSets() >= mWorkout.getCurrentSet() + 1) {

        }else{
            mWorkout.incrementExerciseIndex();
        }

        updateUI();
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {

            mWorkoutElapsedTime = System.currentTimeMillis() - mWorkoutStartTime;
            int seconds = (int) mWorkoutElapsedTime / 1000;
            int secondsDisplayed = seconds % 60;
            int minutes = (int) seconds / 60;
            int minutesDisplayed = minutes % 60;
            int hours = (int) minutes / 60;

            mBinding.setElapsedTime("" + String.format("%02d", hours) + ":" + String.format("%02d", minutesDisplayed) + ":" + String.format("%02d", secondsDisplayed));

            handler.postDelayed(this, 1000);
        }
    };
}
