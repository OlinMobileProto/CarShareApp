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
 * Fragment to display an owner's pending requests and allows the owner to accept/deny requests
 */
public class OwnerRequests extends Fragment {

    //These three things are only used in onCReateView, they can be local variables
    private View view;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView requestListView;
    private ArrayList<Request> requests;
    private RequestAdapter requestAdapter;
    private OwnerActivity ownerActivity;
    private JSONArray requestsJSON;
    private TextView textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_owner_requests, container, false);

        textView = (TextView) view.findViewById(R.id.owner_pending_requests);
        ownerActivity = (OwnerActivity) getActivity();
        requestsJSON = ownerActivity.getPendingRequestsArray();
        requests = new ArrayList<>();
        if (requestsJSON == null) {
            textView.setText(R.string.no_pending_requests);
        } else {
            if (requestsJSON.length() == 0) {
                textView.setText(R.string.no_pending_requests);
            } else {
                textView.setText(R.string.pending_requests);
                for (int i = 0; i < requestsJSON.length(); i++) {
                    try {
                        requests.add(new Request((JSONObject) requestsJSON.get(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    /**
     * Updates the list of the owner's pending requests. Called when screen is swiped to refresh.
     */
    public void swipeUpdate() {
        ownerActivity.getRequests();
        requestsJSON = ownerActivity.getPendingRequestsArray();
        requests = new ArrayList<>();
        if (requestsJSON != null) {
            if (requestsJSON.length() == 0) {
                textView.setText(R.string.no_pending_requests);
            } else {
                textView.setText(R.string.pending_requests);
                for (int i = 0; i < requestsJSON.length(); i++) {
                    try {
                        requests.add(new Request((JSONObject) requestsJSON.get(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            requestAdapter.notifyDataSetChanged();
        } else {
            textView.setText(R.string.no_pending_requests);
            requestAdapter.notifyDataSetChanged();
        }
    }
}
