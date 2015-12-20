package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.zghadyali.carshareapp.Volley.Callback;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parent activity for activities that needs access to facebook friend data.
 */
abstract public class FriendActivity extends AppCompatActivity {
    private JSONArray friendsJSON;
    protected ArrayList<String> friends;
    protected ArrayList<String> friendsIDs;
    protected ArrayList<String> borrowerFriendsIDs;
    protected String profileID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FRIENDACTIVITY", "friendactivity created");
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    /**
     * Get the facebook friend data from Facebook.
     */
    public void setupFriends() {
        //The left side of this equal sign is unnecessary.
        GraphRequestAsyncTask request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("FRIENDACTIVITY","graphrequest completed");
                        try {
                            friends = new ArrayList<String>();
                            friendsIDs = new ArrayList<String>();
                            borrowerFriendsIDs = new ArrayList<String>();
                            JSONObject res = response.getJSONObject();
                            Log.d("res",res.toString());
                            friendsJSON = res.getJSONArray("data");
                            Log.d("friendsJSON: ", friendsJSON.toString());
                            if (friendsJSON != null) {
                                int len = friendsJSON.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject temp = friendsJSON.getJSONObject(i);
                                    friends.add(temp.get("name").toString());
                                    String thisID = temp.get("id").toString();
                                    friendsIDs.add(thisID);
                                    addIfBorrower(thisID);
                                }
                                Log.d("FriendActivity","friends and friendsIDs set up");
//                                Log.d("FriendActivity","final borrowerFriendsIDs: " + borrowerFriendsIDs.toString());
                            }
                        } catch (Exception e) {
                            Log.e("FRIENDACTIVITY GraphRequest Error", e.getMessage());
                        }
                    }
                }
        ).executeAsync();
    }

    /**
     * Gets the name of a friend from their id.
     * @param id The friend's id
     * @return String of the friend's name
     */
    public String getFriendNameFromID(String id) {
        for (int i = 0; i < friendsJSON.length(); i++) {
            try {
                JSONObject friend = friendsJSON.getJSONObject(i);
                //These should be defined as constants somewhere
                String thisID = friend.getString("id");
                if (thisID.equals(id)) {
                    return friend.getString("name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "FRIEND ID NOT FOUND";
    }

    /**** FRIEND GETTERS AND SETTERS ****/
    public ArrayList<String> getFriendsIDs() {
        return friendsIDs;
    }
    public void setFriendsIDs(ArrayList<String> newList) {
        friendsIDs = newList;
    }
    public ArrayList<String> getBorrowerFriendsIDs() {
        return borrowerFriendsIDs;
    }
    public ArrayList<String> getFriends() {
        return friends;
    }
    public void setFriends(ArrayList<String> newList) {
        friends = newList;
    }
    public String getProfileID() {
        return profileID;
    }
    public JSONArray getFriendsJSON() {
        return friendsJSON;
    }

    /**
     * Adds this id to the borrowerfriendsIDs list if they're a borrower on the server.
     * @param id The id to check
     */
    private void addIfBorrower(final String id) {
        final VolleyRequests handler = new VolleyRequests(this.getApplicationContext());
        GraphRequestAsyncTask userid_request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            handler.getuser(new Callback() {
                                @Override
                                public void callback(Integer user_status) {
                                    if (user_status == 1){
                                        Log.d("isBorrower: ", id + " is an owner");
                                    }
                                    if (user_status == 2){
                                        Log.d("isBorrower", id + " is a borrower");
                                        borrowerFriendsIDs.add(id);
                                        Log.d("isBorrower","new borrowerFriendsIDs: " + borrowerFriendsIDs.toString());
                                    }
                                }
                            }, id);
                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }
                    }
                }
        ).executeAsync();
    }
}
