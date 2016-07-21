package edu.upenn.cis350.calorietracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GridLabelRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jay Jung on 3/23/16.
 * Activity to display progress graphs
 */
public class ProgressActivity extends AppCompatActivity {
    ArrayList<Food> d1;
    ArrayList<Food> d2;
    ArrayList<Food> d3;
    ArrayList<Food> d4;
    ArrayList<Food> d5;
    ArrayList<Food> d6;
    ArrayList<Food> d7;

    ArrayList<Exercise> d1e;
    ArrayList<Exercise> d2e;
    ArrayList<Exercise> d3e;
    ArrayList<Exercise> d4e;
    ArrayList<Exercise> d5e;
    ArrayList<Exercise> d6e;
    ArrayList<Exercise> d7e;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progress);
        GraphView graph = (GraphView) findViewById(R.id.graph);

        //get the lists
        getListsFromIntent();

        //get the lists of foods for each day
        int d1C = calculateTotalCalories(d1);
        int d2C = calculateTotalCalories(d2);
        int d3C = calculateTotalCalories(d3);
        int d4C = calculateTotalCalories(d4);
        int d5C = calculateTotalCalories(d5);
        int d6C = calculateTotalCalories(d6);
        int d7C = calculateTotalCalories(d7);

        int d1Ce = calculateTotalExercise(d1e);
        int d2Ce = calculateTotalExercise(d2e);
        int d3Ce = calculateTotalExercise(d3e);
        int d4Ce = calculateTotalExercise(d4e);
        int d5Ce = calculateTotalExercise(d5e);
        int d6Ce = calculateTotalExercise(d6e);
        int d7Ce = calculateTotalExercise(d7e);

        //dates
        Calendar cal = Calendar.getInstance();
        Date d1D = cal.getTime();

        cal.add(Calendar.DATE, -1);
        Date d2D = cal.getTime();

        cal.add(Calendar.DATE, -1);
        Date d3D = cal.getTime();

        cal.add(Calendar.DATE, -1);
        Date d4D = cal.getTime();

        cal.add(Calendar.DATE, -1);
        Date d5D = cal.getTime();

        cal.add(Calendar.DATE, -1);
        Date d6D = cal.getTime();

        cal.add(Calendar.DATE, -1);
        Date d7D = cal.getTime();

        Log.v("hello", String.valueOf(d1C));
        // Data Points are hard-coded.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d7D, d7C),
                new DataPoint(d6D, d6C),
                new DataPoint(d5D, d5C),
                new DataPoint(d4D, d4C),
                new DataPoint(d3D, d3C),
                new DataPoint(d2D, d2C),
                new DataPoint(d1D, d1C)
        });

        // Data Points are hard-coded.
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d7D, d7Ce),
                new DataPoint(d6D, d6Ce),
                new DataPoint(d5D, d5Ce),
                new DataPoint(d4D, d4Ce),
                new DataPoint(d3D, d3Ce),
                new DataPoint(d2D, d2Ce),
                new DataPoint(d1D, d1Ce)
        });

        //for legends
        series.setTitle("Calorie Intake from Food");
        series2.setTitle("Calorie Burned from Exercise");

        series.setColor(Color.BLUE);
        series.setColor(Color.RED);

        graph.addSeries(series);
        graph.addSeries(series2);

        //set grid labels
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Calories");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Days");

        //graph API to set mins and maxes on axes
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(d7D.getTime());
        graph.getViewport().setMaxX(d1D.getTime());

        graph.getViewport().setYAxisBoundsManual(true);

        //get the max of all the cals
        int max = findMax(d1C, d2C, d3C, d4C, d5C, d6C, d7C);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(max + 200);

        //legend
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    public int calculateTotalCalories(ArrayList<Food> list) {
        int totalCalories = 0;
        for (Food f : list) {
            totalCalories += f.getTotalCalories();
        }

        return totalCalories;
    }

    public int calculateTotalExercise(ArrayList<Exercise> list) {
        int totalCalories = 0;
        for (Exercise e : list) {
            totalCalories += e.getCalories();
        }

        return totalCalories;
    }

    public void onBackButtonClick(View view) {
        Intent i = new Intent();
        setResult(RESULT_OK, i);

        finish();
    }

    int findMax(int d1, int d2, int d3, int d4, int d5, int d6, int d7) {
        int[] calArray = {d1, d2, d3, d4, d5, d6, d7};

        int max = 0;

        for (int i : calArray) {
            if (i > max) max = i;
        }

        return max;
    }

    public void getListsFromIntent () {
        Intent i = getIntent();
        d1 = (ArrayList<Food>) i.getSerializableExtra("d1");
        d2 = (ArrayList<Food>) i.getSerializableExtra("d2");
        d3 = (ArrayList<Food>) i.getSerializableExtra("d3");
        d4 = (ArrayList<Food>) i.getSerializableExtra("d4");
        d5 = (ArrayList<Food>) i.getSerializableExtra("d5");
        d6 = (ArrayList<Food>) i.getSerializableExtra("d6");
        d7 = (ArrayList<Food>) i.getSerializableExtra("d7");
        d1e = (ArrayList<Exercise>) i .getSerializableExtra("d1e");
        d2e = (ArrayList<Exercise>) i .getSerializableExtra("d2e");
        d3e = (ArrayList<Exercise>) i .getSerializableExtra("d3e");
        d4e = (ArrayList<Exercise>) i .getSerializableExtra("d4e");
        d5e = (ArrayList<Exercise>) i .getSerializableExtra("d5e");
        d6e = (ArrayList<Exercise>) i .getSerializableExtra("d6e");
        d7e = (ArrayList<Exercise>) i .getSerializableExtra("d7e");
    }

}
