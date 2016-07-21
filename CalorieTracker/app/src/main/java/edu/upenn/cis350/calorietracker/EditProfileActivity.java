package edu.upenn.cis350.calorietracker;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
/**
 * Created by Richard on 2/29/2016.
 * Activity class for profile page
 */
public class EditProfileActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    private Spinner gender_spinner, activity_level_spinner;

    /**
     * Default oncreate method - intializes view and the spinners
     */
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Create spinners to select gender and activity level
        gender_spinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter);

        activity_level_spinner = (Spinner) findViewById(R.id.activity_level);
        ArrayAdapter<CharSequence> activity_level_adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_level, android.R.layout.simple_spinner_item);
        activity_level_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_level_spinner.setAdapter(activity_level_adapter);

    }

    /**
     * Override methods that aren't used for this activity
     */
    @Override
    public void onItemSelected(AdapterView parent, View view, int pos, long id){

    }
    @Override
    public void onNothingSelected(AdapterView parent){

    }

    /**
     * Method triggered when activity is canceled
     */
    public void onCancelClick(View view){
        Intent i = new Intent();
        i.putExtra("profileCanceled", true);
        setResult(RESULT_OK, i);
        finish();
    }


    /**
     * Method triggered when save is clicked
     */
    public void onSaveClick(View view){
        //put all of the fields into one intent and send it back to main activity
        Intent i = new Intent();
        i.putExtra("profileCanceled", false);

        //calories
        i.putExtra("calories", calculateCalories());
        EditText nameField = (EditText) findViewById(R.id.name);
        Editable nameText = nameField.getText();
        String name = nameText.toString();

        //name
        i.putExtra("Name", name);
        Object genderItem = gender_spinner.getSelectedItem();
        String gender = genderItem.toString();

        //gender
        i.putExtra("gender", gender);
        EditText weightField = (EditText) findViewById(R.id.weight);
        Editable weightText = weightField.getText();
        String weight = weightText.toString();

        //weight
        i.putExtra("weight", weight);
        EditText heightField = (EditText) findViewById(R.id.height);
        Editable heightText = heightField.getText();
        String height = heightText.toString();

        //height
        i.putExtra("height", height);
        EditText ageField = (EditText) findViewById(R.id.age);
        Editable ageText = ageField.getText();
        String age = ageText.toString();

        //age
        i.putExtra("age", age);
        Object activityItem = activity_level_spinner.getSelectedItem();
        String activity = activityItem.toString();

        i.putExtra("activity",activity);
        setResult(RESULT_OK, i);
        finish();
    }


    /**
     * Method to calculate calories based on inputs - uses the TDEE calculator
     */
    public int calculateCalories(){
        //get the spinner inputs
        String gender = gender_spinner.getSelectedItem().toString();
        String activity_level = activity_level_spinner.getSelectedItem().toString();

        //translate activity levels
        double activity = 0;
        if(activity_level.equals("Sedentary - Little to no exercise")){
            activity = 1.2;
        }else if(activity_level.equals("Lightly Active - Exercise 1-3 times/week")){
            activity = 1.37;
        }else if(activity_level.equals("Moderately Active - Exercise 3-5 times/week")){
            activity = 1.55;
        }else if(activity_level.equals("Very Active - Hard exercise 6-7 times/week")){
            activity = 1.73;
        }else{
            activity = 1.37;
        }

        //get weight, height, age inputs
        EditText weight_input = (EditText)findViewById(R.id.weight);
        int weight = Integer.parseInt(weight_input.getText().toString());
        EditText height_input = (EditText)findViewById(R.id.height);
        int height = Integer.parseInt(height_input.getText().toString());
        EditText age_input = (EditText)findViewById(R.id.age);
        int age = Integer.parseInt(age_input.getText().toString());

        //calculate the BMR
        int BMR = (int) (10*weight + 6.25*height - 5*age);
        if(gender_spinner.getSelectedItem().toString().equals("Male")){
            BMR += 5;
        }else if(gender_spinner.getSelectedItem().toString().equals("Female")){
            BMR -= 161;
        }else{
            BMR =+ 5;
        }

        //return result
        BMR = (int) (BMR*activity);
        return BMR;
    }
}
