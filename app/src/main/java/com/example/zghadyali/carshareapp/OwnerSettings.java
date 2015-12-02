package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jordan on 12/1/15.
 */
public class OwnerSettings extends Fragment {

    private View view;
    private TextView carMake;
    private EditText editCarMake;
    private TextView carModel;
    private EditText editCarModel;
    private TextView licensePlate;
    private EditText editLicensePlate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_settings, container, false);
        carMake = (TextView)view.findViewById(R.id.settings_car_make);
        editCarMake= (EditText)view.findViewById(R.id.settings_car_make_edit);
        carModel = (TextView)view.findViewById(R.id.settings_car_model);
        editCarModel = (EditText)view.findViewById(R.id.settings_car_model_edit);
        licensePlate = (TextView)view.findViewById(R.id.settings_license_plate);
        editLicensePlate = (EditText)view.findViewById(R.id.settings_license_plate_edit);


        return view;
    }

}
