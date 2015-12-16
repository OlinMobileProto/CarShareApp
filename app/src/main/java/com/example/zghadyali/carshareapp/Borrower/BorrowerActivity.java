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
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;
import com.example.zghadyali.carshareapp.Volley.callback_cars;
import com.example.zghadyali.carshareapp.Volley.callback_requests;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class BorrowerActivity extends AppCompatActivity {

    public AccessToken accessToken;
    public String profileID;
    public String name, date, starttime, endtime;
    private int month, year, day, hour, minute;
    private Calendar calendar;
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

        //SETTING DEFAULT DATE AND TIME
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        date = month + "/" + day + "/" + year;
        starttime = hour + ":" + minute;
        endtime = (hour+1) + ":" + minute;

        accessToken = AccessToken.getCurrentAccessToken();
        Log.d("LOGGEDIN ACCESS TOKEN: ", accessToken.getToken());
        if (getIntent().hasExtra("profileID") && getIntent().hasExtra("name")){
            //if the user just signed up as a borrower, extra bundle also contains friends, friendIDs, and friendsJSON so we can use those
            profileID = getIntent().getExtras().getString("profileID");
            name = getIntent().getExtras().getString("name");
            Log.d("PROFILE ID: ", profileID);
            Log.d("name", name);
            updateCarList(date, starttime, endtime);
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

                                updateCarList(date, starttime, endtime);
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

    public void updateCarList(String date, String starttime, String endtime) {

        //Get all of the available cars:
        final VolleyRequests handler = new VolleyRequests(getApplicationContext());
        handler.getavailablecars(new callback_requests() {
            @Override
            public void callback(JSONArray requests) {
                try {
                    if (requests.length() == 0) {
                        borrowerHome = new BorrowerHome();
                        transitionToFragment(borrowerHome);
                    }
                    car_ids = new JSONArray();
                    carsList = new ArrayList<String>();
                    len = requests.length();
                    for (int i = 0; i < len; i++) {
                        final String car_id = requests.getString(i);
                        carsJSON = new JSONArray();
                        handler.getcarinfo(new callback_cars() {
                            @Override
                            public void callback(JSONObject cars) {
                                carsJSON.put(cars);
                                try {
                                    String temp_name = cars.getString("owner");
                                    carsList.add(temp_name + "'s Car");
                                    car_ids.put(cars.getString("facebook_id"));
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
                    Log.e("Error:", e.getMessage());
                }
            }
        },profileID, date, starttime, endtime);

//        final VolleyRequests handler = new VolleyRequests(getApplicationContext());
//        handler.getborrowerinfo(new callback_cars() {
//            @Override
//            public void callback(JSONObject cars) {
//                borrower_cars = new JSONObject();
//                borrower_cars = cars;
//                car_ids = new JSONArray();
//                try {
//                    car_ids = borrower_cars.getJSONArray("can_borrow");
//                    len = car_ids.length();
//                    if (len == 0) {
//                        borrowerHome = new BorrowerHome();
//                        transitionToFragment(borrowerHome);
//                    }
//                    Log.d("You are approved for", String.valueOf(len));
//                    carsJSON = new JSONArray();
//                    carsList = new ArrayList<String>();
//                    for (int i = 0; i < len; i++) {
//                        String car_id = car_ids.getString(i);
//                        handler.getcarinfo(new callback_cars() {
//                            @Override
//                            public void callback(JSONObject cars) {
//                                carsJSON.put(cars);
//                                try {
//                                    String temp_name = cars.getString("owner");
//                                    carsList.add(temp_name + "'s Car");
//                                    //code here to use make and model if they are supplied, if they aren't have to catch if they are null
//                                    //or if they are empty strings
//                                } catch (Exception e) {
//                                    e.getMessage();
//                                }
//                                Log.d("BORROWER'S CARS", carsList.toString());
//                                borrowerHome = new BorrowerHome();
//                                transitionToFragment(borrowerHome);
//                            }
//                        }, car_id);
//                    }
//                } catch (Exception e) {
//                    Log.e("Error: ", e.getMessage());
//                }
//            }
//        }, profileID);
    }

}
