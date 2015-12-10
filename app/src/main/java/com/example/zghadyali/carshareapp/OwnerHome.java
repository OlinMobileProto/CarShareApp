package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private String profileID;
    private JSONObject cars;
    private Button update_carlocation;
    private Button update_keylocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_home, container, false);


        cars = ((OwnerActivity)getActivity()).getCarInfo();
        profileID = ((OwnerActivity)getActivity()).getProfileID();


        Log.d("CarInfo", cars.toString());
        carLocation = (TextView) view.findViewById(R.id.car_location);
        editCarLocation = (EditText) view.findViewById(R.id.car_location_edit);
        keyLocation = (TextView) view.findViewById(R.id.key_location);
        editKeyLocation = (EditText) view.findViewById(R.id.key_location_edit);
        update_carlocation = (Button) view.findViewById(R.id.car_location_update);
        update_keylocation = (Button) view.findViewById(R.id.key_location_update);
        try {
            editCarLocation.setText(cars.getString("parkedLocation"));
            editKeyLocation.setText(cars.getString("keysLocation"));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }

        update_carlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
                JSONObject new_carlocation = new JSONObject();
                try {
                    new_carlocation.put("parkedLocation", editCarLocation.getText().toString());
                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                handler.addcarinfo(profileID,new_carlocation);
            }
        });

        update_keylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
                JSONObject new_keylocation = new JSONObject();
                try {
                    new_keylocation.put("keysLocation", editKeyLocation.getText().toString());
                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                handler.addcarinfo(profileID,new_keylocation);
            }
        });



        return view;
    }
}
