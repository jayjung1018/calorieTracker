package edu.upenn.cis350.calorietracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by Jay Jung on 4/24/2016.
 */
public class ResetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Log:", "Alarm Received");

        int alarmid = intent.getIntExtra("id", -1);

        if (alarmid > 0) {
            //alarm received -> reset today's, shift everything over and update db
            TrackableList.shiftLists();

            //update deficit/surplus achievements
            int net = MainActivity.calorie;

            if (net > 0) MainActivity.achievementProgress[1] += net;
            else MainActivity.achievementProgress[2] += (-1 * net);

            Log.v("surplus", String.valueOf(MainActivity.achievementProgress[2]));

            MainActivity.updateDBAchievement(MainActivity.achievementProgress,
                    DatabaseContract.DataEntry.COLUMN_NAME_APROG);
        }
    }
}
