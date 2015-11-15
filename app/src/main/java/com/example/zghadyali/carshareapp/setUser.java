package com.example.zghadyali.carshareapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

public class setUser extends Fragment {

    public Button setOwner;
    public Button setBorrower;
    public LoginButton loginButton;
    public loginFacebook loginfb;
    public setApprovedList setAL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.set_user, container, false);

        setOwner = (Button) rootview.findViewById(R.id.set_owner);
        setBorrower = (Button) rootview.findViewById(R.id.set_borrower);

        setOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAL = new setApprovedList();
                ((MainActivity)getActivity()).transitionToFragment(setAL);
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
