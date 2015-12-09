package com.example.zghadyali.carshareapp;

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
    public LoginButton loginButton;
    public loginFacebook loginfb;
    public setApprovedList setAL;
    private MainActivity mainActivity;

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
                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                //Makes borrower schema in the server database for facebook user
                handler.makeperson(mainActivity.getProfileID(),mainActivity.profile_name, "borrower");
            }
        });

        setOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAL = new setApprovedList();
                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                //Makes owner schema in the server database
                handler.makeperson(mainActivity.getProfileID(),mainActivity.profile_name, "owner");
                //Makes car schema in the server database
                handler.makeownercar(mainActivity.getProfileID(), mainActivity.profile_name);

//                ((MainActivity)getActivity()).transitionToFragment(setAL);
                mainActivity.transitionToFragment(setAL);
            }
        });

        // Get the friend list from the server
        mainActivity.setupFriends();

        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setFragment(this);

        //should always be logging you out and log out should return you to first screen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.accessToken = null;
                LoginManager.getInstance().logOut();
                mainActivity.setFriends(new ArrayList<String>());

                loginfb = new loginFacebook();
                mainActivity.transitionToFragment(loginfb);
            }
        });

        return rootView;
    }
}
