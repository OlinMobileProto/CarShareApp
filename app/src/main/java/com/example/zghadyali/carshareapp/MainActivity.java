package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public loginFacebook loginfb;
    public CallbackManager callbackManager;
    public AccessToken accessToken;
    public JSONObject userid;
    public JSONArray friendsJSON;
    public ArrayList<String> friends;
    public String profile_name;
    public String profile_id;
    public String carLocation;
    public String keysLocation;
    public SharedPreferences preferences;

//    @Override
//    protected void onStop(){
//        super.onStop();
////        LoginManager.getInstance().logOut();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null){
            Log.d("TOKEN STATUS: ", "you have remained logged in");
            //I know you exist on the server so let's fire up a volley request and figure out what you are
            final VolleyRequests handler = new VolleyRequests(this.getApplicationContext());
            GraphRequestAsyncTask userid_request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            try {
                                JSONObject user_id = response.getJSONObject();
                                Log.d("USER ID JSON", user_id.toString());
                                profile_name = user_id.getString("name");
                                profile_id = user_id.getString("id");
                                handler.getuser(new Callback() {
                                    @Override
                                    public void callback(Integer user_status) {
                                        if (user_status == 1){
                                            Log.d("STATUS: ", "You are an owner on the server already");
                                        Intent intent = new Intent(getApplicationContext(), Owner.class);
                                        startActivity(intent);
                                        }
                                    }
                                }, profile_id);
                                userid = response.getJSONObject();
                            } catch (Exception e) {
                                Log.e("Error: ", e.getMessage());
                            }
                        }
                    }
            ).executeAsync();
        }
        else{
            Log.d("TOKEN STATUS: ", "you've either never logged in before or you're logged out now");
            loginfb = new loginFacebook();
            transitionToFragment(loginfb);
        }

//        if (preferences.contains("FB_ACCESS_TOKEN") && preferences.getBoolean("FB_LOG_IN", false)){
//            Log.d("LOGIN STATUS: ", "logged in before and you are still logged in");
//
//            Intent intent = new Intent(this, Owner.class);
//            startActivity(intent);
//        }
//        else if (preferences.contains("FB_ACCESS_TOKEN") && !preferences.getBoolean("FB_LOG_IN", false)){
//            Log.d("LOGIN STATUS: ", "logged in before but you are not currently logged in");
//            loginfb = new loginFacebook();
//            transitionToFragment(loginfb);
//        }
//        else{
//            Log.d("LOGIN STATUS: ", "not logged in before");
//            loginfb = new loginFacebook();
//            transitionToFragment(loginfb);
//        }
    }

    public void loginSetup(LoginButton button, final Boolean first_time) {
            button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    if (first_time) {
                        Log.d("from login result: ", loginResult.getAccessToken().getToken());
                        accessToken = loginResult.getAccessToken();
                        preferences.edit().putString("FB_ACCESS_TOKEN", accessToken.getToken()).apply();
                        preferences.edit().putBoolean("FB_LOG_IN", true).apply();
                        Log.d("Profile: ", Profile.getCurrentProfile().toString());

                /* make the API call */
                        GraphRequestAsyncTask userid_request = new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        try {
                                            JSONObject user_id = response.getJSONObject();
                                            Log.d("USER ID JSON", user_id.toString());
                                            profile_name = user_id.getString("name");
                                            profile_id = user_id.getString("id");
                                            userid = response.getJSONObject();
                                        } catch (Exception e) {
                                            Log.e("Error: ", e.getMessage());
                                        }
                                    }
                                }
                        ).executeAsync();

                        GraphRequestAsyncTask request = new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me/friends",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        try {
                                            friends = new ArrayList<String>();
                                            JSONObject res = response.getJSONObject();
                                            friendsJSON = res.getJSONArray("data");
                                            Log.d("friendsJSON: ", friendsJSON.toString());
                                            if (friendsJSON != null) {
                                                int len = friendsJSON.length();
                                                for (int i = 0; i < len; i++) {
                                                    JSONObject test = friendsJSON.getJSONObject(i);
                                                    friends.add(test.get("name").toString());
                                                }
                                            }
                                        } catch (Exception e) {
                                            Log.e("Error: ", e.getMessage());
                                        }
                                    }
                                }
                        ).executeAsync();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Owner.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException e) {
                }

            });
        }

    public void transitionToFragment(Fragment fragment){
        //This function takes as input a fragment, initializes the fragment manager and replaces
        //the container with the provided fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
