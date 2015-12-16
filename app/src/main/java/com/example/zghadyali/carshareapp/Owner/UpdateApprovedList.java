package com.example.zghadyali.carshareapp.Owner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zghadyali.carshareapp.Owner.OwnerActivity;
import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.setALParent;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateApprovedList extends setALParent {

    private OwnerActivity ownerActivity;
    private UpdateApprovedList updateApprovedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (thisActivity instanceof OwnerActivity) {
            ownerActivity = (OwnerActivity) thisActivity;
        }

        doneButton.setText(R.string.update);
        // Hide login button
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setVisibility(View.GONE);

        buildApprovedList();
        return view;
    }

    @Override
    protected void setupThisActivity() {
        thisActivity = (OwnerActivity) getActivity();
    }

    @Override
    protected void transitionToNextFragment() {
        ownerActivity.transitionToHome();
    }

    @Override
    protected void makeNewFragment() {
        updateApprovedList = new UpdateApprovedList();
        ownerActivity.transitionToFragment(updateApprovedList);
    }

    private void buildApprovedList() {
        JSONObject carInfo = ownerActivity.getCarInfo();
        try {
            JSONArray approvedJSON = carInfo.getJSONArray("approvedList");
            Log.d("approvedJSON: ", approvedJSON.toString());
            for (int i = 0; i < approvedJSON.length(); i++) {
                approvedListIDs.add(approvedJSON.getString(i));
            }
            Log.d("updateApprovedList","approvedList set up");

        } catch (JSONException e) {
            Log.e("cannot get approvedJSON", e.getMessage());
        }
    }
}
