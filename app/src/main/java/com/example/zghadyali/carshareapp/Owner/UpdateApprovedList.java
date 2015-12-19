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
 * The fragment in the owner activity if the user wants to modify their approved list.
 */
public class UpdateApprovedList extends setALParent {

    private OwnerActivity ownerActivity;
    private UpdateApprovedList updateApprovedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (thisActivity instanceof OwnerActivity) {
            // Make the OwnerActivity-specific stuff available
            ownerActivity = (OwnerActivity) thisActivity;
        }

        // Buttons
        doneButton.setText(R.string.update);
        // Hide the login button
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setVisibility(View.GONE);

        buildApprovedList();
        return view;
    }

    @Override
    protected void setupThisActivity() {
        thisActivity = (OwnerActivity) getActivity();
    }

    /**
     * Go back to OwnerHome when this fragment is done.
     */
    @Override
    protected void transitionToNextFragment() {
        ownerActivity.transitionToHome();
    }

    @Override
    protected void makeNewFragment() {
        updateApprovedList = new UpdateApprovedList();
        ownerActivity.transitionToFragment(updateApprovedList);
    }

    /**
     * Make sure we have the owner's previously-approved list from the server.
     */
    private void buildApprovedList() {
        JSONObject carInfo = ownerActivity.getCarInfo();
        try {
            //This could be in a constant file
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
