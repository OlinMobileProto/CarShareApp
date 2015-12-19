package com.example.zghadyali.carshareapp.Owner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment of the Owner's Setting page, where the Owner can edit their car's information
 */
public class OwnerSettings extends Fragment {

    private View view;
    private TextView carMake;
    private EditText editCarMake;
    private TextView carModel;
    private EditText editCarModel;
    private TextView licensePlate;
    private EditText editLicensePlate;
    private TextView hourlycharge;
    private EditText edithourlycharge;
    private Spinner transmissionSpinner;
    private Button updateButton;

    private String profileID;
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
        hourlycharge = (TextView)view.findViewById(R.id.pricing_text);
        edithourlycharge = (EditText)view.findViewById(R.id.pricing_edittext);
        transmissionSpinner = (Spinner)view.findViewById(R.id.settings_transmission_spinner);
        updateButton = (Button)view.findViewById(R.id.settings_update_button);

        cars = ((OwnerActivity)getActivity()).getCarInfo();
        profileID = ((OwnerActivity)getActivity()).getProfileID();
        Log.d("Stuff:", cars.toString());

        try {
            editCarMake.setText(cars.getString("make"));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }
        try {
            editCarModel.setText(cars.getString("model"));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }
        try {
            editLicensePlate.setText(cars.getString("licensePlate"));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }
        try {
            edithourlycharge.setText(cars.getString("moneyPolicy"));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }
        try {
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

                    newCarInfo.put("moneyPolicy", edithourlycharge.getText().toString());

                    newCarInfo.put("isAutomatic", transmissionSpinner.getSelectedItemPosition() != 0);
                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                handler.addcarinfo(profileID, newCarInfo);
            }
        });

        return view;
    }

}