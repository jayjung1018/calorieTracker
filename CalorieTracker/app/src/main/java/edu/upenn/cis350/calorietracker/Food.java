package edu.upenn.cis350.calorietracker;

import java.io.Serializable;

/**
 * Created by Zachary on 2/28/16.
 * Class to store food objects
 */
public class Food extends Trackable implements Serializable{
    private int fats;
    private int carbs;
    private int proteins;
    private int servingSize;
    private String mealType;

    //Food Constructor taking in the name, calories, and a note other fields set to defaults
    Food(String name, int calories, String notes) {
        super(name, calories, notes);
        this.fats = 0;
        this.carbs = 0;
        this.proteins = 0;
        this.servingSize = 1;
        this.mealType = "";
    }

    //Food Constructor taking in each field and a note
    Food (String name, int calories, int fats, int carbs, int proteins,
          int servingSize, boolean favorite, String notes, String mealType) {
        super(name, calories, favorite, notes);
        this.fats = fats;
        this.carbs = carbs;
        this.proteins = proteins;
        this.servingSize = servingSize;
        this.mealType = mealType;
    }

    //Getters and Setters for the Fields

    public int getFatsPerServing() {
        return this.fats;
    }

    public int getCarbsPerServing() {
        return this.carbs;
    }

    public int getProteinsPerServing() {
        return this.proteins;
    }

    public int getServingSize() {
        return this.servingSize;
    }

    public int getTotalCalories() {
        return this.getCalories()*this.servingSize;
    }

    public int getTotalFats() {
        return this.fats*this.servingSize;
    }

    public int getTotalCarbs() {
        return this.carbs*this.servingSize;
    }

    public int getTotalProteins() {
        return this.proteins*this.servingSize;
    }

    public String getMealType() {
        return mealType;
    }

    void setFats(int f) {
        this.fats = f;
    }

    void setCarbs(int c) {
        this.carbs = c;
    }

    void setProteins (int p) {
        this.proteins = p;
    }

    void setServingSize(int s) {
        this.servingSize = s;
    }

    /*Returns the food as a string listing the total Calories, Fats, Carbs,
    * Proteins, Serving Size, and Meal.  Returns these in the form Total ___: ____
    * two per line.
     */
    @Override
    public String toString() {
        String s = "Total Calories: " + getTotalCalories() +
                "\t\t\tTotal Fats(g): " + getTotalFats() +
                "\nTotal Carbs(g): " + getTotalCarbs() +
                "\t\t\tTotal Proteins(g): " + getTotalProteins() +
                "\nServing Size: " + getServingSize() +
                "\t\t\tMeal Type: " + getMealType();
        return s;
    }

    /*Checks if this food is equal to another Trackable by checking each field
    Returns a boolean, true if equal, false if not
     */
    @Override
    public boolean equals(Trackable t) {
        Food f = (Food) t;
        return this.getName().equals(f.getName()) &&
                this.getCalories()== f.getCalories() &&
                this.getFatsPerServing()== f.getFatsPerServing() &&
                this.getCarbsPerServing()== f.getCarbsPerServing() &&
                this.getProteinsPerServing()==f.getProteinsPerServing() &&
                this.getServingSize()==f.getServingSize();
    }

}