package edu.upenn.cis350.calorietracker;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Richard on 4/26/2016.
 */
public class NotificationActivity extends Activity {
    public static final int NewNotificationActivity_ID = 1;
    SharedPreferences prefs;
    ArrayAdapter<String> adapter;

    Set<String> alarmNames;
    HashMap<String, String> alarms;

    Set<String> deleted;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        prefs = getSharedPreferences("Notifications",0);

        Switch dailySwitch = (Switch) findViewById(R.id.dailySwitch);
        dailySwitch.setChecked(prefs.getBoolean("dailySwitch",false));

        //Context context = getApplicationContext();
        alarmNames = prefs.getStringSet("alarmNames",new HashSet<String>());
        alarms = new HashMap<String, String>();
        for(String v: alarmNames){
            String time = prefs.getString(v,"");
            if(!time.equals("")){
                alarms.put(v, prefs.getString(v,""));
            }
        }

        ListView listView = (ListView)findViewById(R.id.notificationList);
        ArrayList<String> values = new ArrayList<String>(alarmNames);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,values);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove(adapter.getItem(position));
                alarmNames.remove(adapter.getItem(position));
                deleted.add(adapter.getItem(position));
                adapter.remove(adapter.getItem(position));
                editor.putStringSet("alarmNames", alarmNames);
                editor.commit();
                adapter.notifyDataSetChanged();
                return true;
            }

        });

        deleted = new HashSet<String>();
    }

    public void onBackButtonClick(View view){
        Intent i = new Intent();
        ArrayList<String> deletedNotifications = new ArrayList<String>();
        deletedNotifications.addAll(deleted);
        i.putStringArrayListExtra("deleted", deletedNotifications);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onNewNotificationClick(View view){
        Intent i = new Intent(this, NewNotificationActivity.class);
        startActivityForResult(i, NewNotificationActivity_ID);
    }

    public void onDailySwitchClick(View view){
        Boolean switched = prefs.getBoolean("dailySwitch",false);
        Switch dailySwitch = (Switch) findViewById(R.id.dailySwitch);
        SharedPreferences.Editor editor = prefs.edit();
        dailySwitch.setChecked(!switched);
        editor.putBoolean("dailySwitch", !switched);
        editor.commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode){
            case NewNotificationActivity_ID:
                if(resultCode == RESULT_CANCELED){
                    break;
                }else {
                    String name = intent.getStringExtra("NOTIFICATION_NAME");
                    String time = intent.getStringExtra("NOTIFICATION_TIME");

                    alarms.put(name, time);
                    alarmNames.add(name);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(name, time);
                    editor.putStringSet("alarmNames", alarmNames);
                    editor.commit();
                    adapter.add(name);
                    adapter.notifyDataSetChanged();
                }
        }
    }
}
