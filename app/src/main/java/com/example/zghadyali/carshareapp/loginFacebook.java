package com.example.zghadyali.carshareapp;

import android.content.Context;
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

        mainActivity.loginSetup(loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.accessToken == null) {
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));
                    //check if first time logging in or
                    SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                    if (preferences.contains("HAS_BEEN_RUN_FLAG")){
                        Log.d("I KNOW", "This is not your first time logging in");
                    }
                    else{
                        setuser = new setUser();
                        mainActivity.transitionToFragment(setuser);
                    }
                } else {
                    mainActivity.accessToken = null;
                    LoginManager.getInstance().logOut();
                    mainActivity.friends = new ArrayList<String>();
                }
            }
        });

    return rootview;
    }

}
