package com.example.zghadyali.carshareapp.SignUp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zghadyali.carshareapp.R;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

// fragment that is launched first from MainActivity. Just creates the Facebook login button and logs
// the user in
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
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));
            }
        });

    return rootview;
    }

}
