package edu.upenn.cis350.calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * Created by Richard on 2/29/2016.
 */
public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final int EditProfile_ID = 1;

    private String name;
    private String gender;
    private int weight;
    private int height;
    private int age;
    private String activity;
    private int calories;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Update fields from saved preferences
        this.name = getPreferences(0).getString("Name", "n/a");
        this.gender = getPreferences(0).getString("gender", "n/a");
        this.weight = getPreferences(0).getInt("weight", 0);
        this.height = getPreferences(0).getInt("height", 0);
        this.age = getPreferences(0).getInt("age", 0);
        this.activity = getPreferences(0).getString("activity", "n/a");
        this.calories = getPreferences(0).getInt("calories", 2000);

        //get the views
        ((TextView) findViewById(R.id.NameText)).setText(name);
        ((TextView) findViewById(R.id.GenderText)).setText(gender);
        ((TextView) findViewById(R.id.WeightText)).setText(Integer.toString(weight));
        ((TextView) findViewById(R.id.HeightText)).setText(Integer.toString(height));
        ((TextView) findViewById(R.id.AgeText)).setText(Integer.toString(age));
        ((TextView) findViewById(R.id.ActivityText)).setText(activity);
    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int pos, long id){

    }
    @Override
    public void onNothingSelected(AdapterView parent){

    }


    /**
     * Method triggered on back click
     */
    public void onBackClick(View view){
        //put into intent the new calories and finish
        Intent i = new Intent();
        i.putExtra("profileCanceled", false);
        i.putExtra("NEW_CALORIES", calories);
        setResult(RESULT_OK, i);
        finish();
    }

    /**
     * Method triggered on edit click
     */
    //Launch edit profile activity
    public void onEditClick(View view){
        //start the editprofileactivity class
        Intent i = new Intent(this, EditProfileActivity.class);
        startActivityForResult(i, EditProfile_ID);
    }

    /**
     * Coming back from the edit profile activity class
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode){
            case EditProfile_ID:
                if(!intent.getBooleanExtra("profileCanceled", false)) {

                    //get all of the inputs from the edit activity
                    SharedPreferences.Editor editor = getPreferences(0).edit();
                    name = intent.getStringExtra("Name");
                    editor.putString("Name", name);

                    gender = intent.getStringExtra("gender");
                    editor.putString("gender", gender);

                    String tempweight = intent.getStringExtra("weight");
                    weight = Integer.parseInt(tempweight);
                    editor.putInt("weight", weight);

                    String tempheight = intent.getStringExtra("height");
                    height = Integer.parseInt(tempheight);
                    editor.putInt("height", height);

                    String tempage = intent.getStringExtra("age");
                    age = Integer.parseInt(tempage);
                    editor.putInt("age", age);

                    activity = intent.getStringExtra("activity");
                    editor.putString("activity", activity);

                    calories = intent.getIntExtra("calories", 2000);
                    editor.putInt("calories", calories);
                    editor.commit();

                    //set all of the views to the gotten inputs
                    ((TextView) findViewById(R.id.NameText)).setText(this.name);
                    ((TextView) findViewById(R.id.GenderText)).setText(gender);
                    ((TextView) findViewById(R.id.WeightText)).setText(Integer.toString(weight));
                    ((TextView) findViewById(R.id.HeightText)).setText(Integer.toString(height));
                    ((TextView) findViewById(R.id.AgeText)).setText(Integer.toString(age));
                    ((TextView) findViewById(R.id.ActivityText)).setText(activity);
                }
                break;

        }
    }

}
