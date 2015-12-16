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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jordan on 12/14/15.
 */
public class OwnerRequests extends Fragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView requestListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_home, container, false);

        requestListView = (ListView) view.findViewById(R.id.pending_requests_list);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.owner_pending_requests_swipe_refresh);


        return view;
    }
}
