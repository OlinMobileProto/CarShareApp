package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Jordan on 11/16/15.
 */
public class SetCarInfo extends Fragment {

    private View view;
    private EditText carParked;
    private EditText keyLocaiton;
    private EditText hourlyRate;


    public SetCarInfo() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.set_car_info, container, false);



        return view;
    }
}
