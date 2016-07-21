package edu.upenn.cis350.calorietracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Main activity that contains all of the main menu buttons and the caloric values for today
 */
public class MainActivity extends AppCompatActivity {

    //static final
    public static final int FOOD_ACTIVITY_ID = 1;
    public static final int CHOSEN_FROM_FOODLIST_ID = 2;
    public static final int EXERCISE_ACTIVITY_ID = 3;
    public static final int CHOSEN_FROM_EXERCISELIST_ID = 4;
    public static final int PROFILE_ID = 5;
    public static final int PROGRESS_ID = 6;
    public static final int HISTORY_ID = 7;
    public static final int NOTIFICATION_ID = 8;

    public static String fb_id;

    //calorie variables
    static int calorie = 2000;
    int food = 0;
    int exercise = 0;
    static int goal = 2000;
    int weight = 0;

    //food lists
    private Switch selectSwitch;

    //achievement tracker
    static int[] achievementFlags;
    static int[] achievementProgress;
    final int DEFICIT = 500;
    final int SURPLUS = 500;
    final int FOODFAV = 5;
    final int EXFAV = 5;

    //fb info
    private CallbackManager callbackManager;

    //databaseHelper to pull db -> make it static so others can use
    static DatabaseHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fb initialization
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //db initialization
        mDbHelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_main);

        selectSwitch = (Switch) findViewById(R.id.select);

        //food is false and exercise is true
        selectSwitch.setChecked(false);

        //set callbackmanager
        initializeLogout();

        //get user info from fb
        fb_id = getInfo();

        //initialize lists
        TrackableList.buildLists();

        //load the appropriate data from db
        loadData(fb_id);

        //update the view with the info
        updateView();

        //reset information at midnight
        reset();

        //check for surplus/deficit achievement status
        if (achievementFlags[1] == 0 && achievementProgress[1] > DEFICIT) {
            achievementFlags[1] = 1;
            updateDBAchievement(achievementFlags, DatabaseContract.DataEntry.COLUMN_NAME_AFLAG);

            UnlockDialog unlockDialog = new UnlockDialog();
            unlockDialog.show(getFragmentManager(), "unlocked");
        }
        Log.v("achievement", String.valueOf(achievementProgress[2]));

        if (achievementFlags[2] == 0 && achievementProgress[2] > SURPLUS) {
            achievementFlags[2] = 1;
            updateDBAchievement(achievementFlags, DatabaseContract.DataEntry.COLUMN_NAME_AFLAG);

            UnlockDialog unlockDialog = new UnlockDialog();
            unlockDialog.show(getFragmentManager(), "unlocked");
        }

        //create notifications for alarms stored in shared preferences
        runNotifications();
    }

    /**
     * Method to add food or exercise
     */
    public void onAddButtonClick(View view) {
        //put into intent if food or exercise
        Intent i = new Intent(this, AddTrackableActivity.class);
        i.putExtra("isExercise", selectSwitch.isChecked());
        i.putExtra("fb_id", fb_id);

        //food branch
        if (!selectSwitch.isChecked()) {
            startActivityForResult(i, FOOD_ACTIVITY_ID);
        }

        //exercise branch
        else {
            startActivityForResult(i, EXERCISE_ACTIVITY_ID);
        }
    }

    /**
     * Method to view current lists
     */
    public void onListButtonClick(View view) {
        //put all lists in intent - also check if food or exercise
        Intent i = new Intent(this, TabLayoutActivity.class);
        i.putExtra("lists", TrackableList.getAllLists());
        i.putExtra("isExercise", selectSwitch.isChecked());

        //food branch
        if (!selectSwitch.isChecked()) {
            startActivityForResult(i, CHOSEN_FROM_FOODLIST_ID);
        }

        //exercise branch
        else {
            startActivityForResult(i, CHOSEN_FROM_EXERCISELIST_ID);
        }
    }

    /**
     * Set profile button
     */
    public void onProfileButtonClick(View view) {
        //set intent and switch
        Intent i = new Intent(this, ProfileActivity.class);
        startActivityForResult(i, PROFILE_ID);
    }

    /**
     * Check progress button
     */
    public void onProgressButtonClick(View view) {
        //set intent and switch
        Intent i = new Intent(this, ProgressActivity.class);
        i.putExtra("d1", TrackableList.getTodayFoods());
        i.putExtra("d2", TrackableList.getD2Foods());
        i.putExtra("d3", TrackableList.getD3Foods());
        i.putExtra("d4", TrackableList.getD4Foods());
        i.putExtra("d5", TrackableList.getD5Foods());
        i.putExtra("d6", TrackableList.getD6Foods());
        i.putExtra("d7", TrackableList.getD7Foods());

        i.putExtra("d1e", TrackableList.getTodayExercises());
        i.putExtra("d2e", TrackableList.getD2Exercises());
        i.putExtra("d3e", TrackableList.getD3Exercises());
        i.putExtra("d4e", TrackableList.getD4Exercises());
        i.putExtra("d5e", TrackableList.getD5Exercises());
        i.putExtra("d6e", TrackableList.getD6Exercises());
        i.putExtra("d7e", TrackableList.getD7Exercises());


        startActivityForResult(i, PROGRESS_ID);
    }

    /**
     * Check achievements button
     */
    public void onAchievementsClick (View view) {
        //put into intent current progress and switch
        Intent i = new Intent(this, AchievementsActivity.class);
        i.putExtra("Flags", achievementFlags);
        i.putExtra("Progress", achievementProgress);
        startActivity(i);
    }

    /**
     * Check nutrition/meal distribution for the day
     */
    public void onNutritionClick (View view) {

    }

    public void onFriendsButtonClick(View view) {
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Intent intent = new Intent(MainActivity.this,FriendsList.class);
                        try {
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            intent.putExtra("jsondata", rawName.toString());
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();
    }

    public void onNotificationButtonClick(View view){
        Intent i = new Intent(this, NotificationActivity.class);
        startActivityForResult(i, NOTIFICATION_ID);
    }

    /**
     * Method for easter egg click
     */
    public void onEasterEggClick (View view) {
        //increment "progress" (how many times user clicked it"
        achievementProgress[7]++;

        //update db
        updateDBAchievement(achievementProgress, DatabaseContract.DataEntry.COLUMN_NAME_APROG);

        //switch flag and show the dialog for unlocking
        if (achievementFlags[7] == 0) {
            achievementFlags[7] = 1;

            //update db
            updateDBAchievement(achievementFlags, DatabaseContract.DataEntry.COLUMN_NAME_AFLAG);

            UnlockDialog unlockDialog = new UnlockDialog();
            unlockDialog.show(getFragmentManager(), "unlocked");
        }
    }

    /**
     * Method for history button
     */
    public void onHistoryButtonClick(View view){
        //set intent and switch
        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra("isExercise", selectSwitch.isChecked());
        startActivityForResult(i, HISTORY_ID);
    }

    /**
     * Method for returning from any activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        callbackManager.onActivityResult(requestCode, resultCode, intent);

        //based on activity code -
        switch (requestCode) {
            //coming back from addFood
            case FOOD_ACTIVITY_ID:
                returnFromFood(intent);
                break;

            //coming back from recent/favorites list
            case CHOSEN_FROM_FOODLIST_ID:
                returnFromChosenFromFoodList(intent);
                break;

            case EXERCISE_ACTIVITY_ID:
                returnFromExercise(intent);
                break;

            //coming back from exercise list
            case CHOSEN_FROM_EXERCISELIST_ID:
                returnFromChosenFromExerciseList(intent);
                break;

            //coming back from the profile activity
            case PROFILE_ID:
                returnFromProfile(intent);
                break;
            //coming back from history activity
            case HISTORY_ID:
                returnFromHistory(intent);
                break;
            //coming back from notification activity
            case NOTIFICATION_ID:
                returnFromNotification(intent);
                break;
            default:
                break;
        }

        updateView();
    }

    //coming back from notification activity
    private void returnFromNotification(Intent intent) {
        deleteNotifications(intent.getStringArrayListExtra("deleted"));
        runNotifications();
    }

    //coming back from history activity
    private void returnFromHistory(Intent intent) {
        boolean listCanceled  = intent.getBooleanExtra("listCanceled", true);
        if (!listCanceled) {
            //get the chosen food and start the add trackable activity with the selected item
            Trackable chosenFood = (Food) intent.getSerializableExtra("trackableFromList");
            Intent i = new Intent(this, AddTrackableActivity.class);
            i.putExtra("trackableFromList", chosenFood);
            i.putExtra("isExercise", false);
            i.putExtra("isEditing", intent.getIntExtra("isEditing", 0));

            startActivityForResult(i, FOOD_ACTIVITY_ID);
        }
    }

    //coming back from profile actiivty
    private void returnFromProfile(Intent intent) {
        boolean profileCanceled = intent.getBooleanExtra("profileCanceled", true);
        if (!profileCanceled) {
            //set the new goal based on the calculated goal
            int newGoal = intent.getIntExtra("NEW_CALORIES", 2000);
            goal = newGoal;

            //update goal to db
            updateDBGoal(newGoal);
            updateFromProfile(newGoal);
        }
    }

    //coming back from exercise list
    private void returnFromChosenFromExerciseList(Intent intent) {
        boolean exerciseListCanceled = intent.getBooleanExtra("listCanceled", true);
        if (!exerciseListCanceled) {
            //find the chosen exercise and feed it to the add trackable activity
            Trackable chosenExercise = (Exercise) intent.getSerializableExtra("trackableFromList");
            Intent i = new Intent(this, AddTrackableActivity.class);
            i.putExtra("trackableFromList", chosenExercise);
            i.putExtra("isExercise", true);
            i.putExtra("isEditing", intent.getIntExtra("isEditing", 0));
            i.putExtra("fb_id", fb_id);

            startActivityForResult(i, EXERCISE_ACTIVITY_ID);
        }
    }

    //coming back from exercise
    private void returnFromExercise(Intent intent) {
        boolean addExerciseCanceled = intent.getBooleanExtra("addCanceled", true);

        //if there was an exercise added, set the views
        if (!addExerciseCanceled) {
            //get the minutes
            TextView minutesView = (TextView) findViewById(R.id.minutesMain);

            String newMinutes = "" + TrackableList.getTodayTotalMinutes();

            //set the minutes
            minutesView.setText(newMinutes);

            //add exercise to recents list
            if (intent.getBooleanExtra("isNewFavorite",true)) {
                //check for achievements
                achievementProgress[5]++;
                updateDBAchievement(achievementProgress,
                        DatabaseContract.DataEntry.COLUMN_NAME_APROG);

                if (achievementFlags[5] == 0) {
                    if (achievementProgress[5] == EXFAV) {
                        achievementFlags[5] = 1;

                        //update DB
                        updateDBAchievement(achievementFlags,
                                DatabaseContract.DataEntry.COLUMN_NAME_APROG);

                        //dialog
                        UnlockDialog ud = new UnlockDialog();
                        ud.show(getFragmentManager(), "fifth");
                    }
                }
            }
        }
    }

    //coming back from food list
    private void returnFromChosenFromFoodList(Intent intent) {
        boolean listCanceled = intent.getBooleanExtra("listCanceled", true);
        if (!listCanceled) {
            //get the chosen food and start the add trackable activity with the selected item
            Trackable chosenFood = (Food) intent.getSerializableExtra("trackableFromList");
            Intent i = new Intent(this, AddTrackableActivity.class);
            i.putExtra("trackableFromList", chosenFood);
            i.putExtra("isExercise", false);
            i.putExtra("isEditing", intent.getIntExtra("isEditing", 0));
            i.putExtra("fb_id", fb_id);

            startActivityForResult(i, FOOD_ACTIVITY_ID);
        }
    }

    //Called when return to MainActivity from Food
    private void returnFromFood(Intent intent) {
        boolean addCanceled = intent.getBooleanExtra("addCanceled", true);

        //if there was a food added, set the views
        if (!addCanceled) {
            //check achievement
            achievementProgress[0]++;

            //update db
            updateDBAchievement(achievementProgress, DatabaseContract.DataEntry.COLUMN_NAME_APROG);

            if (achievementFlags[0] == 0) {
                achievementFlags[0] = 1;

                //update db
                updateDBAchievement(achievementFlags, DatabaseContract.DataEntry.COLUMN_NAME_AFLAG);

                //show dialog
                UnlockDialog unlockDialog = new UnlockDialog();
                unlockDialog.show(getFragmentManager(), "first");
            }

            //get the nutrients
            TextView fatsView = (TextView) findViewById(R.id.fatsMain);
            TextView carbsView = (TextView) findViewById(R.id.carbsMain);
            TextView proteinsView = (TextView) findViewById(R.id.proteinsMain);

            String newFats = "" + TrackableList.getTodayTotalFats();
            String newCarbs = "" + TrackableList.getTodayTotalCarbs();
            String newProteins = "" + TrackableList.getTodayTotalProteins();

            //set the nutrients
            fatsView.setText(newFats);
            carbsView.setText(newCarbs);
            proteinsView.setText(newProteins);

            if (intent.getBooleanExtra("isNewFavorite",true)) {

                //check for achievements
                achievementProgress[4]++;

                //update db
                updateDBAchievement(achievementProgress,
                        DatabaseContract.DataEntry.COLUMN_NAME_APROG);
                if (achievementFlags[4] == 0) {
                    if (achievementProgress[4] == FOODFAV) {
                        achievementFlags[4] = 1;

                        //update db
                        updateDBAchievement(achievementFlags,
                                DatabaseContract.DataEntry.COLUMN_NAME_AFLAG);

                        //show dialog
                        UnlockDialog ud = new UnlockDialog();
                        ud.show(getFragmentManager(), "fourth");
                    }
                }
            }
        }
    }

    private void deleteNotifications(ArrayList<String> deleted) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        for(String v: deleted){
            Intent alarmIntent = new Intent(this, NotificationReceiver.class);
            alarmIntent.putExtra("name", v);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
            alarmManager.cancel(pendingIntent);
        }
    }

    /**
     * Method to update the calorieView on the main page - feeds the caloric value, food intake, and
     * the goal values to the custom view to use and update
     */
    public void updateView() {
        CalorieView calorieView = (CalorieView) findViewById(R.id.calorieView);
        calorie = goal - TrackableList.getTodayTotalFoodCalories() +
                TrackableList.getTodayTotalExerciseCalories();
        food = TrackableList.getTodayTotalFoodCalories();
        exercise = TrackableList.getTodayTotalExerciseCalories();

        calorieView.goal = this.goal;
        calorieView.calories = this.calorie;
        calorieView.food = this.food;
        calorieView.exercise = this.exercise;
        calorieView.invalidate();
    }

    /**
     * Method to update the goal, caloric intake, and remaning values coming back from the profile
     * page
     */
    private void updateFromProfile(int newGoal) {
        CalorieView calorieView = (CalorieView) findViewById(R.id.calorieView);
        this.goal = newGoal;
        calorieView.goal = newGoal;
        calorieView.calories = newGoal - calorieView.food;
        calorieView.invalidate();
    }

    /**
     * Method to set the logout button - when clicked, just logout of fb
     */
    private void initializeLogout() {
        //setting logout button
        LoginButton loginbutton = (LoginButton) findViewById(R.id.login_button_main);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout when clicked
                LoginManager.getInstance().logOut();
                finish();
            }
        });
    }

    /**
     * FB API method to get the current user's information
     */
    private String getInfo() {
        //get profile info
        String username = getIntent().getStringExtra("name");
        String id = getIntent().getStringExtra("id");

        //set the text to the username
        TextView text = (TextView) findViewById(R.id.user);
        text.setText("Hello " + username + "!");

        return id;
    }

    /**
     * Method to reset values on the calendar
     */
    private void reset() {
        //set the values
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 10);
        //use the alarm
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        //feed to the resetReceiver class to handle
        Intent i = new Intent(this, ResetReceiver.class);
        i.putExtra("id", 1);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
    }


    private void runNotifications(){
        SharedPreferences prefs = getSharedPreferences("Notifications",0);
        Set<String> alarmNames = prefs.getStringSet("alarmNames", new HashSet<String>());
        HashMap<String,String> alarms = new HashMap<String, String>();
        if(alarmNames==null || alarmNames.isEmpty()){

        }else {
            for (String v : alarmNames) {
                String time = prefs.getString(v, "");
                if (!time.equals("")) {
                    alarms.put(v, prefs.getString(v, ""));
                }
            }

            for (String name : alarms.keySet()) {
                createNotification(name, alarms.get(name));
            }
        }
        if (prefs.getBoolean("dailySwitch", false)) {
            createNotification("daily alert", "21:00");
        }

    }

    private void createNotification(String name, String time){
        String[] hourMinute = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourMinute[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hourMinute[1]));
        Log.v("time: ", Integer.parseInt(hourMinute[0]) + " " + Integer.parseInt(hourMinute[1]));
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificationReceiver.class);
        alarmIntent.putExtra("name", name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
              AlarmManager.INTERVAL_DAY, pendingIntent);

        /* For demonstration purposes only
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES/15, pendingIntent);*/

    }

    public void loadData(String id) {
        //get db
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //set projection
        String[] projection = {
                DatabaseContract.DataEntry._ID,
                DatabaseContract.DataEntry.COLUMN_NAME_FBID,
                DatabaseContract.DataEntry.COLUMN_NAME_GOAL,
                DatabaseContract.DataEntry.COLUMN_NAME_WEIGHT,
                DatabaseContract.DataEntry.COLUMN_NAME_FAVFOOD,
                DatabaseContract.DataEntry.COLUMN_NAME_RECFOOD,
                DatabaseContract.DataEntry.COLUMN_NAME_FAVEX,
                DatabaseContract.DataEntry.COLUMN_NAME_RECEX,
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
                DatabaseContract.DataEntry.COLUMN_NAME_D7E,
                DatabaseContract.DataEntry.COLUMN_NAME_AFLAG,
                DatabaseContract.DataEntry.COLUMN_NAME_APROG};

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

        //if the fb_id exists - load the appropriate data
        if (c.moveToFirst()) {
            //get/set the goal
            goal = c.getInt(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_GOAL));
            weight = c.getInt(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_WEIGHT));

            //get the bytes
            byte[] favFoodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_FAVFOOD));
            byte[] recFoodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_RECFOOD));
            byte[] favExa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_FAVEX));
            byte[] recExa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_RECEX));

            //food blobs
            byte[] d1Foodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D1));
            byte[] d2Foodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D2));
            byte[] d3Foodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D3));
            byte[] d4Foodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D4));
            byte[] d5Foodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D5));
            byte[] d6Foodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D6));
            byte[] d7Foodsa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D7));

            //exercise blobs
            byte[] d1Exa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D1E));
            byte[] d2Exa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D2E));
            byte[] d3Exa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D3E));
            byte[] d4Exa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D4E));
            byte[] d5Exa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D5E));
            byte[] d6Exa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D6E));
            byte[] d7Exa = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_D7E));

            //achievement blobs
            byte[] aflags = c.getBlob(c.getColumnIndex((DatabaseContract.DataEntry.COLUMN_NAME_AFLAG)));
            byte[] aprogs = c.getBlob(c.getColumnIndex(DatabaseContract.DataEntry.COLUMN_NAME_APROG));

            //deserialize them back to their objects
            ArrayList<Food> favfoods = (ArrayList<Food>) Serializer.deserializeObject(favFoodsa);
            ArrayList<Food> recfoods = (ArrayList<Food>) Serializer.deserializeObject(recFoodsa);
            ArrayList<Exercise> favEx = (ArrayList<Exercise>) Serializer.deserializeObject(favExa);
            ArrayList<Exercise> recEx = (ArrayList<Exercise>) Serializer.deserializeObject(recExa);

            //food history
            ArrayList<Food> d1Foods = (ArrayList<Food>) Serializer.deserializeObject(d1Foodsa);
            ArrayList<Food> d2Foods = (ArrayList<Food>) Serializer.deserializeObject(d2Foodsa);
            ArrayList<Food> d3Foods = (ArrayList<Food>) Serializer.deserializeObject(d3Foodsa);
            ArrayList<Food> d4Foods = (ArrayList<Food>) Serializer.deserializeObject(d4Foodsa);
            ArrayList<Food> d5Foods = (ArrayList<Food>) Serializer.deserializeObject(d5Foodsa);
            ArrayList<Food> d6Foods = (ArrayList<Food>) Serializer.deserializeObject(d6Foodsa);
            ArrayList<Food> d7Foods = (ArrayList<Food>) Serializer.deserializeObject(d7Foodsa);

            //exercise history
            ArrayList<Exercise> d1Ex = (ArrayList<Exercise>) Serializer.deserializeObject(d1Exa);
            ArrayList<Exercise> d2Ex = (ArrayList<Exercise>) Serializer.deserializeObject(d2Exa);
            ArrayList<Exercise> d3Ex = (ArrayList<Exercise>) Serializer.deserializeObject(d3Exa);
            ArrayList<Exercise> d4Ex = (ArrayList<Exercise>) Serializer.deserializeObject(d4Exa);
            ArrayList<Exercise> d5Ex = (ArrayList<Exercise>) Serializer.deserializeObject(d5Exa);
            ArrayList<Exercise> d6Ex = (ArrayList<Exercise>) Serializer.deserializeObject(d6Exa);
            ArrayList<Exercise> d7Ex = (ArrayList<Exercise>) Serializer.deserializeObject(d7Exa);
            achievementProgress = (int[]) Serializer.deserializeObject(aprogs);
            achievementFlags = (int[]) Serializer.deserializeObject(aflags);

            //store into current trackable
            TrackableList.setAllLists(favfoods, recfoods, favEx, recEx, d1Foods, d2Foods, d3Foods,
                    d4Foods, d5Foods, d6Foods, d7Foods, d1Ex, d2Ex, d3Ex, d4Ex, d5Ex, d6Ex, d7Ex);
        }

        //if the fb_id doesn't exist - new user initialize row on db
        else initializeRow(id);
        c.close();
    }

    public void initializeRow(String id) {
        //get the database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //set values
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_FBID, id);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_GOAL, 2000);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_WEIGHT, 0);
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_FAVFOOD,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_RECFOOD,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_FAVEX,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_RECEX,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D1,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D2,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D3,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D4,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D5,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D6,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D7,
                Serializer.serializeObject(new ArrayList<Food>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D1E,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D2E,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D3E,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D4E,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D5E,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D6E,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_D7E,
                Serializer.serializeObject(new ArrayList<Exercise>()));
        int[] flags = {0 ,0, 0, 0, 0, 0, 0, 0};
        int[] progress = {0 ,0, 0, 0, 0, 0, 0, 0};
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_AFLAG,
                Serializer.serializeObject(flags));
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_APROG,
                Serializer.serializeObject(progress));

        //insert values
        db.insert(DatabaseContract.DataEntry.TABLE_NAME, null, values);

        //initialize achievment
        achievementFlags = flags;
        achievementProgress = progress;
    }

    public static void updateDBAchievement (int[] array, String type) {
        //call db
        SQLiteDatabase db = MainActivity.mDbHelper.getReadableDatabase();

        //serialize the array
        byte[] blob = Serializer.serializeObject(array);

        //set the new value
        ContentValues values = new ContentValues();
        values.put(type, blob);

        //select query
        String selection = DatabaseContract.DataEntry.COLUMN_NAME_FBID + "=?";
        String[] selectionArgs = {fb_id};

        //update db
        db.update(
                DatabaseContract.DataEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public static void updateDBGoal (int goal) {
        //call db
        SQLiteDatabase db = MainActivity.mDbHelper.getReadableDatabase();

        //set the new value
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DataEntry.COLUMN_NAME_GOAL, goal);

        //select query
        String selection = DatabaseContract.DataEntry.COLUMN_NAME_FBID + "=?";
        String[] selectionArgs = {fb_id};

        //update db
        db.update(
                DatabaseContract.DataEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}