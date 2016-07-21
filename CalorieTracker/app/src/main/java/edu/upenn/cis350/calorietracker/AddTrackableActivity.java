package edu.upenn.cis350.calorietracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by JayJung on 2/27/2016.
 * Activity class to add the currently selected trackable activity - food or exercise
 */
public class AddTrackableActivity extends AppCompatActivity {
    private Food foodInput;
    private Exercise exerciseInput;
    private boolean inputChoice;
    private String fb_id;
    private int isEditing = 0;
    private static int RESULT_LOAD_IMAGE = 1;
    final int EXHOURS = 100;

    /**
     * Oncreate default activity - initializes all of the fields
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get intents
        inputChoice = getIntent().getBooleanExtra("isExercise",true);
        isEditing = getIntent().getIntExtra("isEditing", 0);
        fb_id = getIntent().getStringExtra("fb_id");

        //code block for setting initial fields (if coming from a list or editing
        //food branch
        if (!inputChoice) {
            setContentView(R.layout.activity_food);
            //initialize spinner
            Spinner typeSpinner = (Spinner) findViewById(R.id.type);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.mealtypes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSpinner.setAdapter(adapter);

            if (getIntent().hasExtra("trackableFromList")) {
                // set all of the fields here if coming from a list
                foodInput = (Food) getIntent().getSerializableExtra("trackableFromList");
                EditText nameField = (EditText) findViewById(R.id.name);
                EditText calorieField = (EditText) findViewById(R.id.calories);
                EditText fatField = (EditText) findViewById(R.id.fats);
                EditText carbField = (EditText) findViewById(R.id.carbs);
                EditText proteinField = (EditText) findViewById(R.id.proteins);
                EditText servingSizeField = (EditText) findViewById(R.id.servingSize);
                CheckBox favoriteCheckbox = (CheckBox) findViewById(R.id.favoriteCheckbox);
                nameField.setText(foodInput.getName());
                calorieField.setText("" + foodInput.getCalories());
                fatField.setText("" + foodInput.getFatsPerServing());
                carbField.setText("" + foodInput.getCarbsPerServing());
                proteinField.setText("" + foodInput.getProteinsPerServing());
                servingSizeField.setText("" + foodInput.getServingSize());
                if (isEditing != 0) {
                    favoriteCheckbox.setChecked(foodInput.isFavorite());
                }
                else {
                    favoriteCheckbox.setChecked(false);
                }
            }
        }
        //exercise branch
        else {
            setContentView(R.layout.activity_exercise);
            if (getIntent().hasExtra("trackableFromList")) {
                //set all of the fields if coming from a list
                exerciseInput = (Exercise) getIntent().getSerializableExtra("trackableFromList");
                EditText nameField = (EditText) findViewById(R.id.name);
                EditText calorieField = (EditText) findViewById(R.id.calories);
                EditText minutesField = (EditText) findViewById(R.id.minutes);
                EditText intensityField = (EditText) findViewById(R.id.intensity);
                CheckBox favoriteCheckbox = (CheckBox) findViewById(R.id.favoriteCheckbox);
                nameField.setText(exerciseInput.getName());
                calorieField.setText("" + exerciseInput.getCalories());
                minutesField.setText("" + exerciseInput.getMinutes());
                intensityField.setText("" + exerciseInput.getIntensity());
                if (isEditing != 0) {
                    favoriteCheckbox.setChecked(exerciseInput.isFavorite());
                }
                else {
                    favoriteCheckbox.setChecked(false);
                }
            }
        }
    }

    public void onCheckboxClicked(View view) {
    }

    /**
     * Method for confirming the add food activity
     */
    public void onConfirmButtonClick(View view) {
        //get inputs
        EditText nameField = (EditText) findViewById(R.id.name);
        EditText calorieField = (EditText) findViewById(R.id.calories);
        EditText notesField = (EditText) findViewById(R.id.notes);

        //input check
        if (calorieField.getText().toString().equals("")) {
            Toast.makeText(this, "Must input a calorie!", Toast.LENGTH_LONG).show();
            return;
        }
        if (nameField.getText().toString().equals("")) {
            Toast.makeText(this, "Must input a name!", Toast.LENGTH_LONG).show();
            return;
        }

        //get the inputs common to both trackables
        String nameInput = nameField.getText().toString();
        String notesInput = notesField.getText().toString();
        int calorieInput = Integer.parseInt(calorieField.getText().toString());
        Trackable newTrackable;

        //if food
        if (!inputChoice) {
            newTrackable = addFood(nameInput, notesInput, calorieInput);
        }
        //exercise
        else newTrackable = addExercise(nameInput, notesInput, calorieInput);

        //check if adding succeeds first

        if (newTrackable == null) return;
        //make intent
        Intent i = new Intent();

        //put the new trackable into the main activity
        i.putExtra("addCanceled", false);
        i.putExtra("isNewFavorite", newTrackable.isFavorite());
        setResult(RESULT_OK, i);
        finish();
    }

    public Trackable addExercise(String nameInput, String notesInput, int calorieInput) {
        //set new trackable
        Trackable newTrackable;

        //get the fields
        EditText minutesField = (EditText) findViewById(R.id.minutes);
        EditText intensityField = (EditText) findViewById(R.id.intensity);
        CheckBox favoriteCheckbox = (CheckBox) findViewById(R.id.favoriteCheckbox);
        int minutesInput;
        String intensityInput;

        //set default to 0 if no input
        if (minutesField.getText().toString().equals("")) {
            minutesInput = 0;
        }
        else {
            minutesInput = Integer.parseInt(minutesField.getText().toString());
        }

        //intensity
        intensityInput = intensityField.getText().toString();

        //create the new exercise based on the input fields
        newTrackable = new Exercise(nameInput,calorieInput,minutesInput, intensityInput,
                favoriteCheckbox.isChecked(),notesInput);

        //check for exercise achievement
        MainActivity.achievementProgress[3] += minutesInput;
        MainActivity.updateDBAchievement(MainActivity.achievementProgress,
                DatabaseContract.DataEntry.COLUMN_NAME_APROG);

        //check for unlock - if unlocked, set flag and show appropriate achievement
        if (MainActivity.achievementFlags[3] == 0) {
            if (MainActivity.achievementProgress[3] >= EXHOURS) {
                MainActivity.achievementFlags[3] = 1;

                //update database
                MainActivity.updateDBAchievement(MainActivity.achievementFlags,
                        DatabaseContract.DataEntry.COLUMN_NAME_AFLAG);

                //dialogs
                UnlockDialog ud = new UnlockDialog();
                ud.show(getFragmentManager(), "third");
            }
        }

        //today edit
        if (isEditing == 1) {
            TrackableList.editTodayExercise(exerciseInput, (Exercise) newTrackable);
            updateDB(null, TrackableList.getTodayExercises(),
                    DatabaseContract.DataEntry.COLUMN_NAME_D1E);
        }

        //favorite edit
        else if (isEditing == 2) {
            TrackableList.editFavoriteExercise(exerciseInput,(Exercise)newTrackable);
            updateDB(null, TrackableList.getFavoriteExercises(),
                    DatabaseContract.DataEntry.COLUMN_NAME_FAVEX);
        }

        //if not editing, just add to lists
        else {
            TrackableList.addTodayExercise((Exercise)newTrackable);
            updateDB(null, TrackableList.getTodayExercises(),
                    DatabaseContract.DataEntry.COLUMN_NAME_D1E);
            TrackableList.addRecentExercise((Exercise) newTrackable);
            updateDB(null, TrackableList.getRecentExercises(),
                    DatabaseContract.DataEntry.COLUMN_NAME_RECEX);
            if (newTrackable.isFavorite()) {
                TrackableList.addFavoriteExercise((Exercise)newTrackable);
                updateDB(null, TrackableList.getFavoriteExercises(),
                        DatabaseContract.DataEntry.COLUMN_NAME_FAVEX);
            }
        }

        return newTrackable;
    }

    public Trackable addFood(String nameInput, String notesInput, int calorieInput) {
        Trackable newTrackable;

        //get the input fields
        EditText fatField = (EditText) findViewById(R.id.fats);
        EditText carbField = (EditText) findViewById(R.id.carbs);
        EditText proteinField = (EditText) findViewById(R.id.proteins);
        EditText servingSizeField = (EditText) findViewById(R.id.servingSize);
        Spinner mealTypeField = (Spinner) findViewById(R.id.type);
        CheckBox favoriteCheckbox = (CheckBox) findViewById(R.id.favoriteCheckbox);

        if (servingSizeField.getText().toString().equals("")) {
            Toast.makeText(this, "Must input a serving size!",Toast.LENGTH_LONG).show();
            return null;
        }

        //convert inputs to ints/strings
        int servingSize = Integer.parseInt(servingSizeField.getText().toString());
        int fatInput;
        int carbInput;
        int proteinInput;
        String mealtype = mealTypeField.getSelectedItem().toString();

        //fat, carb, input - set to 0 default if no input is made
        if (fatField.getText().toString().equals("")) {
            fatInput = 0;
        }
        else {
            fatInput = Integer.parseInt(fatField.getText().toString());
        }

        if (carbField.getText().toString().equals("")) {
            carbInput = 0;
        }
        else {
            carbInput = Integer.parseInt(carbField.getText().toString());
        }

        if (proteinField.getText().toString().equals("")) {
            proteinInput = 0;
        }
        else {
            proteinInput = Integer.parseInt(proteinField.getText().toString());
        }

        //make the new food with the fields taken
        newTrackable = new Food(nameInput, calorieInput, fatInput, carbInput, proteinInput,
                servingSize, favoriteCheckbox.isChecked(), notesInput, mealtype);

        //today list
        if (isEditing == 1) {
            TrackableList.editTodayFood(foodInput,(Food)newTrackable);
            updateDB(TrackableList.getTodayFoods(), null,
                    DatabaseContract.DataEntry.COLUMN_NAME_D1);
        }

        //favorite list
        else if (isEditing == 2) {
            TrackableList.editFavoriteFood(foodInput, (Food)newTrackable);
            updateDB(TrackableList.getFavoriteFoods(), null,
                    DatabaseContract.DataEntry.COLUMN_NAME_FAVFOOD);
        }

        //add to both lists
        else {
            //add and update today food
            TrackableList.addTodayFood((Food) newTrackable);
            updateDB(TrackableList.getTodayFoods(), null,
                    DatabaseContract.DataEntry.COLUMN_NAME_D1);

            //add and update recent foods
            TrackableList.addRecentFood((Food)newTrackable);
            updateDB(TrackableList.getRecentFoods(), null,
                    DatabaseContract.DataEntry.COLUMN_NAME_RECFOOD);

            //add and upate favorite foods if checked
            if (newTrackable.isFavorite()) {
                TrackableList.addFavoriteFood((Food) newTrackable);
                updateDB(TrackableList.getFavoriteFoods(), null,
                        DatabaseContract.DataEntry.COLUMN_NAME_FAVFOOD);
            }
        }
        return newTrackable;
    }
    /**
     * Method that occurs when the cancel button is clicked from the add
     */
    public void onCancelButtonClick(View view) {
        Intent i = new Intent();
        setResult(RESULT_OK, i);

        i.putExtra("addCanceled", true);
        finish();
    }

    /**
     * Method that is triggered when picture button is clicked
     */
    public void onPictureButtonClick(View view) {
        // Go to camera roll to select photo.
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    /**
     * Method for returning from any activities
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get the image if one was selected
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView picture = (ImageView) findViewById(R.id.picture);
            picture.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    /*
    Method to update the database with the given list
     */
    public void updateDB(ArrayList<Food> food, ArrayList<Exercise> ex, String type) {
        //call db
        SQLiteDatabase db = MainActivity.mDbHelper.getReadableDatabase();

        //intiailize blob
        byte[] blob;
        //serialize the list
        if (!inputChoice) blob = Serializer.serializeObject(food);
        else blob = Serializer.serializeObject(ex);

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
}