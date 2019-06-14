package com.cmcinnis.craig.workoutplanner;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmcinnis.craig.workoutplanner.Database.Workout;
import com.cmcinnis.craig.workoutplanner.Models.WorkoutViewModel;

public class WorkoutDialogFragment extends DialogFragment {
    private static final String TAG = "WorkoutDialogFragment";
    private static final String ARG_DATA = "exercise";

    private EditText mWorkoutName;
    private Workout mWorkout;

    private WorkoutViewModel mWorkoutViewModel;

    /*
     * Create an instance of this dialog with a passed in Workout object
     */
    public static WorkoutDialogFragment newInstance(Workout workout){
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, workout);

        WorkoutDialogFragment fragment = new WorkoutDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // get passed in exercise
        mWorkout = getArguments().getParcelable(ARG_DATA);

        //setup the alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_workout, null);

        builder.setView(v)
                .setPositiveButton(R.string.dialog_save_exercise, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Here we save the exercise after validating it has valid characteristics
                        mWorkout.setWorkoutName(mWorkoutName.getText().toString());

                        String validation = Workout.validateWorkout(mWorkout);
                        if(validation == null)
                        {
                            Log.d(TAG, "Saving Workout: " + mWorkout);
                            mWorkoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
                            mWorkoutViewModel.insertWorkoutWithExercises(mWorkout);
                        }else{
                            Log.d(TAG, "Workout validation failed: " + validation);
                            Toast.makeText(getContext(), "Failed to save Workout: " + validation, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //cancel the work
                        WorkoutDialogFragment.this.getDialog().cancel();
                    }
                });

        //set default values in case we passed in an existing workout
        mWorkoutName = v.findViewById(R.id.dialog_workout_name);
        mWorkoutName.setText(mWorkout.getWorkoutName());

        return builder.create();
    }
}
