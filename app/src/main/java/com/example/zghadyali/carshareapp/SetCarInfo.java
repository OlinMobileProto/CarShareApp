package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Jordan on 11/16/15.
 */
public class SetCarInfo extends Fragment {

    private View view;
    private EditText carParked;
    private EditText keyLocation;
    private EditText hourlyRate;
    private MainActivity mainActivity;
    private JSONObject setcarinfo;
    private Button carinfo_next;

    public SetCarInfo() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.set_car_info, container, false);

        mainActivity = (MainActivity) getActivity();


        carParked = (EditText) view.findViewById(R.id.car_location_edit_signup);
        keyLocation = (EditText) view.findViewById(R.id.key_location_edit_signup);
        hourlyRate = (EditText) view.findViewById(R.id.hourly_rate_edit_signup);
        Button carinfo_next = (Button) view.findViewById(R.id.next_to_details);

        carinfo_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carparkedlocation = carParked.getText().toString();
                String keylocationedit = keyLocation.getText().toString();
                String hourlyRatecar = hourlyRate.getText().toString();

                JSONObject setcarinfo = new JSONObject();
                try {
                    setcarinfo.put("parkedLocation", carparkedlocation);
                } catch (Exception e) {
                    Log.e("ERROR!", e.getMessage());
                }
                try {
                    setcarinfo.put("keysLocation", keylocationedit);
                } catch (Exception e) {
                    Log.e("ERROR!", e.getMessage());
                }
                try {
                    setcarinfo.put("moneyPolicy", hourlyRatecar);
                } catch (Exception e) {
                    Log.e("ERROR!", e.getMessage());
                }

                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
                handler.addcarinfo(((MainActivity) getActivity()).profile_id, setcarinfo);

                Intent intent = new Intent(getActivity(), OwnerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
