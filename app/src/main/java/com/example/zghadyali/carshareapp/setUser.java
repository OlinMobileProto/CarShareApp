package com.example.zghadyali.carshareapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

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

        setOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAL = new setApprovedList();
                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
                handler.makeownercar(((MainActivity) getActivity()).profile_id, ((MainActivity) getActivity()).profile_name);
                ((MainActivity)getActivity()).transitionToFragment(setAL);
                mainActivity.transitionToFragment(setAL);
            }
        });

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
