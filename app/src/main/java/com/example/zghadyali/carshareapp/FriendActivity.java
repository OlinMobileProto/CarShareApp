package com.example.zghadyali.carshareapp;

import android.os.Bundle;
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
 * Created by bill on 12/5/15.
 */
abstract public class FriendActivity extends AppCompatActivity{

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

    public void setupFriends() {
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


    public String getFriendNameFromID(String id) {
        for (int i = 0; i < friendsJSON.length(); i++) {
            try {
                JSONObject friend = friendsJSON.getJSONObject(i);
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

    public ArrayList<String> getFriendsIDs() {
        return friendsIDs;
    }

    public void setFriendsIDs(ArrayList<String> newList) {
        friendsIDs = newList;
    }

    public void addToFriendsIDs(String s) {
        friendsIDs.add(s);
    }

    public ArrayList<String> getBorrowerFriendsIDs() {
        return borrowerFriendsIDs;
    }

    public void setBorrowerFriendsIDs(ArrayList<String> newList) {
        borrowerFriendsIDs = newList;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> newList) {
        friends = newList;
    }

    public void addToFriends(String s) {
        friends.add(s);
    }

    public String getProfileID() {
        return profileID;
    }

    public JSONArray getFriendsJSON() {
        return friendsJSON;
    }

    private void addIfBorrower(final String id) {
        final VolleyRequests handler = new VolleyRequests(this.getApplicationContext());
//        final boolean[] borrowerStatus = new boolean[1];
        GraphRequestAsyncTask userid_request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
//                            JSONObject user_id = response.getJSONObject();
//                            Log.d("USER ID JSON", user_id.toString());
//                            profileName = user_id.getString("name");
//                            profileID = user_id.getString("id");
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
//                            userid = response.getJSONObject();
                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }
                    }
                }
        ).executeAsync();
//        return borrowerStatus[0];
    }
}
