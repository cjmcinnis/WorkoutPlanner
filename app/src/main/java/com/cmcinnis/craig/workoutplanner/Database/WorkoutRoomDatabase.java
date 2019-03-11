package com.cmcinnis.craig.workoutplanner.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Workout.class, Exercise.class}, version = 1)
abstract class WorkoutRoomDatabase extends RoomDatabase {
    private static volatile WorkoutRoomDatabase INSTANCE;
    private static final String DB_NAME = "WorkoutDatabase";

    public abstract WorkoutDao workoutDao();

    /*
     * Make our database as a singleton
     */
    static WorkoutRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WorkoutRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WorkoutRoomDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // add test data
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WorkoutDao mDao;

        PopulateDbAsync(WorkoutRoomDatabase db) {
            mDao = db.workoutDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteWorkouts();
            mDao.deleteExercises();
            Workout workout = new Workout("workout1");
            Exercise exercise = new Exercise.Builder("Bench Press", 3, 10).setRestTime(90).build();
            workout.exercises.add(exercise);
            exercise = new Exercise.Builder("Curls", 5, 6).build();
            workout.exercises.add(exercise);

            mDao.insertWorkoutWithExercises(workout);
            workout = new Workout("Cardio");
            exercise = new Exercise.Builder("Jogging", 3, 50).setRestTime(90).build();
            workout.exercises.add(exercise);
            mDao.insertWorkoutWithExercises(workout);
            return null;
        }
    }
}