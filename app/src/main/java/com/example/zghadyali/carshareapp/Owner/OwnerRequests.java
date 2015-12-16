package com.example.zghadyali.carshareapp.Owner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jordan on 12/14/15.
 */
public class OwnerRequests extends Fragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView requestListView;
    private ArrayList<Request> requests;
    private RequestAdapter requestAdapter;
    private OwnerActivity ownerActivity;
    private JSONArray requestsJSON;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_home, container, false);

        ownerActivity = (OwnerActivity) getActivity();
        requestsJSON = ownerActivity.getPendingRequestsArray();
        requests = new ArrayList<>();
        if (requestsJSON.length() == 0 || requestsJSON == null){
            
        } else {
            for (int i = 0; i < requestsJSON.length(); i++) {
                try {
                    requests.add(new Request((JSONObject) requestsJSON.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        requestListView = (ListView) view.findViewById(R.id.pending_requests_list);

        requestAdapter = new RequestAdapter(getContext(), requests);
        requestListView.setAdapter(requestAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.owner_pending_requests_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Swipe Refresh", "onRefresh called form SwipeRefreshLayout");
                swipeUpdate();
            }
        });

        return view;
    }

    public void swipeUpdate() {
        ownerActivity.getRequests();
        ownerActivity.makePendingRequests();
        requestsJSON = ownerActivity.getPendingRequestsArray();
        requests = new ArrayList<>();
        for (int i = 0; i < requestsJSON.length(); i++) {
            try {
                requests.add(new Request((JSONObject) requestsJSON.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        requestAdapter.notifyDataSetChanged();
    }
}
