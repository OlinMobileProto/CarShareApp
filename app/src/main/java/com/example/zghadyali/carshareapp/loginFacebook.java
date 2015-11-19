package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;

public class loginFacebook extends Fragment {

    public LoginButton loginButton;
    public setUser setuser;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.login_facebook, container, false);

        mainActivity = (MainActivity) getActivity();
        loginButton = (LoginButton) rootview.findViewById(R.id.login_button);
        loginButton.setFragment(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.preferences.contains("FB_ACCESS_TOKEN") && !mainActivity.preferences.getBoolean("FB_LOG_IN", false)) {
                    //you want to log in and go to your home page
                    mainActivity.loginSetup(loginButton, false);
                    Log.d("You are", "going to go to your home page flow brah depending on if you are a borrower or owner");
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));
                } else {
                    mainActivity.loginSetup(loginButton, true);
                    Log.d("You are", "continuing your sign up flow brah, thrilled to meet you");
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));
                    setuser = new setUser();
                    mainActivity.transitionToFragment(setuser);
                }
            }
        });

    return rootview;
    }

}
