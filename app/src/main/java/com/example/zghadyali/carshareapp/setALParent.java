package com.example.zghadyali.carshareapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Parent class of all fragments to modify the approved list.
 */
public abstract class setALParent extends Fragment {

    //TODO make stuff private
    //TODO make it protected so that the subclasses can inherit them?
    public ListView friendsListView;
    public EditText searchFriends;
    public ArrayAdapter<String> friendsAdapter;
    public ArrayList<Integer> approved_list;
    public JSONArray approved_listJSON;
    public ArrayList<Integer> approvedList;
    protected ArrayList<String> approvedListIDs;
    private JSONArray approvedJSON;
    protected LoginButton loginButton;
    public loginFacebook loginfb;
    protected Button doneButton;
    private JSONArray approvedJSONarray;
    protected FriendActivity thisActivity;
    protected SetCarInfo setCarInfo;
    private ArrayList<String> friendsIDs;
    private ArrayList<String> friendsNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.set_approved_list, container, false);

        setupThisActivity();
        friendsListView = (ListView) rootview.findViewById(R.id.friends_list);
        searchFriends = (EditText) rootview.findViewById(R.id.search_friends_list);
        doneButton = (Button) rootview.findViewById(R.id.done_button);

        approvedList = new ArrayList<Integer>();
        approvedListIDs = new ArrayList<String>();
        approvedJSONarray = new JSONArray();

        friendsIDs = thisActivity.getFriendsIDs();
        friendsNames = thisActivity.getFriends();

        final MyCustomAdapter friendsAdapter = new MyCustomAdapter(friendsIDs, setALParent.this, getActivity());
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

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                // Build the approvedJSONarray
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
                        approvedJSONarray.put((thisActivity.friendsJSON).getJSONObject(friendPos));
                    } catch (JSONException e) {
                        Log.e("MYAPP", "unexpected JSON exception", e);
                        // Do something to recover ... or kill the app.
                    }
                }
                // Send the approved array to the server
                //TODO make this work in owneractivity
                handler.addtoapproved(((MainActivity) getActivity()).profile_id, approvedJSONarray);

                transistionToNextFragment();
            }
        });

        return rootview;
    }

    abstract protected void setupThisActivity();

    abstract protected void transistionToNextFragment();

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

    public FriendActivity getThisActivity() {
        return thisActivity;
    }
}
