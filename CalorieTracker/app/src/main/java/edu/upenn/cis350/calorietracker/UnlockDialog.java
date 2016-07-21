package edu.upenn.cis350.calorietracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Jay Jung on 3/24/2016.
 * Unlock dialog that shows up when achievements are unlocked
 */
public class UnlockDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        //build dialog with title and message
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Achievement Unlocked!!!");
        builder.setMessage("Go to the achievements page to view what you've unlocked!");

        //set positive button to "ok" - default but doesn't do anything
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

}
