package edu.upenn.cis350.calorietracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Richard on 4/8/2016.
 * Activity class for the history page
 */
public class HistoryActivity extends AppCompatActivity {
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //set up the page adapter
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        HistoryAdapter adapter = new HistoryAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

    }

    //Return to mainScreen when back is pressed
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("listCanceled", true);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    /*
    Method to set current list for the breakdown of the piechart
     */
    public void onViewBreakdownButtonClick(View view) {
        //get the list necessary
        ArrayList<Food> currentList = new ArrayList<Food>();
        int position = pager.getCurrentItem();

        switch (position) {
            case 0:
                currentList = TrackableList.getTodayFoods();
                break;
            case 1:
                currentList = TrackableList.getD2Foods();
                break;
            case 2:
                currentList = TrackableList.getD3Foods();
                break;
            case 3:
                currentList = TrackableList.getD4Foods();
                break;
            case 4:
                currentList = TrackableList.getD5Foods();
                break;
            case 5:
                currentList = TrackableList.getD6Foods();
                break;
            case 6:
                currentList = TrackableList.getD7Foods();
                break;
            default:
                break;

        }

        //put into intent today's foods and the current goal
        Intent i = new Intent(this, MealChart.class);
        i.putExtra("List", currentList);
        startActivity(i);
    }
}
