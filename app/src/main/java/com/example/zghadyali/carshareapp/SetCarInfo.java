package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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


        Button button = (Button)view.findViewById(R.id.done_car_info);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OwnerActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
