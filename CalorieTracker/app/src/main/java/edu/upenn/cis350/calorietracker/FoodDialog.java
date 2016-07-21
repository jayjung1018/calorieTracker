package edu.upenn.cis350.calorietracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.facebook.share.widget.ShareDialog;

/**
 * Created by Jay Jung on 4/8/2016.
 * Dialog that pops up when clicked on a food or exercise on a list
 */
public class FoodDialog extends android.support.v4.app.DialogFragment{
    String note = "";
    String title = "";
    Trackable item;
    ListFragment lf;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        //build the alert dialog with title/message
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(note);

        //set the positive button to add food
        builder.setPositiveButton("Add Food", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent();
                i.putExtra("listCanceled", false);
                i.putExtra("trackableFromList", item);
                i.putExtra("isEditing", 0);
                getActivity().setResult(Activity.RESULT_OK, i);
                getActivity().finish();
            }
        });
        //set negative button to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    /**
     * Setter methods for dialog
     */
    public void setNote(String note) {
        this.note = note;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTrackable(Trackable trackable) {
        this.item = trackable;
    }

    public void setlf(ListFragment lf) {
        this.lf = lf;
    }
}