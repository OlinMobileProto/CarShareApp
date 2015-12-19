package com.example.zghadyali.carshareapp.SignUp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zghadyali.carshareapp.Borrower.BorrowerActivity;
import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.SignUp.MainActivity;
import com.example.zghadyali.carshareapp.SignUp.setApprovedList;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;

// fragment the user goes to after logging in if they do not exist on the server already. The first
// step in the user sign up flow. They sign up as a car owner or borrower. This is a binary decision
// so a user cannot be an owner and a borrower and this decision will define which activity launches
// when they open the app every subsequent time they open the app
public class setUser extends Fragment {

    private Button setOwner;
    private Button setBorrower;
    private Button next;
    private setApprovedList setAL;
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
                setBorrower.setSelected(true);
                setOwner.setSelected(false);
            }
        });

        setOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOwner = true;
                setOwner.setSelected(true);
                setBorrower.setSelected(false);
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
                    handler.makeperson(mainActivity.getProfileID(), mainActivity.getProfileName(), "owner");
                    //Makes car schema in the server database
                    handler.makeownercar(mainActivity.getProfileID(), mainActivity.getProfileName());

                    ((MainActivity)getActivity()).transitionToFragment(setAL);
                    mainActivity.transitionToFragment(setAL);

                } else if (isOwner != null && !isOwner){
                    VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());

                    //Makes person: borrower in the server database for facebook user
                    handler.makeperson(mainActivity.getProfileID(), mainActivity.getProfileName(), "borrower");
                    //Makes borrower schema in the server database
                    handler.makeborrower(mainActivity.getProfileID(), mainActivity.getProfileName());
                    Intent borrower_intent = new Intent(getActivity().getApplicationContext(), BorrowerActivity.class);
                    borrower_intent.putExtra("profileID", mainActivity.getProfileID());
                    borrower_intent.putExtra("name", mainActivity.getProfileName());
                    borrower_intent.putExtra("friends", mainActivity.getFriends());
                    borrower_intent.putExtra("friendsIDs", mainActivity.getFriendsIDs());
                    borrower_intent.putExtra("friendsJSON", mainActivity.getFriendsJSON().toString());
                    startActivity(borrower_intent);
                } else{

                }
            }
        });

        return rootView;
    }
}
