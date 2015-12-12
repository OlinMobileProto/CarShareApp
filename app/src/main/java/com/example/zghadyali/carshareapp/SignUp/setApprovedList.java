package com.example.zghadyali.carshareapp.SignUp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.setALParent;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class setApprovedList extends setALParent {

    private MainActivity mainActivity;
    private setApprovedList setAL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = super.onCreateView(inflater, container, savedInstanceState);

        if (thisActivity instanceof MainActivity) {
            // Make the MainActivity-specific stuff available
            mainActivity = (MainActivity) thisActivity;
        }

        doneButton.setText(R.string.next);

        loginButton = (LoginButton) rootview.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        //should always be logging you out and log out should return you to first screen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.accessToken = null;
                LoginManager.getInstance().logOut();
                mainActivity.setFriends(new ArrayList<String>());
                mainActivity.setFriendsIDs(new ArrayList<String>());

                loginfb = new loginFacebook();
                mainActivity.transitionToFragment(loginfb);
            }
        });

        return rootview;
    }

    @Override
    protected void setupThisActivity() {
        thisActivity = (MainActivity) getActivity();
    }

    @Override
    protected void makeNewFragment() {
        setAL = new setApprovedList();
        mainActivity.transitionToFragment(setAL);
    }

    @Override
    protected void transitionToNextFragment() {
        setCarInfo = new SetCarInfo();
        mainActivity.transitionToFragment(setCarInfo);
    }
}
