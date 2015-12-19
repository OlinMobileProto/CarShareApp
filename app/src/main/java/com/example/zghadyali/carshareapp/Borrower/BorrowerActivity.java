package com.example.zghadyali.carshareapp.Borrower;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.SignUp.MainActivity;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;
import com.example.zghadyali.carshareapp.Volley.callback_cars;
import com.example.zghadyali.carshareapp.Volley.callback_requests;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

// this is the borrower activity that the user is directed to if they exist on the server and are a
// borrower. We get all of the cars this borrower can borrow and we give them the ability to request
// the cars by providing the date, time they will take the car, and time they will return the car.
// We check if the date is before the current date or the time is before the current time and that the
// time you return is after the time you were going to leave.

/**
 * The convention for adding comments describing a method or class is to use this
 * style of comments, there are a lot of auto documentation generators that will
 * automatically read comments if they are in this form. No worries though, just
 * a tip for the future
 */
public class BorrowerActivity extends AppCompatActivity {

    /**
     * Citing this http://programmers.stackexchange.com/questions/143736/why-do-we-need-private-variables
     * You should be using getters and setters if you need to access these outside the class
     * and making these all private/protected
     */
    public AccessToken accessToken;
    public String profileID;
    public String name, date, starttime, endtime;
    private int month, year, day, hour, minute;
    private Calendar calendar;
    //You can remove this unused variable
    public JSONObject borrower_cars;

    public JSONArray car_ids;
    public JSONArray carsJSON;
    public ArrayList<String> carsList;
    public BorrowerHome borrowerHome;
    public int len;
    public BorrowerTrips borrowerTrips;
    public JSONArray borrowerRequests;
    public ArrayList<Request> dispBorrowerRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower);


        //SETTING DEFAULT DATE AND TIME
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        /**
         * Dates are some of the worst designed things in Java, but instead of this
         * consider doing something like this
         * http://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
         * the SimpleDataFormat should really be used to generate the string values of dates
         */
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
            //Nitpick, but you might consider this to be an info log that is saying
            //There is no stored info about the user
            Log.d("BORROWER CLASS: ", "I don't have any of that information right now");
            //YOu can remove the left side of this equals sign and the code will workt he same
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

    //THis method is wicked clean, nice work
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.trips:
                getRequests();
                return true;
            case R.id.action_logout:
                LoginManager.getInstance().logOut();
                Log.d("Access token", accessToken.toString());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                transitionToFragment(borrowerHome);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //It looks like this method is just used in this class so it should be private
    public void transitionToFragment(Fragment fragment){
        //This function takes as input a fragment, initializes the fragment manager and replaces
        //the container with the provided fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //Sweet
        ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        ft.replace(R.id.container, fragment);

        // Start the animated transition.
        ft.commit();
    }

    public void getRequests(){
        final VolleyRequests handler = new VolleyRequests(getApplicationContext());
        borrowerRequests = new JSONArray();
        dispBorrowerRequests = new ArrayList<Request>();
        handler.getborrowerRequests(new callback_requests() {
            @Override
            public void callback(JSONArray requests) {
                borrowerRequests = requests;
                try {
                    for (int i = 0; i < borrowerRequests.length(); i++) {
                        dispBorrowerRequests.add(new Request((JSONObject) borrowerRequests.get(i)));
                        Log.d("REQUEST OBJECT: ", dispBorrowerRequests.get(i).toString());
                    }
                    Collections.sort(dispBorrowerRequests);
                    Collections.reverse(dispBorrowerRequests);
                } catch (JSONException e) {
                    Log.e("Error: ", e.getMessage());
                }
                borrowerTrips = new BorrowerTrips();
                transitionToFragment(borrowerTrips);
            }
        }, profileID);
    }

    public void updateCarList(String date, String starttime, String endtime) {

        final String new_date = date;
        final String new_starttime = starttime;
        final String new_endtime = endtime;

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
                                Log.d("asuisuiuf", new_date);
                                handler.editrequestalldone(profileID, new_date, new_starttime);
                                borrowerHome = new BorrowerHome();
                                borrowerTrips = new BorrowerTrips();
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
