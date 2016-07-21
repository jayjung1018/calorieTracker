package edu.upenn.cis350.calorietracker;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Jay Jung on 3/24/2016.
 * Activity class for achievements
 */
public class AchievementsActivity extends AppCompatActivity{
    //the thresholds for achievement numbers
    final int DEFICIT = 500;
    final int SURPLUS = 500;
    final int EXHOURS = 100;
    final int FOODFAV = 5;
    final int EXFAV = 5;

    int[] unlock;
    int[] progress;

    /*
    On create method - gets the values from the main activity intent
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        //get intents
        unlock = getIntent().getIntArrayExtra("Flags");
        progress = getIntent().getIntArrayExtra("Progress");

        //show appropriate icons
        checkAchievements();
    }

    //method to check which achievements have been unlocked
    protected void checkAchievements () {
        //go through each unlocked draw the appropriate image
        for (int i = 0; i < unlock.length; i++) {
            drawPics(i);
        }
    }

    /*
    Method to draw pictures based on unlock flags
     */
    public void drawPics(int i) {
        //initialize necessary components
        int[] pictures = {R.drawable.meal, R.drawable.fire, R.drawable.bulky, R.drawable.dumbbell,
                          R.drawable.chefhat, R.drawable.runningshoes, R.drawable.achievement,
                          R.drawable.easteregg};

        int[] views = {R.id.imageView0, R.id.imageView1, R.id.imageView2, R.id.imageView3,
                       R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7};

        if (unlock[i] != 0) {
            ImageView iv = (ImageView) findViewById(views[i]);
            iv.setImageResource(pictures[i]);
        }
    }

    /*
    Set Question marks for dialog
     */
    public void setQs(AchievementDialog dialog) {
        dialog.setTitle("???");
        dialog.setMessage("???");
    }
    /*
    Method triggered when an achievement item is clicked on
     */
    public void onItemClick(View view) {
        ImageView clicked = (ImageView) view;
        AchievementDialog dialog = new AchievementDialog();
        //if the achievement is unlocked - show what it is, else ???????
        switch (clicked.getId()) {
            case(R.id.imageView0):
                dialog.setUnlocked(unlock[0]);
                if (unlock[0] == 0) {
                    setQs(dialog);
                }
                else {
                    dialog.setMessage("Recorded your first meal! \nRecorded meals : " +
                            progress[0]);
                    dialog.setTitle("Newbie");
                    dialog.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.meal));
                }
                break;
            case(R.id.imageView1):
                dialog.setUnlocked(unlock[1]);
                if (unlock[1] == 0) {
                    setQs(dialog);
                }
                else {
                    dialog.setTitle("Shredded");
                    dialog.setMessage("You've collected a deficit of " + DEFICIT +
                            " calories! \nCurrent: " + progress[1]);
                    dialog.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.fire));
                }
                break;
            case(R.id.imageView2):
                dialog.setUnlocked(unlock[2]);
                if (unlock[2] == 0) {
                    setQs(dialog);
                }
                else {
                    dialog.setTitle("Bulky");
                    dialog.setMessage("You've collected a surplus of " + SURPLUS +
                            " calories! \nCurrent: " + progress[2]);
                    dialog.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.bulky));
                }
                break;
            case(R.id.imageView3):
                dialog.setUnlocked(unlock[3]);
                if (unlock[3] == 0) {
                    setQs(dialog);
                }
                else {
                    dialog.setTitle("Fitness Guru");
                    dialog.setMessage("You've logged in more than " + EXHOURS +
                            " hours of excercise! \nCurrent: " + progress[3]);
                    dialog.setImage(BitmapFactory.decodeResource(getResources(),
                            R.drawable.dumbbell));
                }
                break;
            case(R.id.imageView4):
                dialog.setUnlocked(unlock[4]);
                if (unlock[4] == 0) {
                    setQs(dialog);
                }
                else {
                    dialog.setTitle("Foodie");
                    dialog.setMessage("You've added more than " + FOODFAV +
                            " foods to your favorites! \nCurrent: " + progress[4]);
                    dialog.setImage(BitmapFactory.decodeResource(getResources(),
                            R.drawable.chefhat));
                }
                break;
            case(R.id.imageView5):
                dialog.setUnlocked(unlock[5]);
                if (unlock[5] == 0) {
                    setQs(dialog);
                }
                else {
                    dialog.setTitle("Exercisaholic");
                    dialog.setMessage("You've added more than " + EXFAV +
                            " exercises to your favorites! \nCurrent: " + progress[5]);
                    dialog.setImage(BitmapFactory.decodeResource(getResources(),
                            R.drawable.runningshoes));
                }
                break;
            case(R.id.imageView6):
                dialog.setUnlocked(unlock[6]);
                if (unlock[6] == 0) {
                    setQs(dialog);
                }
                else {
                    dialog.setTitle("Achievement Hunter");
                    dialog.setMessage("You've shared an achievement on your wall! \nShared Posts: "
                            + progress[6]);
                    dialog.setImage(BitmapFactory.decodeResource(getResources(),
                            R.drawable.achievement));
                }
                break;
            case(R.id.imageView7):
                dialog.setUnlocked(unlock[7]);
                if (unlock[7] == 0) {
                    setQs(dialog);
                }
                else {
                    dialog.setTitle("Easter Egg");
                    dialog.setMessage("You've found the Easter Egg! \nClicked Egg " +
                            progress[7] + " times");
                    dialog.setImage(BitmapFactory.decodeResource(getResources(),
                            R.drawable.easteregg));
                }
                break;
            default:
                break;
        }
        dialog.show(getFragmentManager(), "dialog");

    }

    /*
    Method for back click
     */
    public void onBackClick(View view) {
        finish();
    }

}
