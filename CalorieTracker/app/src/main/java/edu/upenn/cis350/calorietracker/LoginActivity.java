package edu.upenn.cis350.calorietracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Jay Jung on 3/18/2016.
 * Activity page for facebook login
 */
public class LoginActivity extends AppCompatActivity {

    //for login
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accesstoken;
    private ProfileTracker mProfileTracker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fb initialization
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        //set up the login button
        LoginButton loginbutton = (LoginButton) findViewById(R.id.login_button);
        loginbutton.setReadPermissions("user_birthday", "user_friends", "public_profile");

        //callback method for fb api - start the main activity after logging in successfully
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            //success = new intent and new activity
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            startApp(profile2);
                            mProfileTracker.stopTracking();
                        }
                    };
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    startApp(profile);
                }
            }

            //toast to indicate cancellation for cancel
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void startApp(Profile profile) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);

        String username = profile.getFirstName();
        String id = profile.getId();

        i.putExtra("name", username);
        i.putExtra("id", id);

        startActivityForResult(i, 1);
    }
}
