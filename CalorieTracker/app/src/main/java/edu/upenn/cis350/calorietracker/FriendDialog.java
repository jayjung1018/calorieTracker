package edu.upenn.cis350.calorietracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jay Jung on 4/26/2016.
 */
public class FriendDialog extends android.app.DialogFragment {

    String fb_id;
    String name;
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(name);

        builder.setPositiveButton("Check Achievements", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //first read the database
                SQLiteDatabase db = MainActivity.mDbHelper.getReadableDatabase();

                String[] projection = {DatabaseContract.DataEntry.COLUMN_NAME_FBID,
                        DatabaseContract.DataEntry.COLUMN_NAME_APROG,
                        DatabaseContract.DataEntry.COLUMN_NAME_AFLAG};

                String selection = DatabaseContract.DataEntry.COLUMN_NAME_FBID + "=?";
                String[] selectionArgs = {fb_id};

                Log.v("fb", fb_id);

                //query
                Cursor c = db.query(
                        DatabaseContract.DataEntry.TABLE_NAME,  // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                      // The sort order
                );

                //achievement blobs
                c.moveToFirst();
                String id = c.getString(c.getColumnIndex((DatabaseContract.DataEntry.COLUMN_NAME_FBID)));
                Log.v("fromdb", id);
                byte[] aflags = c.getBlob(c.getColumnIndex((DatabaseContract.DataEntry.COLUMN_NAME_AFLAG)));
                byte[] aprogs = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_APROG));
                int[] achievementProgress = (int[]) Serializer.deserializeObject(aprogs);
                int[] achievementFlags = (int[]) Serializer.deserializeObject(aflags);

                Intent i = new Intent(getActivity(), AchievementsActivity.class);

                i.putExtra("Flags", achievementFlags);
                i.putExtra("Progress", achievementProgress);

                startActivity(i);
            }
        });

        builder.setNegativeButton("Check Progress", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //first read the database
                SQLiteDatabase db = MainActivity.mDbHelper.getReadableDatabase();

                String[] projection = {DatabaseContract.DataEntry.COLUMN_NAME_FBID,
                        DatabaseContract.DataEntry.COLUMN_NAME_D1,
                        DatabaseContract.DataEntry.COLUMN_NAME_D2,
                        DatabaseContract.DataEntry.COLUMN_NAME_D3,
                        DatabaseContract.DataEntry.COLUMN_NAME_D4,
                        DatabaseContract.DataEntry.COLUMN_NAME_D5,
                        DatabaseContract.DataEntry.COLUMN_NAME_D6,
                        DatabaseContract.DataEntry.COLUMN_NAME_D7,
                        DatabaseContract.DataEntry.COLUMN_NAME_D1E,
                        DatabaseContract.DataEntry.COLUMN_NAME_D2E,
                        DatabaseContract.DataEntry.COLUMN_NAME_D3E,
                        DatabaseContract.DataEntry.COLUMN_NAME_D4E,
                        DatabaseContract.DataEntry.COLUMN_NAME_D5E,
                        DatabaseContract.DataEntry.COLUMN_NAME_D6E,
                        DatabaseContract.DataEntry.COLUMN_NAME_D7E};

                String selection = DatabaseContract.DataEntry.COLUMN_NAME_FBID + "=?";
                String[] selectionArgs = {fb_id};

                Log.v("fb", fb_id);

                //query
                Cursor c = db.query(
                        DatabaseContract.DataEntry.TABLE_NAME,  // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                      // The sort order
                );

                //list blobs
                c.moveToFirst();
                String id = c.getString(c.getColumnIndex((DatabaseContract.DataEntry.COLUMN_NAME_FBID)));
                Log.v("fromdb", id);
                byte[] d1Blob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D1));
                byte[] d2Blob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D2));
                byte[] d3Blob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D3));
                byte[] d4Blob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D4));
                byte[] d5Blob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D5));
                byte[] d6Blob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D6));
                byte[] d7Blob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D7));
                byte[] d1eBlob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D1));
                byte[] d2eBlob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D2E));
                byte[] d3eBlob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D3E));
                byte[] d4eBlob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D4E));
                byte[] d5eBlob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D5E));
                byte[] d6eBlob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D6E));
                byte[] d7eBlob = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D7E));

                ArrayList<Food> d1List = (ArrayList<Food>) Serializer.deserializeObject(d1Blob);
                ArrayList<Food> d2List = (ArrayList<Food>) Serializer.deserializeObject(d2Blob);
                ArrayList<Food> d3List = (ArrayList<Food>) Serializer.deserializeObject(d3Blob);
                ArrayList<Food> d4List = (ArrayList<Food>) Serializer.deserializeObject(d4Blob);
                ArrayList<Food> d5List = (ArrayList<Food>) Serializer.deserializeObject(d5Blob);
                ArrayList<Food> d6List = (ArrayList<Food>) Serializer.deserializeObject(d6Blob);
                ArrayList<Food> d7List = (ArrayList<Food>) Serializer.deserializeObject(d7Blob);

                ArrayList<Exercise> d1eList =
                        (ArrayList<Exercise>) Serializer.deserializeObject(d1eBlob);
                ArrayList<Exercise> d2eList =
                        (ArrayList<Exercise>) Serializer.deserializeObject(d2eBlob);
                ArrayList<Exercise> d3eList =
                        (ArrayList<Exercise>) Serializer.deserializeObject(d3eBlob);
                ArrayList<Exercise> d4eList =
                        (ArrayList<Exercise>) Serializer.deserializeObject(d4eBlob);
                ArrayList<Exercise> d5eList =
                        (ArrayList<Exercise>) Serializer.deserializeObject(d5eBlob);
                ArrayList<Exercise> d6eList =
                        (ArrayList<Exercise>) Serializer.deserializeObject(d6eBlob);
                ArrayList<Exercise> d7eList =
                        (ArrayList<Exercise>) Serializer.deserializeObject(d7eBlob);

                Intent i = new Intent(getActivity(), ProgressActivity.class);
                i.putExtra("d1", d1List);
                i.putExtra("d2", d2List);
                i.putExtra("d3", d3List);
                i.putExtra("d4", d4List);
                i.putExtra("d5", d5List);
                i.putExtra("d6", d6List);
                i.putExtra("d7", d7List);
                i.putExtra("d1e", d1eList);
                i.putExtra("d2e", d2eList);
                i.putExtra("d3e", d3eList);
                i.putExtra("d4e", d4eList);
                i.putExtra("d5e", d5eList);
                i.putExtra("d6e", d6eList);
                i.putExtra("d7e", d7eList);

                startActivity(i);
            }
        });


        return builder.create();
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
