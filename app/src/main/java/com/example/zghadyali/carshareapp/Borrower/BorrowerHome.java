package com.example.zghadyali.carshareapp.Borrower;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.R;

public class BorrowerHome extends Fragment {

    public Button now;
    public ListView carsListView;
    public ArrayAdapter carsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BorrowerActivity borrowerActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_borrower_home, container, false);

        borrowerActivity = (BorrowerActivity) getActivity();

        Log.d("BORROWER HOME STATUS: ", "you are now in the borrower home fragment");
        now = (Button) rootview.findViewById(R.id.now_button);
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        carsListView = (ListView) rootview.findViewById(R.id.cars_list);
        carsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.text_view, borrowerActivity.carsList);
        carsListView.setAdapter(carsAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Swipe Refresh", "onRefresh called form SwipeRefreshLayout");
                swipeUpdate();
            }
        });

        if (borrowerActivity.len == 0){
            TextView context = (TextView) rootview.findViewById(R.id.context);
            context.setText("You are not approved to borrow any cars right now.");
            now.setVisibility(View.GONE);
        } else {

            carsListView = (ListView) rootview.findViewById(R.id.cars_list);
            final CarsListCustomAdapter adapter = new CarsListCustomAdapter(((BorrowerActivity)getActivity()).carsList, BorrowerHome.this, getActivity());
            carsListView.setAdapter(adapter);

        }

        return rootview;
    }

    private void swipeUpdate() {
        borrowerActivity.updateCarList();
        carsAdapter.notifyDataSetChanged();
    }


}
