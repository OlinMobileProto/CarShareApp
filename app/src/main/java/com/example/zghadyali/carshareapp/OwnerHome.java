package com.example.zghadyali.carshareapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
        
        handler.getcarinfo(new callback_cars() {
            @Override
            public void callback(JSONObject cars) {
                Log.d("JSON CAR to string: ", cars.toString());
            }
        }, "1090612884283818");


        view = inflater.inflate(R.layout.owner_home, container, false);
        carLocation = (TextView)view.findViewById(R.id.car_location);
        editCarLocation = (EditText)view.findViewById(R.id.car_location_edit);
        keyLocation = (TextView)view.findViewById(R.id.key_location);
        editKeyLocation = (EditText)view.findViewById(R.id.key_location_edit);

        editCarLocation.setText("car is parked here");

        editKeyLocation.setText("Your pocket");

        return view;
    }
}
