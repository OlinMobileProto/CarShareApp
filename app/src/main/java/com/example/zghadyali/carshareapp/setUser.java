package com.example.zghadyali.carshareapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zghadyali.carshareapp.Owner.BorrowerActivity;

public class setUser extends Fragment {

    public Button setOwner;
    public Button setBorrower;
    public Button next;
    public setApprovedList setAL;
    private MainActivity mainActivity;
    private Boolean isOwner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.set_user, container, false);

        mainActivity = (MainActivity) getActivity();
        setOwner = (Button) rootView.findViewById(R.id.set_owner);
        setBorrower = (Button) rootView.findViewById(R.id.set_borrower);

        // Get the friend list from the server
        mainActivity.setupFriends();

        setBorrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOwner = false;
            }
        });

        setOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOwner = true;
            }
        });

        next = (Button) rootView.findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOwner != null && isOwner){
                    setAL = new setApprovedList();
                    VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                    //Makes owner schema in the server database
                    handler.makeperson(((MainActivity) getActivity()).profileID, ((MainActivity) getActivity()).profile_name, "owner");
                    //Makes car schema in the server database
                    handler.makeownercar(((MainActivity) getActivity()).profileID, ((MainActivity) getActivity()).profile_name);

                    ((MainActivity)getActivity()).transitionToFragment(setAL);
                    mainActivity.transitionToFragment(setAL);

                } else if (isOwner != null && !isOwner){
                    VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                    //Makes person: borrower in the server database for facebook user
                    handler.makeperson(((MainActivity) getActivity()).profileID, ((MainActivity) getActivity()).profile_name, "borrower");
                    //Makes borrower schema in the server database
                    handler.makeborrower(((MainActivity) getActivity()).profileID, ((MainActivity) getActivity()).profile_name);
                    Intent borrower_intent = new Intent(getActivity().getApplicationContext(), BorrowerActivity.class);
                    borrower_intent.putExtra("profileID", mainActivity.profileID);
                    borrower_intent.putExtra("name", mainActivity.profile_name);
                    borrower_intent.putExtra("friends", mainActivity.friends);
                    borrower_intent.putExtra("friendsIDs", mainActivity.friendsIDs);
                    borrower_intent.putExtra("friendsJSON", mainActivity.friendsJSON.toString());
                    startActivity(borrower_intent);
                } else{

                }
            }
        });

        return rootView;
    }
}
