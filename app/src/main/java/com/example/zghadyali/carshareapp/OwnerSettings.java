package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Spinner transmissionSpinner;
    private Button updateButton;

    private String profile_id;
    private JSONObject cars;


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
        transmissionSpinner = (Spinner)view.findViewById(R.id.settings_transmission_spinner);
        updateButton = (Button)view.findViewById(R.id.settings_update_button);

        cars = ((OwnerActivity)getActivity()).carInfo;
        profile_id = ((OwnerActivity)getActivity()).profileID;
        Log.d("Stuff:", cars.toString());

        try {
            editCarMake.setText(cars.getString("make"));
            editCarModel.setText(cars.getString("model"));
            editLicensePlate.setText(cars.getString("licensePlate"));
            transmissionSpinner.setSelection((cars.getBoolean("isAutomatic") ? 1 : 0));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
                JSONObject newCarInfo = new JSONObject();
                try {
                    newCarInfo.put("make", editCarMake.getText().toString());
                    newCarInfo.put("model", editCarModel.getText().toString());
                    newCarInfo.put("licensePlate", editLicensePlate.getText().toString());
                    newCarInfo.put("isAutomatic", transmissionSpinner.getSelectedItemPosition() != 0);
                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                handler.addcarinfo(profile_id, newCarInfo);
            }
        });

        return view;
    }

}
