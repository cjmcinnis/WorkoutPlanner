package com.cmcinnis.craig.workoutplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/*
 * An abstract class used for quickly implementation a holder class for hosting fragments
 *
 */

public abstract class SingleFrameActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //read in the xml container
        setContentView(R.layout.activity_fragment);

        //setup fragment manager
        FragmentManager fm = getSupportFragmentManager();
        //add blank fragment container as first fragment
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
