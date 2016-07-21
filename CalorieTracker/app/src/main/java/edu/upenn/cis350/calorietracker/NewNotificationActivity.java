package edu.upenn.cis350.calorietracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Richard on 4/26/2016.
 */
public class NewNotificationActivity extends Activity{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
    }

    public void onCancelButtonClick(View view){
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }

    public void onSaveNotificationClick(View view){
        Intent i = new Intent();
        TextView name = (TextView) findViewById(R.id.newNotificationNameField);
        TextView time = (TextView) findViewById(R.id.newNotificationTimeField);
        i.putExtra("NOTIFICATION_NAME", name.getText().toString());
        i.putExtra("NOTIFICATION_TIME", time.getText().toString());
        setResult(RESULT_OK,i);
        finish();
    }
}
