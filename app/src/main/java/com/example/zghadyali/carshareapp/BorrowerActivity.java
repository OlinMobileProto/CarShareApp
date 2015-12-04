package com.example.zghadyali.carshareapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BorrowerActivity extends AppCompatActivity {

    public AccessToken accessToken;
    public String profile_id;
    public String name;
    public JSONObject borrower_cars;
    public JSONArray car_ids;
    public JSONArray carsJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower);

        accessToken = AccessToken.getCurrentAccessToken();
        Log.d("LOGGEDIN ACCESS TOKEN: ", accessToken.getToken());
        if (getIntent().hasExtra("profile_id") && getIntent().hasExtra("name")){
            profile_id = getIntent().getExtras().getString("profile_id");
            name = getIntent().getExtras().getString("name");
            Log.d("PROFILE ID: ", profile_id);
            Log.d("name", name);
            final VolleyRequests handler = new VolleyRequests(getApplicationContext());
            handler.getborrowerinfo(new callback_cars() {
                @Override
                public void callback(JSONObject cars) {
                    borrower_cars = new JSONObject();
                    borrower_cars = cars;
                    car_ids = new JSONArray();
                    try{
                        car_ids = borrower_cars.getJSONArray("can_borrow");

                        int len = car_ids.length();
                        carsJSON = new JSONArray();
                        for (int i = 0; i < len; i++) {
                            String test = car_ids.getString(i);
                            handler.getcarinfo(new callback_cars() {
                                @Override
                                public void callback(JSONObject cars) {
                                    carsJSON.put(cars);
                                    Log.d("BORROWER'S CARS", carsJSON.toString());
                                }
                            }, test);

                        }


                    } catch (Exception e){
                        Log.e("Error: ", e.getMessage());
                    }
                }
            }, profile_id);
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
                                profile_id = user_id.getString("id");
                                VolleyRequests handler = new VolleyRequests(getApplicationContext());
                                handler.getborrowerinfo(new callback_cars() {
                                    @Override
                                    public void callback(JSONObject cars) {
                                        borrower_cars = new JSONObject();
                                        borrower_cars = cars;
                                        car_ids = new JSONArray();
                                        try{
                                            car_ids = borrower_cars.getJSONArray("can_borrow");
                                        } catch (Exception e){
                                            Log.e("Error: ", e.getMessage());
                                        }
                                    }
                                }, profile_id);
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
}
