package com.cmcinnis.craig.workoutplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WorkoutListFragment extends Fragment {

    private RecyclerView mWorkoutRecyclerView;
    private WorkoutAdapter mAdapter;
    private Button mNewWorkoutButton;

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
                createNewWorkout();
            }
        });

        updateUI();

        return view;
    }

    // Reload the recyclerview when it is updated or first created
    public void updateUI(){


        if(mAdapter == null) {
            List<Workout> mWorkouts = new ArrayList<>();
            mWorkouts.add(new Workout("test"));
            mWorkouts.add(new Workout("test2"));

            //setup adapter
            mAdapter = new WorkoutAdapter(mWorkouts);
            //mWorkoutRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            mWorkoutRecyclerView.setAdapter(mAdapter);
        }else{
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Send user to activity for setting up a new workout
    private void createNewWorkout(){
        Intent intent = CreateWorkoutActivity.newIntent(getActivity());
        startActivity(intent);
    }


    private class WorkoutHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private Workout mWorkout;
        private TextView mTextView;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {

            super(inflater.inflate(viewType, parent, false));
            mTextView = (TextView) itemView.findViewById(R.id.workout_title);
        }

        // when clicked send user to WorkoutViewerActivity
        @Override
        public void onClick(View v) {

        }

        // Update each row with data
        public void bind(Workout workout){
            mWorkout = workout;
            mTextView.setText(mWorkout.getWorkoutName());
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder>{



        private List<Workout> mWorkouts;

        public WorkoutAdapter(List<Workout> workouts){
            mWorkouts = workouts;
        }

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

        public void setWorkouts(List<Workout> workouts){mWorkouts = workouts;}

        @Override
        public int getItemViewType(int position) {
            return R.layout.list_item_workout;
        }
    }

}
