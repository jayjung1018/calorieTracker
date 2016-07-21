package edu.upenn.cis350.calorietracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jay Jung on 4/21/2016.
 */
public class HistoryAdapter extends FragmentPagerAdapter {

    HistoryListFragment hlf;
    public static final String CHOSEN_LIST = "CHOSEN_LIST";
    public static final String PAGE_NUMBER = "PAGE_NUMBER";

    public HistoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        ArrayList<Trackable> currentList = new ArrayList<Trackable>();

        //get the list based on page position
        currentList = getList(position);

        //put list and position into bundle
        bundle.putSerializable(CHOSEN_LIST, currentList);
        bundle.putInt(PAGE_NUMBER, position);

        //start the list fragment
        hlf = new HistoryListFragment();
        hlf.setArguments(bundle);
        return hlf;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        DateFormat df = new SimpleDateFormat("MM/dd");
        Calendar cal = Calendar.getInstance();

        switch(position) {
            case 0:
                return "Today (" + df.format(cal.getTime()) + ")";
            case 1:
                cal.add(Calendar.DATE, -1);
                return "Yesterday (" + df.format(cal.getTime()) + ")";
            case 2:
                cal.add(Calendar.DATE, -2);
                return df.format(cal.getTime());
            case 3:
                cal.add(Calendar.DATE, -3);
                return df.format(cal.getTime());
            case 4:
                cal.add(Calendar.DATE, -4);
                return df.format(cal.getTime());
            case 5:
                cal.add(Calendar.DATE, -5);
                return df.format(cal.getTime());
            case 6:
                cal.add(Calendar.DATE, -6);
                return df.format(cal.getTime());
            default:
                break;
        }
        return "";
    }

    public ArrayList<Trackable> getList(int position) {
        ArrayList<Food> foodList;
        ArrayList<Exercise> exList;
        ArrayList<Trackable> result = new ArrayList<Trackable>();

        //get the column name from position
        switch(position) {
            case 0:
                //get each list
                foodList = TrackableList.getTodayFoods();
                exList = TrackableList.getTodayExercises();

                //join the two lists
                result.addAll(foodList);
                result.addAll(exList);
                break;
            case 1:
                //get each list
                foodList = TrackableList.getD2Foods();
                exList = TrackableList.getD2Exercises();

                //join the two lists
                result.addAll(foodList);
                result.addAll(exList);
                break;
            case 2:
                //get each list
                foodList = TrackableList.getD3Foods();
                exList = TrackableList.getD3Exercises();

                //join the two lists
                result.addAll(foodList);
                result.addAll(exList);
                break;
            case 3:
                //get each list
                foodList = TrackableList.getD4Foods();
                exList = TrackableList.getD4Exercises();

                //join the two lists
                result.addAll(foodList);
                result.addAll(exList);
                break;
            case 4:
                //get each list
                foodList = TrackableList.getD5Foods();
                exList = TrackableList.getD5Exercises();

                //join the two lists
                result.addAll(foodList);
                result.addAll(exList);
                break;
            case 5:
                //get each list
                foodList = TrackableList.getD6Foods();
                exList = TrackableList.getD6Exercises();

                //join the two lists
                result.addAll(foodList);
                result.addAll(exList);
                break;
            case 6:
                //get each list
                foodList = TrackableList.getD7Foods();
                exList = TrackableList.getD7Exercises();

                //join the two lists
                result.addAll(foodList);
                result.addAll(exList);
                break;
            default:
                break;
        }
        return result;
    }

    ArrayList<Food> getCurrentFoods() {
        return hlf.getListChoice();
    }

}
