package edu.upenn.cis350.calorietracker;

import java.io.Serializable;

/**
 * Created by Zachary on 3/15/16.
 * An abstract class for a trackable object - Food and Exercise classes inherit this
 */
public abstract class Trackable implements Serializable{
    String name;
    int calories;
    String notes;
    boolean isFavorite;

    Trackable (String name, int calories, String notes) {
        this.name = name;
        this.calories = calories;
        this.isFavorite = false;
        this.notes = notes;
    }

    Trackable (String name, int calories, boolean favorite, String notes) {
        this.name = name;
        this.calories = calories;
        this.isFavorite = favorite;
        this.notes = notes;
    }

    public String getName() {
        return this.name;
    }

    public int getCalories() {
        return this.calories;
    }

    public boolean isFavorite () {
        return this.isFavorite;
    }

    public String getNotes() {
        return this.notes;
    }

    void setName(String s) {
        this.name = s;
    }

    void setCalories(int c) {
        this.calories = c;
    }

    void setFavorite(boolean f) {
        this.isFavorite = f;
    }

    void setNotes(String s) {
        this.notes = s;
    }

    public abstract String toString();

    public abstract boolean equals(Trackable t);
}
