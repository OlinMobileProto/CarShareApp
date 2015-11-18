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
    public ArrayList<Integer> approved_list;
    public JSONArray approved_listJSON;
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

        approved_list = new ArrayList<Integer>();
        friendsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.text_view, ((MainActivity)getActivity()).friends);

        MyCustomAdapter adapter = new MyCustomAdapter(((MainActivity)getActivity()).friends, setApprovedList.this, getActivity());
        friendsList.setAdapter(adapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code that will transition to next fragment which should ask for a few more details
                approved_listJSON = new JSONArray();
                for (int i=0; i < approved_list.size(); i++){
                    try{
                        approved_listJSON.put(((MainActivity) getActivity()).friendsJSON.get(approved_list.get(i)));
                    } catch (Exception e){
                        Log.e("Error: ", e.getMessage());
                    }
                }
                Log.d("APPROVED LIST JSON: ", approved_listJSON.toString());
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

    public void addPosToApprovedList(int pos) {
        approved_list.add(pos);
        Log.d("new approved list", approved_list.toString());
    }

    public void removePosFromApprovedList(int pos) {
        approved_list.remove((Object) pos);
        Log.d("new approved list", approved_list.toString());
    }

    public boolean PosIsApproved(int pos) {
        return approved_list.contains(pos);
    }


}
