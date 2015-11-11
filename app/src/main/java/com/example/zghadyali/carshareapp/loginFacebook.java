package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class loginFacebook extends Fragment {

    public LoginButton loginButton;
    public CallbackManager callbackManager;
    public AccessToken accessToken;
    public ListView friendsList;
    public ArrayAdapter<String> friendsAdapter;   //use the array adapter to update the view of
    public EditText searchFriends;
    public ArrayList<String> friends;
    public JSONArray approved_list;
    public Button next;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.login_facebook, container, false);

        loginButton = (LoginButton) rootview.findViewById(R.id.login_button);
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
//                Log.d("Access token: ", AccessToken.getCurrentAccessToken().toString());
                SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                if (preferences.contains("HAS_BEEN_RUN_FLAG")){
                    //
                }
                else {
                    //do the facebook authentication
                    preferences.edit().putBoolean("HAS_BEEN_RUN_FLAG", true).apply();
                    friendsList.setVisibility(View.VISIBLE);
                    searchFriends.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    Log.d("Access token: ", loginResult.getAccessToken().getToken());
                    accessToken = loginResult.getAccessToken();
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
                                    } catch (Exception e) {
                                        Log.e("ERROR: ", e.getMessage());
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
                                        approved_list = new JSONArray();
                                        JSONObject res = response.getJSONObject();
//                                    Log.d("Response", res.toString());
                                        final JSONArray friendsJSON = res.getJSONArray("data");
                                        Log.d("friendsJSON: ", friendsJSON.toString());
                                        if (friendsJSON != null) {
                                            int len = friendsJSON.length();
                                            for (int i = 0; i < len; i++) {
                                                JSONObject test = friendsJSON.getJSONObject(i);
                                                friends.add(test.get("name").toString());
                                            }
                                            friendsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.text_view, friends);
                                            friendsList.setAdapter(friendsAdapter);
                                            friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    try {
                                                        approved_list.put(friendsJSON.get(position));
                                                        Log.d("approved list: ", approved_list.toString());
                                                    } catch (Exception e) {
                                                        Log.e("Error: ", e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        Log.e("Error: ", e.getMessage());
                                    }
                                }
                            }
                    ).executeAsync();
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }


        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accessToken == null) {
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));
                } else {
                    accessToken = null;
                    LoginManager.getInstance().logOut();
                    friends = new ArrayList<String>();
                    searchFriends.setVisibility(View.GONE);
                    friendsList.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                }
            }
        });

        searchFriends = (EditText) rootview.findViewById(R.id.search_friends);
        searchFriends.setVisibility(View.GONE);
        friendsList = (ListView) rootview.findViewById(R.id.friends_list);
        friendsList.setVisibility(View.GONE);
        next = (Button) rootview.findViewById(R.id.next);
        next.setVisibility(View.GONE);

    return rootview;
    }
}
