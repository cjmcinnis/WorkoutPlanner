package com.cmcinnis.craig.workoutplanner;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmcinnis.craig.workoutplanner.Database.Workout;
import com.cmcinnis.craig.workoutplanner.Models.WorkoutViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkoutListFragment extends Fragment {

    private static final String TAG = "WorkoutListFragment";
    public static final String WORKOUT_ID_REQUEST = "WorkoutId";
    public static final int NEW_WORKOUT_ACTIVITY_REQUEST_CODE = 1;
    private RecyclerView mWorkoutRecyclerView;
    private WorkoutAdapter mAdapter;
    private Button mNewWorkoutButton;
    private WorkoutViewModel mWorkoutViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);
        mWorkoutRecyclerView = (RecyclerView) view.findViewById(R.id.workout_recycler_view);
        mWorkoutRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setup button to create a new workout
        mNewWorkoutButton = (Button) view.findViewById(R.id.new_workout_button);
        mNewWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass in null since we are creating a new workout
                editWorkout(null);
            }
        });

        //setup workoutviewmodel
        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);



        updateUI();

        return view;
    }

    /*
     * Sets up an observer to check when our WorkoutList has beeen updated
     */
    private void setupWorkoutObserver() {
        mWorkoutViewModel.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(@Nullable List<Workout> workouts) {
                if (mAdapter != null) {
                    mAdapter.setWorkouts(workouts);
                }else {
                    Log.e(TAG, "Attempted to update workout list with null adapter");
                }
            }
        });
    }

    // Reload the recyclerview when it is updated or first created
    public void updateUI(){

        //setup adapter if we need to
        if(mAdapter == null) {

            //setup adapter
            mAdapter = new WorkoutAdapter();

            //mWorkoutRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            mWorkoutRecyclerView.setAdapter(mAdapter);

        }else{
        }

        //update workout list
        setupWorkoutObserver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * Starts an activity for modifying a workout
     * if parameter is null then we are creating a new workout
     */
    private void editWorkout(Workout workout){
        Intent intent = CreateWorkoutActivity.newIntent(getActivity());

        if(workout != null)
        {
            intent.putExtra(WORKOUT_ID_REQUEST, workout.getId());
        }

        //startActivityForResult(intent, NEW_WORKOUT_ACTIVITY_REQUEST_CODE);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateUI();
    }

    private class WorkoutHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private Workout mWorkout;
        private TextView mTextView;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {

            super(inflater.inflate(viewType, parent, false));
            mTextView = (TextView) itemView.findViewById(R.id.workout_title);
            itemView.setOnClickListener(this);
        }

        /* when clicked send user to WorkoutViewerActivity
         * pass in workout
         */
        @Override
        public void onClick(View v) {

            editWorkout(mWorkout);
        }

        // Update each row with data
        public void bind(Workout workout){
            mWorkout = workout;
            mTextView.setText(mWorkout.getWorkoutName());
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder>{



        private List<Workout> mWorkouts = Collections.emptyList();

        public WorkoutAdapter(){ }

        @NonNull
        @Override
        public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new WorkoutHolder(layoutInflater, parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {
            Workout workout = mWorkouts.get(position);
            holder.bind(workout);
        }


        @Override
        public int getItemCount() {
            return mWorkouts.size();
        }

        /* Update workout list and update our list
         *
         */
        public void setWorkouts(List<Workout> workouts){
            mWorkouts = workouts;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.list_item_workout;
        }
    }

}
