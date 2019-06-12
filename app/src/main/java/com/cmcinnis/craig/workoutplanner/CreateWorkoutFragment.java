package com.cmcinnis.craig.workoutplanner;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;
import com.cmcinnis.craig.workoutplanner.Database.Workout;
import com.cmcinnis.craig.workoutplanner.Models.WorkoutViewModel;

import java.util.Collections;
import java.util.List;

/*
 * Page for managing Workouts by adding/modify the exercises inside of it
 */

public class CreateWorkoutFragment extends Fragment {
    private static final String TAG = "CreateWorkoutFragment";
    private static final int REQUEST_EXERCISE = 0; //code when returning from ExerciseDialogFragment
    private static final String DIALOG_EXERCISE = "ExerciseDialog"; //code when returning from ExerciseDialogFragment
    private Workout mWorkout;
    private WorkoutViewModel mWorkoutViewModel;
    private RecyclerView mExerciseRecyclerView;
    private ExerciseAdapter mAdapter;
    private Button mNewExerciseButton;

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

        mExerciseRecyclerView = view.findViewById(R.id.create_exercise_recycler);
        mExerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        mWorkout = new Workout("");
        if(workoutId != -1)
        {
            // if we sent in a workout then load it into UI
            setupObservers(workoutId);
        }

        //Setup button for adding a new exercise
        mNewExerciseButton = (Button) view.findViewById(R.id.add_exercise_button);
        mNewExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an alert dialog for creating a new exercise
                modifyExercise(null);
            }
        });


        return view;
    }

    /*
     * Updates our ExerciseRecyclerView when it has an update or creates it if is the first time in use
     */
    private void updateUI(){
        //setup adapter if we need to
        if(mAdapter == null) {

            //setup adapter
            mAdapter = new ExerciseAdapter();

            //mWorkoutRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            mExerciseRecyclerView.setAdapter(mAdapter);
        }else{
        }
    }

    /*
     * Create an AlertDialog for modifying an exercise. If passed in exercise is null, then it
     * will create a new one.
     */
    private void modifyExercise(Exercise exercise){
        if(exercise == null)
        {
            exercise = new Exercise.Builder("", 0 , 0)
                    .build();
            exercise.setWorkoutId(mWorkout.getId());
            Log.d(TAG, "Creating exercise dialog for a new exercise");
        }else{
            Log.d(TAG, "Creating ExerciseDialog for " + exercise.getExerciseName());
        }

        FragmentManager manager = getFragmentManager();
        ExerciseDialogFragment dialog = ExerciseDialogFragment.newInstance(exercise);
        dialog.setTargetFragment(CreateWorkoutFragment.this, REQUEST_EXERCISE);
        dialog.show(manager, DIALOG_EXERCISE);

    }

    /*
     * Setup an observer to watch when workout & exercises change
     */
    private void setupObservers(long workoutId){
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
                //update our adapter list when ever exercises are updated
                mAdapter.setExercises(mWorkout.getExercises());
            }
        });
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private Exercise mExercise;
        private TextView mTextView;

        public ExerciseHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {

            super(inflater.inflate(viewType, parent, false));
            mTextView = (TextView) itemView.findViewById(R.id.exercise_title);

            //set listener for when we select exercise in list
            itemView.setOnClickListener(this);
        }

        /* When clicked create a dialog fragment for editing an exercise
         */
        @Override
        public void onClick(View v) {
            // Modify the exercise using an alertdialog
            modifyExercise(mExercise);

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
        public void setExercises(List<Exercise> exercises){
            mExercises = exercises;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.list_item_exercise;
        }
    }
}
