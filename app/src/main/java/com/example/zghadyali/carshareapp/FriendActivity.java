package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
                            JSONObject res = response.getJSONObject();
                            Log.d("res",res.toString());
                            friendsJSON = res.getJSONArray("data");
                            Log.d("friendsJSON: ", friendsJSON.toString());
                            if (friendsJSON != null) {
                                int len = friendsJSON.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject temp = friendsJSON.getJSONObject(i);
                                    friends.add(temp.get("name").toString());
                                    friendsIDs.add(temp.get("id").toString());
                                }
                                Log.d("FriendActivity","friends and friendsIDs set up");

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
        Log.d("HELLO", "hello");
        return friendsIDs;
    }

    public void setFriendsIDs(ArrayList<String> newList) {
        friendsIDs = newList;
    }

    public void addToFriendsIDs(String s) {
        friendsIDs.add(s);
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
}
