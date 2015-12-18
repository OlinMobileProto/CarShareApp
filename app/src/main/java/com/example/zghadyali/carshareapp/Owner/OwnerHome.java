package com.example.zghadyali.carshareapp.Owner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.Borrower.BorrowerActivity;
import com.example.zghadyali.carshareapp.Borrower.CarsListCustomAdapter;
import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

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

    //For Future and Current Cars Adapter
    public ListView carsListView;
    private AllRequestsAdapter requestAdapter;
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_home, container, false);

        final OwnerActivity owneractivity = ((OwnerActivity)getActivity());
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


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Swipe Refresh", "onRefresh called form SwipeRefreshLayout");
                swipeUpdate();
            }
        });
//
//        owneractivity.getfuturecurrentRequests();
        Log.d("message message", owneractivity.getFutureRequestsArray().toString());

        if (owneractivity.getFutureRequestsArray().size() == 0){
            TextView context = (TextView) view.findViewById(R.id.context);
            context.setText("You have no upcoming requests!");
        } else {
            Log.d("HERE", "jiu");
            carsListView = (ListView) view.findViewById(R.id.requests_list);
            requestAdapter = new AllRequestsAdapter(getContext(), owneractivity.getFutureRequestsArray());
            carsListView.setAdapter(requestAdapter);
        }


        update_carlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
                JSONObject new_carlocation = new JSONObject();
                try {
                    new_carlocation.put("parkedLocation", editCarLocation.getText().toString());
                } catch (Exception e) {
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

    private void swipeUpdate() {
        //SETTING DEFAULT DATE AND TIME
        final OwnerActivity owneractivity = ((OwnerActivity)getActivity());
        owneractivity.getfuturecurrentRequests();
    }
}
