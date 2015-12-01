package com.example.zghadyali.carshareapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
 * A simple {@link Fragment} subclass.
 */
public class setApprovedList extends Fragment {

    public ListView friendsList;
    public EditText searchFriends;
    public ArrayAdapter<String> friendsAdapter;
    public ArrayList<Integer> approved_list;
    public JSONArray approved_listJSON;
    public ArrayList<Integer> approvedList;
    public LoginButton loginButton;
    public loginFacebook loginfb;
    public Button next;
    private JSONArray approvedJSONarray;
    private MainActivity mainActivity;
    private SetCarInfo setCarInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.set_approved_list, container, false);

        mainActivity = (MainActivity) getActivity();
        friendsList = (ListView) rootview.findViewById(R.id.friends_list);
        searchFriends = (EditText) rootview.findViewById(R.id.search_friends_list);
        next = (Button) rootview.findViewById(R.id.next_to_details);

        approvedList = new ArrayList<Integer>();
        approvedJSONarray = new JSONArray();
        friendsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.text_view, mainActivity.friends);

        MyCustomAdapter adapter = new MyCustomAdapter(mainActivity.friends, setApprovedList.this, getActivity());
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
                setCarInfo = new SetCarInfo();

                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                for (int k = 0; k < approvedList.size(); k++ ){
                    try {
                        approvedJSONarray.put((mainActivity.friendsJSON).getJSONObject(approvedList.get(k)));
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
                mainActivity.preferences.edit().putBoolean("FB_LOG_IN", false).apply();
                mainActivity.friends = new ArrayList<String>();
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


}
