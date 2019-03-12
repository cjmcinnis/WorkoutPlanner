package com.cmcinnis.craig.workoutplanner;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;
import com.cmcinnis.craig.workoutplanner.Database.Workout;
import com.cmcinnis.craig.workoutplanner.Models.WorkoutViewModel;

import java.util.Collections;
import java.util.List;

public class CreateWorkoutFragment extends Fragment {
    private static final String TAG = "CreateWorkoutFragment";
    private Workout mWorkout;
    private WorkoutViewModel mWorkoutViewModel;

    /*
     * Creates an instance of this fragment with a supplied workout Id
     */
    public static CreateWorkoutFragment newInstance(long workoutId)
    {
        Bundle args = new Bundle();
        args.putLong(WorkoutListFragment.WORKOUT_ID_REQUEST, workoutId);

        CreateWorkoutFragment fragment = new CreateWorkoutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_create_workout, container, false);

        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        long workoutId = getArguments().getLong(WorkoutListFragment.WORKOUT_ID_REQUEST);
        if(workoutId == -1)
        {
            mWorkout = new Workout("");
        }else{
            setupObservers(workoutId);
        }

        return view;
    }

    /*
     * Setups an observer to watch when workout & exercises change
     */
    public void setupObservers(long workoutId){
        mWorkoutViewModel.getWorkout(workoutId).observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                mWorkout = workout;
                Log.d(TAG, "Setting workout to: " + workout.getWorkoutName());
            }
        });

        mWorkoutViewModel.getExercises(workoutId).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                mWorkout.setExercises(exercises);
                Log.d(TAG, "Setting workout exercise to list of size: " + mWorkout.getExercises().size());
            }
        });
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private Exercise mExercise;
        private TextView mTextView;

        public ExerciseHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {

            super(inflater.inflate(viewType, parent, false));
            //mTextView = (TextView) itemView.findViewById(R.id.workout_title);
            itemView.setOnClickListener(this);
        }

        /* When clicked create a dialog fragment for editing an exercise
         */
        @Override
        public void onClick(View v) {
        }

        // Update each row with data
        public void bind(Exercise exercise){
            mExercise = exercise;
            mTextView.setText(mExercise.getExerciseName());
        }
    }

    private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder>{



        private List<Exercise> mExercises = Collections.emptyList();

        public ExerciseAdapter(){ }

        @NonNull
        @Override
        public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ExerciseHolder(layoutInflater, parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
            Exercise exercise = mExercises.get(position);
            holder.bind(exercise);
        }


        @Override
        public int getItemCount() {
            return mExercises.size();
        }

        /* Update workout list and update our list
         *
         */
        public void setWorkouts(List<Exercise> workouts){
            mExercises = workouts;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.list_item_workout;
        }
    }
}
