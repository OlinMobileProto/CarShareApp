package com.example.zghadyali.carshareapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class loginFacebook extends Fragment {

    public LoginButton loginButton;
    public CallbackManager callbackManager;
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.login_facebook, container, false);

        loginButton = (LoginButton) rootview.findViewById(R.id.login_button);
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Access token: ", AccessToken.getCurrentAccessToken().toString());
                Log.d("Profile: ", Profile.getCurrentProfile().toString());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }



        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));
            }
        });



    return rootview;
    }
}
