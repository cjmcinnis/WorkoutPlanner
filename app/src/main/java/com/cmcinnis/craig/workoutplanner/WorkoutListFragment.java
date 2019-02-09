package com.cmcinnis.craig.workoutplanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkoutListFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class WorkoutHolder extends RecyclerView.ViewHolder{

        public WorkoutHolder(View itemView) {
            super(itemView);
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder>{

        @NonNull
        @Override
        public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 0;
        }
    }

}
