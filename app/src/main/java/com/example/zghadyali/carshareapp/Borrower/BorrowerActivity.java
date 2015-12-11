package com.example.zghadyali.carshareapp.Borrower;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.VolleyRequests;
import com.example.zghadyali.carshareapp.callback_cars;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BorrowerActivity extends AppCompatActivity {

    public AccessToken accessToken;
    public String profileID;
    public String name;
    public JSONObject borrower_cars;
    public JSONArray car_ids;
    public JSONArray carsJSON;
    public ArrayList<String> carsList;
    public BorrowerHome borrowerHome;
    public int len;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower);

        accessToken = AccessToken.getCurrentAccessToken();
        Log.d("LOGGEDIN ACCESS TOKEN: ", accessToken.getToken());
        if (getIntent().hasExtra("profileID") && getIntent().hasExtra("name")){
            //if the user just signed up as a borrower, extra bundle also contains friends, friendIDs, and friendsJSON so we can use those
            profileID = getIntent().getExtras().getString("profileID");
            name = getIntent().getExtras().getString("name");
            Log.d("PROFILE ID: ", profileID);
            Log.d("name", name);
            updateCarList();
        }
        else{
            Log.d("BORROWER CLASS: ", "I don't have any of that information right now");
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
                                profileID = user_id.getString("id");
                                updateCarList();
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
        getMenuInflater().inflate(R.menu.menu_borrower, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void transitionToFragment(Fragment fragment){
        //This function takes as input a fragment, initializes the fragment manager and replaces
        //the container with the provided fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void updateCarList() {

        final VolleyRequests handler = new VolleyRequests(getApplicationContext());
        handler.getborrowerinfo(new callback_cars() {
            @Override
            public void callback(JSONObject cars) {
                borrower_cars = new JSONObject();
                borrower_cars = cars;
                car_ids = new JSONArray();
                try {
                    car_ids = borrower_cars.getJSONArray("can_borrow");
                    len = car_ids.length();
                    if (len == 0) {
                        borrowerHome = new BorrowerHome();
                        transitionToFragment(borrowerHome);
                    }
                    Log.d("You are approved for", String.valueOf(len));
                    carsJSON = new JSONArray();
                    carsList = new ArrayList<String>();
                    for (int i = 0; i < len; i++) {
                        String car_id = car_ids.getString(i);
                        handler.getcarinfo(new callback_cars() {
                            @Override
                            public void callback(JSONObject cars) {
                                carsJSON.put(cars);
                                try {
                                    String temp_name = cars.getString("owner");
                                    carsList.add(temp_name + "'s Car");
                                    //code here to use make and model if they are supplied, if they aren't have to catch if they are null
                                    //or if they are empty strings
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                Log.d("BORROWER'S CARS", carsList.toString());
                                borrowerHome = new BorrowerHome();
                                transitionToFragment(borrowerHome);
                            }
                        }, car_id);
                    }
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
        }, profileID);
    }

}
