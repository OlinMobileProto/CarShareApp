package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jordan on 11/18/15.
 */
public class OwnerHome extends Fragment {

    private View view;
    private TextView carLocation;
    private EditText editCarLocation;
    private TextView keyLocation;
    private EditText editKeyLocation;
    private String profile_id;
    private JSONObject cars;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_home, container, false);

        cars = ((OwnerActivity)getActivity()).getCarInfo();

        Log.d("CarInfo", cars.toString());
        carLocation = (TextView) view.findViewById(R.id.car_location);
        editCarLocation = (EditText) view.findViewById(R.id.car_location_edit);
        keyLocation = (TextView) view.findViewById(R.id.key_location);
        editKeyLocation = (EditText) view.findViewById(R.id.key_location_edit);
        try {
            editCarLocation.setText(cars.getString("parkedLocation"));
            editKeyLocation.setText(cars.getString("keysLocation"));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }


        return view;
    }
}
