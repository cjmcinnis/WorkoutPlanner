package com.cmcinnis.craig.workoutplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class WorkoutListActivity extends SingleFrameActivity{

    @Override
    public Fragment createFragment(){
        return new WorkoutListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
