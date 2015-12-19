package com.example.zghadyali.carshareapp.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zghadyali.carshareapp.FriendActivity;
import com.example.zghadyali.carshareapp.SignUp.MainActivity;
import com.example.zghadyali.carshareapp.R;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;

/**
 * Activity for Owner use
 */
public class OwnerActivity extends FriendActivity {

    private OwnerHome ownerHome = new OwnerHome();
    private OwnerSettings ownerSettings = new OwnerSettings();
    private UpdateApprovedList updateApprovedList = new UpdateApprovedList();
    private OwnerRequests ownerRequests = new OwnerRequests();
    private OwnerTrips ownerTrips;

    public AccessToken accessToken;
//    public String profileID;
    public String name;
    private JSONObject carInfo;
    private JSONArray requestsArray;
    private JSONArray pendingRequestsArray;

    private JSONArray ownerRequestsJSONArray;
    public ArrayList<Request> dispOwnerRequests;
    final private String PENDING_CODE = "PENDING";

    //Calendar related calls:
    private Calendar calendar;
    private int day, year,month;
    private String date;
    private ArrayList <Request> futurecurrentrequestsArray;


    //Making Volley Request
    public void volley_data() {
        VolleyRequests handler = new VolleyRequests(getApplicationContext());

        handler.getcarinfo(new callback_cars() {
            @Override
            public void callback(JSONObject cars) {
                carInfo = cars;
                OwnerHome home = new OwnerHome();
                transitionToFragment(home);
                Log.d("JSON CAR to string: ", cars.toString());
            }
        }, profileID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        setupFriends();

        accessToken = AccessToken.getCurrentAccessToken();
        Log.d("LOGGEDIN ACCESS TOKEN: ", accessToken.getToken());

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date = month + "/" + day + "/" + year;

        if (getIntent().hasExtra("profileID") && getIntent().hasExtra("name")){
            profileID = getIntent().getExtras().getString("profileID");
            name = getIntent().getExtras().getString("name");
            Log.d("PROFILE ID: ", profileID);
            Log.d("name", name);
            getfuturecurrentRequests();
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
                                profileID = user_id.getString("id");
                                getfuturecurrentRequests();
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
                getfuturecurrentRequests();
                return true;
            case R.id.action_settings:
                transitionToFragment(ownerSettings);
                return true;
            case R.id.action_approvedlist:
                transitionToFragment(updateApprovedList);
                return true;
            case R.id.action_logout:
                LoginManager.getInstance().logOut();
                Log.d("Access token", accessToken.toString());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_pending_requests:
                getRequests();
                return true;
            case R.id.owner_history:
                getAllRequests();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * transitions to the input fragment
     * @param fragment Fragment to transition to
     */
    public void transitionToFragment(Fragment fragment){
        //This function takes as input a fragment, initializes the fragment manager and replaces
        //the container with the provided fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        ft.replace(R.id.container, fragment);

        // Start the animated transition.
        ft.commit();
    }

    /**
     * Returns the JSONArray of requests
     * @return JSONArray of requests
     */
    public JSONArray getRequestsArray() {
        return requestsArray;
    }

    /**
     * makes a Volley Request to get the owner's car info
     * @return JSONObject of the owner's car
     */
    public JSONObject getCarInfo() {
        VolleyRequests handler = new VolleyRequests(getApplicationContext());

        handler.getcarinfo(new callback_cars() {
            @Override
            public void callback(JSONObject cars) {
                carInfo = cars;
                Log.d("OwnerActivity", "carInfo updated: " + carInfo.toString());
            }
        }, profileID);
        return carInfo;
    }

    /**
     * gets all of the owner's pending requests
     */
    public void getRequests() {
        VolleyRequests handler = new VolleyRequests(getApplicationContext());
        handler.getownerRequests(new callback_requests() {
            @Override
            public void callback(JSONArray requests) {
                requestsArray = requests;
                pendingRequestsArray = new JSONArray();
                Log.d("STUFF", requestsArray.toString());
                try {
                    for (int i = 0; i < requestsArray.length(); i++) {
                        JSONObject request = requestsArray.getJSONObject(i);
                        if (request.getString("approved").equals(PENDING_CODE)) {
                            pendingRequestsArray.put(request);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error!", "no requests :(" + e.getMessage());
                }
                ownerRequests = new OwnerRequests();
                transitionToFragment(ownerRequests);
            }
        }, profileID);
    }

    /**
     * makes a Volley request to get all of the owner's approved future requests
     */
    public void getfuturecurrentRequests() {
        Log.d("in the future current", "here");
        VolleyRequests handler = new VolleyRequests(getApplicationContext());
        futurecurrentrequestsArray = new ArrayList<com.example.zghadyali.carshareapp.Owner.Request>();
        handler.getfuturecurrentrequests(new callback_requests() {
            @Override
            public void callback(JSONArray requests) {
                try {
                    for (int i = 0; i < requests.length(); i++) {
                        JSONObject request = requests.getJSONObject(i);
                        futurecurrentrequestsArray.add(new com.example.zghadyali.carshareapp.Owner.Request(request));
                        Log.d("haifhasighai", request.toString());
                        Log.d("date: ", date);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error!", "no requests :(" + e.getMessage());
                }
                volley_data();
//                ownerHome = new OwnerHome();
//                transitionToFragment(ownerHome);
            }
        }, profileID, date);
    }

    /**
     * returns the JSONArray of the owner's pending requests
     * @return JSONArray of the owner's pending requests
     */
    public JSONArray getPendingRequestsArray() {
        return pendingRequestsArray;
    }

    /**
     * gets all of an owner's requests
     */
    public void getAllRequests() {
        final VolleyRequests handler = new VolleyRequests(getApplicationContext());
        ownerRequestsJSONArray = new JSONArray();
        dispOwnerRequests = new ArrayList<Request>();
        handler.getownerRequests(new callback_requests() {
            @Override
            public void callback(JSONArray requests) {
                ownerRequestsJSONArray = requests;
                try {
                    for (int i = 0; i < ownerRequestsJSONArray.length(); i++) {
                        dispOwnerRequests.add(new Request((JSONObject) ownerRequestsJSONArray.get(i)));
                        Log.d("REQUEST OBJECT: ", dispOwnerRequests.get(i).toString());
                    }
                    Collections.sort(dispOwnerRequests);
                    Collections.reverse(dispOwnerRequests);
                } catch (JSONException e) {
                    Log.e("Error: ", e.getMessage());
                }
                ownerTrips = new OwnerTrips();
                transitionToFragment(ownerTrips);
            }
        }, profileID);
    }

    /**
     * returns an ArrayList of Requests representing of the owner's future requests
     * @return an ArrayList of Requests representing of the owner's future requests
     */
    public ArrayList<Request> getFutureRequestsArray() { return futurecurrentrequestsArray; }

    /**
     * transitions to the owner's home fragment
     */
    public void transitionToHome() {
        transitionToFragment(ownerHome);
    }
}
