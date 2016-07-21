package edu.upenn.cis350.calorietracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * Created by Zachary on 3/26/16.
 * Activity class for laying out tabs
 */
public class TabLayoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        //get the intent if its exercise or food
        boolean isExercise = getIntent().getBooleanExtra("isExercise",true);

        //set up the page adapter
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                isExercise);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("listCanceled", true);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
