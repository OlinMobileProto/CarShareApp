package com.example.zghadyali.carshareapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class BorrowerHome extends Fragment {

    public Button now;
    public ListView carsListView;
    public ArrayAdapter carsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_borrower_home, container, false);

        now = (Button) rootview.findViewById(R.id.now_button);
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        carsListView = (ListView) rootview.findViewById(R.id.cars_list);
        carsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.cars_list, ((BorrowerActivity)getActivity()).carsList);
        carsListView.setAdapter(carsAdapter);


        return rootview;
    }



}
