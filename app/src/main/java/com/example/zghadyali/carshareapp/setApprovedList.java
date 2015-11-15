package com.example.zghadyali.carshareapp;

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

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class setApprovedList extends Fragment {

    public ListView friendsList;
    public EditText searchFriends;
    public ArrayAdapter<String> friendsAdapter;
    public JSONArray approved_list;
    public LoginButton loginButton;
    public loginFacebook loginfb;
    public Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.set_approved_list, container, false);

        friendsList = (ListView) rootview.findViewById(R.id.friends_list);
        searchFriends = (EditText) rootview.findViewById(R.id.search_friends_list);
        next = (Button) rootview.findViewById(R.id.next_to_details);

        approved_list = new JSONArray();
        friendsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.text_view, ((MainActivity)getActivity()).friends);
        friendsList.setAdapter(friendsAdapter);
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    approved_list.put(((MainActivity)getActivity()).friendsJSON.get(position));
                    Log.d("approved list: ", approved_list.toString());
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code that will transition to next fragment which should ask for a few more details
            }
        });

        loginButton = (LoginButton) rootview.findViewById(R.id.login_button);
        loginButton.setFragment(this);

        //should always be logging you out and log out should return you to first screen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).accessToken = null;
                LoginManager.getInstance().logOut();
                ((MainActivity)getActivity()).friends = new ArrayList<String>();
                loginfb = new loginFacebook();
                ((MainActivity)getActivity()).transitionToFragment(loginfb);
            }
        });

        return rootview;
    }


}
