package edu.upenn.cis350.calorietracker;

import java.io.Serializable;

/**
 * Created by Zachary on 3/15/16.
 * Object created to store exercises
 */
public class Exercise extends Trackable implements Serializable {
    private int minutes;
    private String intensity;


    Exercise (String name, int calories, int minutes, String intensity, boolean favorite,
              String notes) {
        super (name, calories, favorite, notes);
        this.minutes = minutes;
        this.intensity = intensity;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public String getIntensity() {
        return this.intensity;
    }

    public void setMinutes(int m) {
        this.minutes = m;
    }

    public void setIntensity(String i) {
        this.intensity = i;
    }

    @Override
    public String toString() {
        String s = "Calories Added: " + getCalories() +
                "\nMinutes: " + getMinutes() +
                "\nIntensity: " + getIntensity();
        return s;
    }

    @Override
    public boolean equals(Trackable t) {
        Exercise e = (Exercise) t;
        return this.getName().equals(e.getName()) &&
                this.getCalories() == e.getCalories() &&
                this.getMinutes() == e.getMinutes() &&
                this.getIntensity().equals(e.getIntensity());
    }

}
