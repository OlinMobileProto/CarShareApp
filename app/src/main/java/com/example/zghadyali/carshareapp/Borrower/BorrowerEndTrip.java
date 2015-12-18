package com.example.zghadyali.carshareapp.Borrower;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.example.zghadyali.carshareapp.Owner.OwnerHome;
import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.Venmo.VenmoLibrary;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;
import com.example.zghadyali.carshareapp.Volley.callback_cars;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jordan on 12/18/15.
 */
public class BorrowerEndTrip extends Fragment {

    private View view;
    private TextView returnText;
    private TextView whereParked;
    private EditText whereParkedEdit;
    private Button confirmParked;
    private TextView returnKeys;
    private TextView keysLocation;
    private Button confirmKeys;
    private TextView paymentMessage;
    private Button venmo;
    private boolean carParked;
    private boolean keysReturned;
    private String carId;
    private JSONObject car;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.borrower_end_trip, container, false);

        carParked = false;
        keysReturned = false;

        returnText = (TextView) view.findViewById(R.id.return_text);
        whereParked = (TextView) view.findViewById(R.id.where_parked);
        whereParkedEdit = (EditText) view.findViewById(R.id.where_parked_edit);
        confirmParked = (Button) view.findViewById(R.id.confirm_park_button);
        returnKeys = (TextView) view.findViewById(R.id.return_keys);
        keysLocation = (TextView) view.findViewById(R.id.keys_location);
        confirmKeys = (Button) view.findViewById(R.id.confirm_keys_button);
        paymentMessage = (TextView) view.findViewById(R.id.payment_message);
        venmo = (Button) view.findViewById(R.id.venmo_button);

        carId = ((BorrowerActivity)getActivity()).getCurrentId();

        VolleyRequests handler = new VolleyRequests(getContext());
        handler.getcarinfo(new callback_cars() {
            @Override
            public void callback(JSONObject cars) {
                car = cars;
                Log.d("JSON CAR to string: ", cars.toString());
            }
        }, carId);

        try {
            returnText.setText(R.string.returning + car.getString("owner") + R.string.car_of);
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }

        try {
            whereParkedEdit.setText(car.getString("parkedLocation"));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }

        try {
            keysLocation.setText(car.getString("keysLocation"));
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }

        try {
            paymentMessage.setText(R.string.thank + car.getString("owner") + R.string.thank_you_message);
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }

        confirmParked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carParked = true;
                confirmParked.setBackgroundColor(Color.DKGRAY);
            }
        });

        confirmKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keysReturned = true;
                confirmKeys.setBackgroundColor(Color.DKGRAY);
            }
        });

        try {
            paymentMessage.setText(R.string.thank + car.getString("owner") + R.string.thank_you_message);
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }

        venmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appId = getString(R.string.appId);
                String appName = getString(R.string.app_name);
                String recipient = "venmo@venmo.com";
                String amount = "0.10";
                String note = "Thanks!";
                String txn = "pay";
                final int REQUEST_CODE_VENMO_APP_SWITCH = Integer.parseInt(getString(R.string.appId));
                String app_secret = getString(R.string.appSecret);
                if (VenmoLibrary.isVenmoInstalled(getContext())) {
                    Intent venmoIntent = VenmoLibrary.openVenmoPayment(appId, appName, recipient, amount, note, txn);
                    startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.venmo"));
                    startActivity(intent);
                }
            }
        });
        ((BorrowerActivity) getActivity()).transitionToFragment(new BorrowerHome());
        return view;
    }

}
