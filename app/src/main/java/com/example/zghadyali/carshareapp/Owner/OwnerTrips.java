package com.example.zghadyali.carshareapp.Owner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;
import com.example.zghadyali.carshareapp.Volley.callback_requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerTrips extends Fragment {

    private View rootView;
    private OwnerActivity ownerActivity;
    private TextView context;
    private ListView trips;
    private AllRequestsAdapter allRequestsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.borrower_trips, container, false);
        ownerActivity = (OwnerActivity) getActivity();
        if (ownerActivity.dispOwnerRequests.size() == 0){
            context = (TextView) rootView.findViewById(R.id.context);
            context.setText("You have not responded to any requests right now");
        } else {
            trips = (ListView) rootView.findViewById(R.id.requests_list);
            allRequestsAdapter = new AllRequestsAdapter(getContext(), ownerActivity.dispOwnerRequests);
            trips.setAdapter(allRequestsAdapter);
        }

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ownerActivity.getAllRequests();
                allRequestsAdapter.notifyDataSetChanged();
            }
        });



        return rootView;
    }


}