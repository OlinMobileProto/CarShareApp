package com.example.zghadyali.carshareapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.zghadyali.carshareapp.SignUp.SetCarInfo;
import com.example.zghadyali.carshareapp.SignUp.loginFacebook;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parent class of all fragments to modify the approved list.
 */
public abstract class setALParent extends Fragment {

    private ListView friendsListView;
    private EditText searchFriends;
    private ApprovedListAdapter friendsAdapter;
    protected ArrayList<String> approvedListIDs;
    protected LoginButton loginButton;
    protected loginFacebook loginfb;
    protected Button doneButton;
    protected FriendActivity thisActivity;
    protected SetCarInfo setCarInfo;
    private ArrayList<String> friendsIDs;
    private ArrayList<String> friendsNames;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.set_approved_list, container, false);

        setupThisActivity();
        friendsListView = (ListView) rootview.findViewById(R.id.friends_list);
        searchFriends = (EditText) rootview.findViewById(R.id.search_friends_list);
        doneButton = (Button) rootview.findViewById(R.id.done_button);

        approvedListIDs = new ArrayList<String>();

        friendsIDs = thisActivity.getFriendsIDs();
        friendsNames = thisActivity.getFriends();

        friendsAdapter = new ApprovedListAdapter(friendsIDs, setALParent.this, getActivity());
        friendsListView.setAdapter(friendsAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh_approved_list);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Swipe Refresh", "onRefresh called form SwipeRefreshLayout");
                updateFriends();
            }
        });



        // Live Search Functionality
        searchFriends.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    // Search field is blank, show everyone
                    friendsAdapter.setNewList(friendsIDs);
                }
                else {
                    // Filter friends list to only show search matches
                    ArrayList<String> filteredFriends = new ArrayList<String>();
                    // Search through the names for a match
                    for (int i=0; i<friendsNames.size(); i++) {
                        if (friendsNames.get(i).contains(s)) {
                            // Add this friend's id to the filtered list
                            filteredFriends.add(friendsIDs.get(i));
                        }
                    }
                    friendsAdapter.setNewList(filteredFriends);
                }
            }

            @Override
            public void afterTextChanged(Editable edit) {
                if (edit.length() != 0) {
                    // Business logic for search here
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
//
//                // Build the approvedJSONarray
//                for (int k = 0; k < approvedListIDs.size(); k++ ){
////                    try {
//                        String thisID = approvedListIDs.get(k);
////                        // Get the position of this friend
////                        int friendPos = -1;
////                        for (int j=0;j<friendsIDs.size();j++) {
////                            if (friendsIDs.get(j).equals(thisID)) {
////                                friendPos = j;
////                            }
////                        }
////                        approvedJSONarray.put((thisActivity.friendsJSON).getJSONObject(friendPos));
//                        approvedJSONarray.put(thisID);
////                    } catch (JSONException e) {
////                        Log.e("MYAPP", "unexpected JSON exception", e);
////                        // Do something to recover ... or kill the app.
////                    }
//                }
//                // Send the approved array to the server
//                handler.addtoapproved(thisActivity.getProfileID(), approvedJSONarray);

                transitionToNextFragment();
            }
        });

        return rootview;
    }

    abstract protected void setupThisActivity();

    abstract protected void transitionToNextFragment();

    abstract protected void makeNewFragment();

    public void updateFriends() {
        GraphRequestAsyncTask request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("FRIENDACTIVITY", "graphrequest completed");
                        try {
                            friendsIDs = new ArrayList<String>();
                            JSONObject res = response.getJSONObject();
                            Log.d("res",res.toString());
                            Log.d("friendsJSON: ", res.getJSONArray("data").toString());
                            if (res.getJSONArray("data") != null) {
                                int len = res.getJSONArray("data").length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject temp = res.getJSONArray("data").getJSONObject(i);
                                    friendsIDs.add(temp.get("id").toString());
                                }
                                Log.d("setUser","friends and friendsIDs set up");
                            }
                            Log.d("hihgiahifsaifha", "aofoewir");
                            friendsAdapter.notifyDataSetChanged();
                            makeNewFragment();
                        } catch (Exception e) {
                            Log.e("setALParentGraphRequest", e.getMessage());
                        }
                    }
                }
        ).executeAsync();
    }

    public void addIDToApprovedList(String id) {
        approvedListIDs.add(id);
        Log.d("new approved list", approvedListIDs.toString());
        VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
        JSONArray thisFriendJSONArray = new JSONArray();
        thisFriendJSONArray.put(id);
        handler.addtoapproved(thisActivity.getProfileID(), thisFriendJSONArray);
    }

    public void removeIDFromApprovedList(String id) {
        approvedListIDs.remove(id);
        Log.d("new approved list", approvedListIDs.toString());
        VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
        handler.removefromapproved(thisActivity.getProfileID(), id);
    }

    public boolean IDIsApproved(String id) {
        return approvedListIDs.contains(id);
    }

    public FriendActivity getThisActivity() {
        return thisActivity;
    }
}
