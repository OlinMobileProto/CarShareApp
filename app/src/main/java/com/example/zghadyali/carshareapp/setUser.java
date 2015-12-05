package com.example.zghadyali.carshareapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class setUser extends Fragment {

    public Button setOwner;
    public Button setBorrower;
    public Button next;
    public setApprovedList setAL;
    private MainActivity mainActivity;
    private Boolean isOwner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.set_user, container, false);

        mainActivity = (MainActivity) getActivity();
        setOwner = (Button) rootView.findViewById(R.id.set_owner);
        setBorrower = (Button) rootView.findViewById(R.id.set_borrower);

        setBorrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOwner = false;
            }
        });

        setOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOwner = true;
            }
        });

        GraphRequestAsyncTask request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            mainActivity.friends = new ArrayList<String>();
                            mainActivity.friendsIDs = new ArrayList<String>();
                            JSONObject res = response.getJSONObject();
                            mainActivity.friendsJSON = res.getJSONArray("data");
                            Log.d("friendsJSON: ", mainActivity.friendsJSON.toString());
                            if (mainActivity.friendsJSON != null) {
                                int len = mainActivity.friendsJSON.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject test = mainActivity.friendsJSON.getJSONObject(i);
                                    mainActivity.friends.add(test.get("name").toString());
                                    mainActivity.friendsIDs.add(test.get("id").toString());
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }
                    }
                }
        ).executeAsync();
        
        next = (Button) rootView.findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOwner != null && isOwner){
                    setAL = new setApprovedList();
                    VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                    //Makes owner schema in the server database
                    handler.makeperson(((MainActivity) getActivity()).profile_id, ((MainActivity) getActivity()).profile_name, "owner");
                    //Makes car schema in the server database
                    handler.makeownercar(((MainActivity) getActivity()).profile_id, ((MainActivity) getActivity()).profile_name);

                    ((MainActivity)getActivity()).transitionToFragment(setAL);
                    mainActivity.transitionToFragment(setAL);

                } else if (isOwner != null && !isOwner){
                    VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                    //Makes person: borrower in the server database for facebook user
                    handler.makeperson(((MainActivity) getActivity()).profile_id, ((MainActivity) getActivity()).profile_name, "borrower");
                    //Makes borrower schema in the server database
                    handler.makeborrower(((MainActivity) getActivity()).profile_id, ((MainActivity) getActivity()).profile_name);
                    Intent borrower_intent = new Intent(getActivity().getApplicationContext(), BorrowerActivity.class);
                    borrower_intent.putExtra("profile_id", mainActivity.profile_id);
                    borrower_intent.putExtra("name", mainActivity.profile_name);
                    borrower_intent.putExtra("friends", mainActivity.friends);
                    borrower_intent.putExtra("friendsIDs", mainActivity.friendsIDs);
                    borrower_intent.putExtra("friendsJSON", mainActivity.friendsJSON.toString());
                    startActivity(borrower_intent);
                } else{

                }
            }
        });


        return rootView;
    }
}
