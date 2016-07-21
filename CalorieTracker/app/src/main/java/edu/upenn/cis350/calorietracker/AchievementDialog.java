package edu.upenn.cis350.calorietracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Activity class for the dialogs to use for achievement popups
 * Created by Jay Jung on 3/24/2016.
 */
public class AchievementDialog extends DialogFragment {

    String title;
    String message;
    Bitmap image;
    ShareDialog shareDialog;
    int unlocked;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        //initialize facebook dialog
        shareDialog = new ShareDialog(this);

        //set up the dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //default is close
            }
        });
        if (unlocked == 1) {
            builder.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ShareDialog.canShow(SharePhotoContent.class)) {
                        //build the facebook dialog within
                        SharePhoto.Builder builder = new SharePhoto.Builder();
                        builder = builder.setBitmap(image);

                        //get the photo from builder
                        SharePhoto photo = builder.build();
                        SharePhotoContent.Builder contentBuilder = new SharePhotoContent.Builder();

                        //add photo
                        contentBuilder = contentBuilder.addPhoto(photo);
                        SharePhotoContent content = contentBuilder.build();
                        shareDialog.show(content);

                        //check achievement
                        //increment "progress" (how many times user has shared)
                        MainActivity.achievementProgress[6]++;

                        //update db
                        MainActivity.updateDBAchievement(MainActivity.achievementProgress,
                                DatabaseContract.DataEntry.COLUMN_NAME_APROG);

                        //switch flag and show the dialog for unlocking
                        if (MainActivity.achievementFlags[6] == 0) {
                            MainActivity.achievementFlags[6] = 1;

                            //update db
                            MainActivity.updateDBAchievement(MainActivity.achievementFlags,
                                    DatabaseContract.DataEntry.COLUMN_NAME_AFLAG);

                            UnlockDialog unlockDialog = new UnlockDialog();
                            unlockDialog.show(getFragmentManager(), "unlocked");
                        }
                    }
                }
            });
        }
        return builder.create();
    }

    /**
     * Setter methods for the dialog fields (dialogs are recommended not to have constructors)
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setUnlocked(int unlocked) {
        this.unlocked = unlocked;
    }
}
