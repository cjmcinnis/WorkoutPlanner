package com.cmcinnis.craig.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

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
