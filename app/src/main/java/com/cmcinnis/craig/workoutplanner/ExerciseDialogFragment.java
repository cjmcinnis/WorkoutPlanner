package com.cmcinnis.craig.workoutplanner;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.cmcinnis.craig.workoutplanner.Database.Exercise;
import com.cmcinnis.craig.workoutplanner.Models.WorkoutViewModel;

/*
 * Fragment used for creating or modifying an exercise.
 * Contains all the input fields for setting up an exercise
 */
public class ExerciseDialogFragment extends DialogFragment {
    private static final String TAG = "ExerciseDialogFragment";
    private static final String ARG_DATA = "exercise";
    private static final int REST_DEFAULT_VALUE = 60; //default value for rest time

    private EditText mExerciseName;
    private NumberPicker mReps;
    private NumberPicker mSets;
    private NumberPicker mRest;
    private Exercise mExercise;
    private WorkoutViewModel mWorkoutViewModel;
    /*
     * Create an instance of this dialog with a passed in exercise
     */
    public static ExerciseDialogFragment newInstance(Exercise exercise){
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, exercise);

        ExerciseDialogFragment fragment = new ExerciseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get passed in exercise
        mExercise = getArguments().getParcelable(ARG_DATA);

        //setup the alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_exercise, null);

        builder.setView(v)
                .setPositiveButton(R.string.dialog_save_exercise, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // validate all the input, if it fails display error msg
                        if(validateExerciseName())
                        {
                            //save the exercise
                            mExercise.setSets(mSets.getValue());
                            mExercise.setReps(mReps.getValue());
                            mExercise.setRestDuration(mRest.getValue());
                            mExercise.setExerciseName(mExerciseName.getText().toString());

                            Log.d(TAG, "Saving Exercise: " + mExercise);
                            mWorkoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
                            mWorkoutViewModel.insertExercise(mExercise);
                        }else{
                            Log.d(TAG, "Exercise validation failed");
                        }

                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //cancel the work
                        ExerciseDialogFragment.this.getDialog().cancel();
                    }
                });

        //link all our fields in the dialog set default values
        mExerciseName = v.findViewById(R.id.dialog_exercise_name_input);
        mExerciseName.setText("text");
        mReps = v.findViewById(R.id.dialog_exercise_reps_input);
        mReps.setMinValue(Exercise.MIN_REPS);
        mReps.setMaxValue(Exercise.MAX_REPS);
        mReps.setWrapSelectorWheel(false);
        //initialize reps to current value we passed in
        if (mExercise.getReps() != 0)
        {
            mReps.setValue(mExercise.getReps());
        }

        mSets = v.findViewById(R.id.dialog_exercise_set_input);
        mSets.setMinValue(Exercise.MIN_SETS);
        mSets.setMaxValue(Exercise.MAX_SETS);
        mSets.setWrapSelectorWheel(false);
        //initialize sets to current value we passed in
        if (mExercise.getSets() != 0)
        {
            mSets.setValue(mExercise.getSets());
        }

        //increase step size of rest time numberpick using formatter
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return "" + i * 5;
            }
        };
        mRest = v.findViewById(R.id.dialog_exercise_rest_input);
        mRest.setMinValue(Exercise.MIN_REST);
        mRest.setMaxValue(Exercise.MAX_REST);
        mRest.setFormatter(formatter);
        mRest.setWrapSelectorWheel(false);
        //initialize rest duration to current value we passed in, else default
        if(mExercise.getRestDuration() != 0)
        {
            mRest.setValue(mExercise.getRestDuration());
        }else{
            mRest.setValue(REST_DEFAULT_VALUE);
        }

        return builder.create();
    }
    /*
     * Validates if the provided exercise is valid, if not display error msg
     */
    private boolean validateExerciseName(){
        boolean b = Exercise.validateExerciseName(mExerciseName.getText());

        if(!b)
            Toast.makeText(getContext(), "Invalid name for exercise", Toast.LENGTH_SHORT).show();

        return b;
    }
}
