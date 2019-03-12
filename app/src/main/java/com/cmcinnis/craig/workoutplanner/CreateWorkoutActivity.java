package com.cmcinnis.craig.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;
import com.cmcinnis.craig.workoutplanner.Database.Workout;

import java.util.Collections;
import java.util.List;

public class CreateWorkoutActivity extends SingleFrameActivity {

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, CreateWorkoutActivity.class);
        return  intent;

    }

    @Override
    protected Fragment createFragment() {
        // get workout Id, -1 if no workout supplied
        long workoutId = getIntent().getLongExtra(WorkoutListFragment.WORKOUT_ID_REQUEST,-1);
        return CreateWorkoutFragment.newInstance(workoutId);
    }



}
