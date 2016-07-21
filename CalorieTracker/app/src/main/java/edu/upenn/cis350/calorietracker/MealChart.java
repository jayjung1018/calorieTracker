package edu.upenn.cis350.calorietracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Jay Jung on 4/8/2016.
 * Class to show the infographic on meal types
 */
public class MealChart extends AppCompatActivity{
    Switch selectSwitch;
    ArrayList<Food> todayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        todayList = (ArrayList<Food>) getIntent().getSerializableExtra("List");

        //find the switch -> initialize it
        selectSwitch = (Switch) findViewById(R.id.select2);
        selectSwitch.setChecked(true);

        //set what setting the switch does
        selectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    Log.v("check", "check");
                    setUpCalorieChart(todayList);
                }
                else {
                    Log.v("macro", "macro");
                    setUpMacroChart(todayList);
                }
            }
        });

        //initial
        setUpCalorieChart(todayList);
    }
    public void setUpCalorieChart(ArrayList<Food> todayList) {
        int bfCalories = 0;
        int lCalories = 0;
        int dCalories = 0;
        int sCalories = 0;

        //go through list and find the sums
        for (Food food : todayList) {
            String currentMeal = food.getMealType();
            switch (currentMeal) {
                case "Breakfast":
                    bfCalories = bfCalories + food.getTotalCalories();
                    break;
                case "Lunch":
                    lCalories = lCalories + food.getTotalCalories();
                    break;
                case "Dinner":
                    dCalories = dCalories + food.getTotalCalories();
                    break;
                case "Snack":
                    sCalories = sCalories + food.getTotalCalories();
                    break;
            }
        }

        //calculate the remaining based on calculations above
        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        //adding values to piechart
        ArrayList<Entry> entries = new ArrayList<Entry>();
        if (bfCalories != 0) entries.add(new Entry(bfCalories, 0));
        if (lCalories != 0) entries.add(new Entry(lCalories, 1));
        if (dCalories != 0) entries.add(new Entry(dCalories, 2));
        if (sCalories != 0) entries.add(new Entry(sCalories, 3));

        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        //adding labels
        ArrayList<String> labels = new ArrayList<String>();
        if (bfCalories != 0) labels.add("Breakfast");
        if (lCalories != 0) labels.add("Lunch");
        if (dCalories != 0) labels.add("Dinner");
        if (sCalories != 0) labels.add("Snacks");

        //Piedata
        PieData data = new PieData(labels, dataset);

        pieChart.setData(data);
        pieChart.setDescription("");
        pieChart.animateY(1000);
    }

    public void setUpMacroChart (ArrayList<Food> todayList) {
        Log.v("here", "here");
        int carbs = 0;
        int fats = 0;
        int proteins = 0;

        //go through list and find the sums
        for (Food food : todayList) {
            carbs += food.getTotalCarbs();
            fats += food.getTotalFats();
            proteins += food.getTotalProteins();
        }

        //get the piechart
        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        //adding values to piechart
        ArrayList<Entry> entries = new ArrayList<Entry>();
        if (carbs != 0) entries.add(new Entry(carbs, 0));
        if (fats != 0) entries.add(new Entry(fats, 1));
        if (proteins != 0) entries.add(new Entry(proteins, 2));

        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        //adding labels
        ArrayList<String> labels = new ArrayList<String>();
        if (carbs != 0) labels.add("Carbohydrates");
        if (fats != 0) labels.add("Fats");
        if (proteins != 0) labels.add("Proteins");

        //Piedata
        PieData data = new PieData(labels, dataset);

        pieChart.setData(data);
        pieChart.setDescription("");
        pieChart.animateY(1000);
    }


}
