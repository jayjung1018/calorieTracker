package edu.upenn.cis350.calorietracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Zachary on 2/28/16.
 * A class that contains all of the relevant lists - uses the singleton design to keep all lists
 * static so that each class can use this
 */
public class TrackableList implements Serializable{
    private static ArrayList<Food> todayFoods;
    private static ArrayList<Food> recentFoods;
    private static ArrayList<Food> favoriteFoods;
    private static ArrayList<Food> d2Foods;
    private static ArrayList<Food> d3Foods;
    private static ArrayList<Food> d4Foods;
    private static ArrayList<Food> d5Foods;
    private static ArrayList<Food> d6Foods;
    private static ArrayList<Food> d7Foods;
    private static ArrayList<Exercise> d2Exercises;
    private static ArrayList<Exercise> d3Exercises;
    private static ArrayList<Exercise> d4Exercises;
    private static ArrayList<Exercise> d5Exercises;
    private static ArrayList<Exercise> d6Exercises;
    private static ArrayList<Exercise> d7Exercises;
    private static ArrayList<Exercise> todayExercises;
    private static ArrayList<Exercise> recentExercises;
    private static ArrayList<Exercise> favoriteExercises;
    private static TrackableList trackableList;

    private TrackableList() {
        this.todayFoods = new ArrayList<Food>();
        this.recentFoods = new ArrayList<Food>();
        this.favoriteFoods = new ArrayList<Food>();
        this.todayExercises = new ArrayList<Exercise>();
        this.recentExercises = new ArrayList<Exercise>();
        this.favoriteExercises = new ArrayList<Exercise>();
        this.d2Foods = new ArrayList<Food>();
        this.d3Foods = new ArrayList<Food>();
        this.d4Foods = new ArrayList<Food>();
        this.d5Foods = new ArrayList<Food>();
        this.d6Foods = new ArrayList<Food>();
        this.d7Foods = new ArrayList<Food>();
        this.d2Exercises = new ArrayList<Exercise>();
        this.d3Exercises = new ArrayList<Exercise>();
        this.d4Exercises = new ArrayList<Exercise>();
        this.d5Exercises = new ArrayList<Exercise>();
        this.d6Exercises = new ArrayList<Exercise>();
        this.d7Exercises = new ArrayList<Exercise>();
    }

    static void buildLists() {
        if (trackableList == null) {
            trackableList = new TrackableList();
        }
    }

    static TrackableList getAllLists() {
        return trackableList;
    }

    static void setAllLists(ArrayList<Food> favoriteFoods,
                            ArrayList<Food> recentFoods,
                            ArrayList<Exercise> favoriteExercises,
                            ArrayList<Exercise> recentExercises,
                            ArrayList<Food> d1Foods,
                            ArrayList<Food> d2Foods,
                            ArrayList<Food> d3Foods,
                            ArrayList<Food> d4Foods,
                            ArrayList<Food> d5Foods,
                            ArrayList<Food> d6Foods,
                            ArrayList<Food> d7Foods,
                            ArrayList<Exercise> d1Exercises,
                            ArrayList<Exercise> d2Exercises,
                            ArrayList<Exercise> d3Exercises,
                            ArrayList<Exercise> d4Exercises,
                            ArrayList<Exercise> d5Exercises,
                            ArrayList<Exercise> d6Exercises,
                            ArrayList<Exercise> d7Exercises) {

        trackableList.favoriteFoods = favoriteFoods;
        trackableList.favoriteExercises = favoriteExercises;
        trackableList.recentFoods = recentFoods;
        trackableList.recentExercises = recentExercises;
        trackableList.todayFoods = d1Foods;
        trackableList.d2Foods = d2Foods;
        trackableList.d3Foods = d3Foods;
        trackableList.d4Foods = d4Foods;
        trackableList.d5Foods = d5Foods;
        trackableList.d6Foods = d6Foods;
        trackableList.d7Foods = d7Foods;
        trackableList.todayExercises = d1Exercises;
        trackableList.d2Exercises = d2Exercises;
        trackableList.d3Exercises = d3Exercises;
        trackableList.d4Exercises = d4Exercises;
        trackableList.d5Exercises = d5Exercises;
        trackableList.d6Exercises = d6Exercises;
        trackableList.d7Exercises = d7Exercises;
    }

    static ArrayList<Food> getTodayFoods() {
        return trackableList.todayFoods;
    }

    static ArrayList<Food> getRecentFoods() {
        return trackableList.recentFoods;
    }

    static ArrayList<Food> getFavoriteFoods() {
        return trackableList.favoriteFoods;
    }

    static ArrayList<Exercise> getTodayExercises() {
        return trackableList.todayExercises;
    }

    static ArrayList<Exercise> getRecentExercises() {
        return trackableList.recentExercises;
    }

    static ArrayList<Exercise> getFavoriteExercises() {
        return trackableList.favoriteExercises;
    }

    public static ArrayList<Food> getD2Foods() {
        return d2Foods;
    }

    public static ArrayList<Food> getD3Foods() {
        return d3Foods;
    }

    public static ArrayList<Food> getD4Foods() {
        return d4Foods;
    }

    public static ArrayList<Food> getD5Foods() {
        return d5Foods;
    }

    public static ArrayList<Food> getD6Foods() {
        return d6Foods;
    }

    public static ArrayList<Food> getD7Foods() {
        return d7Foods;
    }

    public static ArrayList<Exercise> getD2Exercises() {
        return d2Exercises;
    }

    public static ArrayList<Exercise> getD3Exercises() {
        return d3Exercises;
    }

    public static ArrayList<Exercise> getD4Exercises() {
        return d4Exercises;
    }

    public static ArrayList<Exercise> getD5Exercises() {
        return d5Exercises;
    }

    public static ArrayList<Exercise> getD6Exercises() {
        return d6Exercises;
    }

    public static ArrayList<Exercise> getD7Exercises() {
        return d7Exercises;
    }

    static void addTodayFood(Food f) {
        trackableList.todayFoods.add(0, f);
    }

    static void removeTodayFood(Food f) {
        trackableList.todayFoods.remove(f);
    }

    static void removeTodayFood(int i) {
        trackableList.todayFoods.remove(i);
    }

    static void editTodayFood(Food former, Food replacement) {
        if (!replacement.equals(former)) {
            for (Food f : todayFoods) {
                if (f.equals(former)) {
                    int index = todayFoods.indexOf(f);
                    todayFoods.remove(index);
                    todayFoods.add(index, replacement);
                    return;
                }
            }
        }
    }

    //adding new food to recent list bumps last value off list
    static void addRecentFood(Food f) {
        trackableList.recentFoods.add(0, f);
        while (trackableList.recentFoods.size() > 10) {
            trackableList.recentFoods.remove(1);
        }
    }

    static void addFavoriteFood(Food f) {
        trackableList.favoriteFoods.add(f);
        f.setFavorite(true);
    }

    static void removeFavoriteFood(Food f) {
        trackableList.favoriteFoods.remove(f);
        f.setFavorite(false);
    }

    static void removeFavoriteFood(int i) {
        Food food = trackableList.favoriteFoods.remove(i);
        food.setFavorite(false);
    }

    static void editFavoriteFood(Food former, Food replacement) {
        if (!replacement.equals(former)) {
            for (Food f : favoriteFoods) {
                if (f.equals(former)) {
                    int index = favoriteFoods.indexOf(f);
                    favoriteFoods.remove(index);
                    favoriteFoods.add(index, replacement);
                    return;
                }
            }
        }
    }

    //exercise add and remove
    static void addTodayExercise(Exercise e) {
        trackableList.todayExercises.add(0, e);
    }

    static void removeTodayExercise(Exercise e) {
        trackableList.todayExercises.remove(e);
    }

    static void removeTodayExercise(int i) {
        trackableList.todayExercises.remove(i);
    }

    static void editTodayExercise(Exercise former, Exercise replacement) {
        if (!replacement.equals(former)) {
            for (Exercise e : todayExercises) {
                if (e.equals(former)) {
                    int index = todayExercises.indexOf(e);
                    todayExercises.remove(index);
                    todayExercises.add(index,replacement);
                    return;
                }
            }
        }
    }

    static void addRecentExercise(Exercise e) {
        for (Exercise temp : trackableList.recentExercises) {
            if (temp.equals(e)) {
                trackableList.recentExercises.remove(temp);
            }
        }
        trackableList.recentExercises.add(0, e);
        while (trackableList.recentExercises.size() > 10) {
            trackableList.recentExercises.remove(trackableList.recentExercises.size());
        }
    }

    static void addFavoriteExercise(Exercise e) {
        trackableList.favoriteExercises.add(e);
        e.setFavorite(true);
    }

    static void removeFavoriteExercise(Exercise e) {
        trackableList.favoriteExercises.remove(e);
        e.setFavorite(false);
    }

    static void removeFavoriteExercise(int i) {
        Exercise exercise = trackableList.favoriteExercises.remove(i);
        exercise.setFavorite(false);
    }

    static void editFavoriteExercise(Exercise former, Exercise replacement) {
        if (!replacement.equals(former)) {
            for (Exercise e : favoriteExercises) {
                if (e.equals(former)) {
                    int index = favoriteExercises.indexOf(e);
                    favoriteExercises.remove(index);
                    favoriteExercises.add(index,replacement);
                    return;
                }
            }
        }
    }

    static void resetTodayFoods() {
        //if time is midnight
        trackableList.todayFoods = new ArrayList<Food>();
    }

    static void resetTodayExercises() {
        //if time is midnight
        trackableList.todayExercises = new ArrayList<Exercise>();
    }

    static int getTodayTotalFoodCalories() {
        int totalCalories = 0;
        for (Food f : todayFoods) {
            totalCalories += f.getTotalCalories();
        }
        return totalCalories;
    }

    static int getTodayTotalFats() {
        int totalFats = 0;
        for (Food f : todayFoods) {
            totalFats += f.getTotalFats();
        }
        return totalFats;
    }

    static int getTodayTotalCarbs() {
        int totalCarbs = 0;
        for (Food f : todayFoods) {
            totalCarbs += f.getTotalCarbs();
        }
        return totalCarbs;
    }

    static int getTodayTotalProteins() {
        int totalProteins = 0;
        for (Food f : todayFoods) {
            totalProteins += f.getTotalProteins();
        }
        return totalProteins;
    }

    static int getTodayTotalExerciseCalories() {
        int totalCalories = 0;
        for (Exercise e : todayExercises) {
            totalCalories += e.getCalories();
        }
        return totalCalories;
    }

    static int getTodayTotalMinutes() {
        int totalMinutes = 0;
        for (Exercise e : todayExercises) {
            totalMinutes += e.getMinutes();
        }
        return totalMinutes;
    }

    static int getFoodCalories(ArrayList<Food> list) {
        int totalCalories = 0;
        for (Food f : list) {
            totalCalories += f.getCalories();
        }

        return totalCalories;
    }

    static void shiftLists() {
        trackableList.d7Foods = trackableList.d6Foods;
        trackableList.d6Foods = trackableList.d5Foods;
        trackableList.d5Foods = trackableList.d4Foods;
        trackableList.d4Foods = trackableList.d3Foods;
        trackableList.d3Foods = trackableList.d2Foods;
        trackableList.d2Foods = trackableList.todayFoods;
        trackableList.todayFoods = new ArrayList<Food>();

        trackableList.d7Exercises = trackableList.d6Exercises;
        trackableList.d6Exercises = trackableList.d5Exercises;
        trackableList.d5Exercises = trackableList.d4Exercises;
        trackableList.d4Exercises = trackableList.d3Exercises;
        trackableList.d3Exercises = trackableList.d2Exercises;
        trackableList.d2Exercises = trackableList.todayExercises;
        trackableList.todayExercises = new ArrayList<Exercise>();

        //get db
        SQLiteDatabase db = MainActivity.mDbHelper.getReadableDatabase();

        //serialize the lists to blobs -> foods
        byte[] blobf1 = Serializer.serializeObject(trackableList.todayFoods);
        byte[] blobf2 = Serializer.serializeObject(trackableList.d2Foods);
        byte[] blobf3 = Serializer.serializeObject(trackableList.d3Foods);
        byte[] blobf4 = Serializer.serializeObject(trackableList.d4Foods);
        byte[] blobf5 = Serializer.serializeObject(trackableList.d5Foods);
        byte[] blobf6 = Serializer.serializeObject(trackableList.d6Foods);
        byte[] blobf7 = Serializer.serializeObject(trackableList.d7Foods);

        //exercises
        byte[] blobe1 = Serializer.serializeObject(trackableList.todayExercises);
        byte[] blobe2 = Serializer.serializeObject(trackableList.d2Exercises);
        byte[] blobe3 = Serializer.serializeObject(trackableList.d3Exercises);
        byte[] blobe4 = Serializer.serializeObject(trackableList.d4Exercises);
        byte[] blobe5 = Serializer.serializeObject(trackableList.d5Exercises);
        byte[] blobe6 = Serializer.serializeObject(trackableList.d6Exercises);
        byte[] blobe7 = Serializer.serializeObject(trackableList.d7Exercises);

        //set the values
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D1, blobf1);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D2, blobf2);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D3, blobf3);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D4, blobf4);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D5, blobf5);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D6, blobf6);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D7, blobf7);

        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D1E, blobe1);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D2E, blobe2);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D3E, blobe3);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D4E, blobe4);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D5E, blobe5);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D6E, blobe6);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D7E, blobe7);

        //select query
        String selection = DatabaseContract.DataEntry.COLUMN_NAME_FBID + "=?";
        String[] selectionArgs = {MainActivity.fb_id};

        //update db
        db.update(
                DatabaseContract.DataEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}