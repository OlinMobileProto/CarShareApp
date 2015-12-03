package com.example.zghadyali.carshareapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class setApprovedList extends Fragment {

    //TODO make stuff private
    public ListView friendsListView;
    public EditText searchFriends;
    public ArrayAdapter<String> friendsAdapter;
    public ArrayList<Integer> approved_list;
    public JSONArray approved_listJSON;
    public ArrayList<Integer> approvedList;
    private ArrayList<String> approvedListIDs;
    private JSONArray approvedJSON;
    public LoginButton loginButton;
    public loginFacebook loginfb;
    public Button next;
    private JSONArray approvedJSONarray;
    private MainActivity mainActivity;
    private SetCarInfo setCarInfo;
    private ArrayList<String> friendsIDs;
    private ArrayList<String> friendsNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.set_approved_list, container, false);

        mainActivity = (MainActivity) getActivity();
        friendsListView = (ListView) rootview.findViewById(R.id.friends_list);
        searchFriends = (EditText) rootview.findViewById(R.id.search_friends_list);
        next = (Button) rootview.findViewById(R.id.next_to_details);

        approvedList = new ArrayList<Integer>();
        approvedListIDs = new ArrayList<String>();
        approvedJSONarray = new JSONArray();

        friendsIDs = mainActivity.friendsIDs;
        friendsNames = mainActivity.friends;

        Log.d("stoio", friendsIDs.toString());

        final MyCustomAdapter friendsAdapter = new MyCustomAdapter(friendsIDs, setApprovedList.this, getActivity());
        friendsListView.setAdapter(friendsAdapter);

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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCarInfo = new SetCarInfo();

                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
                
                for (int k = 0; k < approvedListIDs.size(); k++ ){
                    try {
                        String thisID = approvedListIDs.get(k);
                        // Get the position of this friend
                        int friendPos = -1;
                        for (int j=0;j<friendsIDs.size();j++) {
                            if (friendsIDs.get(j).equals(thisID)) {
                                friendPos = j;
                            }
                        }
                        handler.addtocanborrow(((mainActivity.friendsJSON).getJSONObject(friendPos)).getString("id"),((MainActivity) getActivity()).profile_id);
                        approvedJSONarray.put((mainActivity.friendsJSON).getJSONObject(friendPos));
                    } catch (JSONException e) {
                        Log.e("MYAPP", "unexpected JSON exception", e);
                        // Do something to recover ... or kill the app.
                    }
                }
                handler.addtoapproved(((MainActivity) getActivity()).profile_id, approvedJSONarray);

                mainActivity.transitionToFragment(setCarInfo);
            }
        });

        loginButton = (LoginButton) rootview.findViewById(R.id.login_button);
        loginButton.setFragment(this);

        //should always be logging you out and log out should return you to first screen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.accessToken = null;
                LoginManager.getInstance().logOut();
                mainActivity.friends = new ArrayList<String>();
                mainActivity.setFriendsIDs(new ArrayList<String>());

                loginfb = new loginFacebook();
                mainActivity.transitionToFragment(loginfb);
            }
        });

        return rootview;
    }

    public void addPosToApprovedList(int pos) {
        approvedList.add(pos);
        Log.d("new approved list", approvedList.toString());
    }

    public void removePosFromApprovedList(int pos) {
        approvedList.remove((Object) pos);
        Log.d("new approved list", approvedList.toString());
    }

    public boolean PosIsApproved(int pos) {
        return approvedList.contains(pos);
    }

    //TODO new functions for ids
    public void addIDToApprovedList(String id) {
        approvedListIDs.add(id);
        Log.d("new approved list", approvedListIDs.toString());
    }

    public void removeIDFromApprovedList(String id) {
        approvedListIDs.remove(id);
        Log.d("new approved list", approvedListIDs.toString());
    }

    public boolean IDIsApproved(String id) {
        return approvedListIDs.contains(id);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }
}
