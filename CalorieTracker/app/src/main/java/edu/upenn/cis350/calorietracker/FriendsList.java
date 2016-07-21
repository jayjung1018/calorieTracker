package edu.upenn.cis350.calorietracker;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jay Jung on 4/25/2016.
 */
public class FriendsList extends ListActivity {

    JSONArray friendslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

        ArrayList<String> friends = new ArrayList<String>();
        try {
            friendslist = new JSONArray(jsondata);
            for (int l=0; l < friendslist.length(); l++) {
                friends.add(friendslist.getJSONObject(l).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("friends", friends.toString());
        // adapter which populate the friends in listview
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row_friend, friends);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        try {
            JSONObject friend = friendslist.getJSONObject(position);
            String fb_id = friend.getString("id");
            String name = friend.getString("name");

            if (checkID(fb_id)) {
                FriendDialog fd = new FriendDialog();
                fd.setFb_id(fb_id);
                fd.setName(name);
                fd.show(getFragmentManager(), "dialog");
            }

            else {
                Toast alert = Toast.makeText(this, "Friend has not used the app yet!",
                        Toast.LENGTH_LONG);

                alert.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean checkID(String id) {
        //get db
        DatabaseHelper helper = MainActivity.mDbHelper;
        SQLiteDatabase db = helper.getReadableDatabase();

        //set projection
        String[] projection = {DatabaseContract.DataEntry.COLUMN_NAME_FBID};

        //set selections
        String selection = DatabaseContract.DataEntry.COLUMN_NAME_FBID + "=?";
        String[] selectionArgs = {id};
        String sortOrder = DatabaseContract.DataEntry._ID + " DESC";

        //query
        Cursor c = db.query(
                DatabaseContract.DataEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return c.moveToFirst();
    }

}
