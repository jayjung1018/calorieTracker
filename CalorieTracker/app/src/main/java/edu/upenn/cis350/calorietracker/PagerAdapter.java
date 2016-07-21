package edu.upenn.cis350.calorietracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Zachary on 3/26/16.
 * Class to implement tabs on foods
 */
public class PagerAdapter extends FragmentPagerAdapter {

    public static final String IS_EXERCISE = "CHOSEN_TRACKABLE";
    public static final String CHOSEN_LIST = "CHOSEN_LIST";
    public static final String PAGE_NUMBER = "PAGE_NUMBER";
    private boolean isExercise;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public PagerAdapter(FragmentManager fm, boolean isExercise) {
        super(fm);
        this.isExercise = isExercise;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch(position) {
            case 0: {
                if (isExercise) {
                    bundle.putSerializable(CHOSEN_LIST, TrackableList.getTodayExercises());
                }
                else {
                    bundle.putSerializable(CHOSEN_LIST, TrackableList.getTodayFoods());
                }
                break;
            }
            case 1: {
                if (isExercise) {
                    bundle.putSerializable(CHOSEN_LIST, TrackableList.getRecentExercises());
                }
                else {
                    bundle.putSerializable(CHOSEN_LIST, TrackableList.getRecentFoods());
                }
                break;
            }
            case 2: {
                if (isExercise) {
                    bundle.putSerializable(CHOSEN_LIST, TrackableList.getFavoriteExercises());
                }
                else {
                    bundle.putSerializable(CHOSEN_LIST, TrackableList.getFavoriteFoods());
                }
                break;
            }
            default: break;
        }
        bundle.putBoolean(IS_EXERCISE, this.isExercise);
        bundle.putInt(PAGE_NUMBER, position);
        TrackableListFragment trackableListFragment = new TrackableListFragment();
        trackableListFragment.setArguments(bundle);
        return trackableListFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Today";
        }
        else if (position == 1) {
            return "Recents";
        }
        else {
            return "Favorites";
        }
    }
}
