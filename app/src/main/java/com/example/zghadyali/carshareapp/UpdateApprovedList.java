package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateApprovedList extends setALParent {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        doneButton.setText("Update");
        //TODO hide login button
        return view;
    }

    @Override
    protected void setupThisActivity() {
        thisActivity = (OwnerActivity) getActivity();
    }

    @Override
    protected void transistionToNextFragment() {
        thisActivity.onBackPressed();
    }
}
