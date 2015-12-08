package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jordan on 11/18/15.
 */
public class OwnerActivity extends FriendActivity{

    public OwnerHome ownerHome = new OwnerHome();
    public OwnerSettings ownerSettings = new OwnerSettings();
    public UpdateApprovedList updateApprovedList = new UpdateApprovedList();

    public AccessToken accessToken;
    public String profile_id;
    public String name;
    public JSONObject car_info;

    public JSONArray friendsJSON;
    private ArrayList<String> friends;
    private ArrayList<String> friendsIDs;

    //Making Volley Request
    public void volley_data() {
        VolleyRequests handler = new VolleyRequests(getApplicationContext());

        handler.getcarinfo(new callback_cars() {
            @Override
            public void callback(JSONObject cars) {
                car_info = cars;
                OwnerHome home = new OwnerHome();
                transitionToFragment(home);
                Log.d("JSON CAR to string: ", cars.toString());
            }
        }, profile_id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        setupFriends();

        accessToken = AccessToken.getCurrentAccessToken();
        Log.d("LOGGEDIN ACCESS TOKEN: ", accessToken.getToken());
        if (getIntent().hasExtra("profile_id") && getIntent().hasExtra("name")){
            profile_id = getIntent().getExtras().getString("profile_id");
            name = getIntent().getExtras().getString("name");
            Log.d("PROFILE ID: ", profile_id);
            Log.d("name", name);
            volley_data();
        }
        else{
            Log.d("OWNER CLASS: ", "I don't have any of that information right now");
            GraphRequestAsyncTask userid_request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            try {
                                final JSONObject user_id = response.getJSONObject();
                                Log.d("USER ID JSON", user_id.toString());
                                name = user_id.getString("name");
                                profile_id = user_id.getString("id");
                                volley_data();
                            } catch (Exception e){
                                Log.e("Error: ", e.getMessage());
                            }
                        }
                    }).executeAsync();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_home:
                transitionToFragment(ownerHome);
                return true;
            case R.id.action_settings:
                transitionToFragment(ownerSettings);
                return true;
            case R.id.action_approvedlist:
                transitionToFragment(updateApprovedList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void transitionToFragment(Fragment fragment){
        //This function takes as input a fragment, initializes the fragment manager and replaces
        //the container with the provided fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
