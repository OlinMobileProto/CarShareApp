package com.example.zghadyali.carshareapp.Borrower;


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

// displays all of the requests the borrower has made using our custom adapter for each request
// the requests are organized so that upcoming requests appear at the top of the list
public class BorrowerTrips extends Fragment {

    private View rootView;
    private BorrowerActivity borrowerActivity;
    private TextView context;
    private ListView trips;
    private RequestAdapter requestAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.borrower_trips, container, false);
        borrowerActivity = (BorrowerActivity) getActivity();
        if (borrowerActivity.dispBorrowerRequests.size() == 0){
            context = (TextView) rootView.findViewById(R.id.context);
            context.setText("You have not made any requests right now");
        } else {
            trips = (ListView) rootView.findViewById(R.id.requests_list);
            requestAdapter = new RequestAdapter(getContext(), borrowerActivity.dispBorrowerRequests);
            trips.setAdapter(requestAdapter);
        }

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                borrowerActivity.getRequests();
                requestAdapter.notifyDataSetChanged();
            }
        });



        return rootView;
    }


}
