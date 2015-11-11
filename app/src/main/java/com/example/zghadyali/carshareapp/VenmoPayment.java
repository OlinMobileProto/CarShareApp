package com.example.zghadyali.carshareapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zghadyali.carshareapp.VenmoLibrary.VenmoResponse;

/**
 * Created by Jordan on 11/10/15.
 */
public class VenmoPayment extends Fragment {

    private View view;
    private String appId = getString(R.string.appId);
    private String appName = getString(R.string.app_name);
    private String recipient = "venmo@venmo.com";
    private String amount = "0.10";
    private String note = "Thanks!";
    private String txn = "pay";
    private final int REQUEST_CODE_VENMO_APP_SWITCH = (int) Integer.getInteger(getString(R.string.appId));
    private String app_secret = getString(R.string.appSecret);
    private Button button;



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.venmo_payment, container, false);
        button = (Button) view.findViewById(R.id.venmo_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VenmoLibrary.isVenmoInstalled(getContext())) {
                    Intent venmoIntent = VenmoLibrary.openVenmoPayment(appId, appName, recipient, amount, note, txn);
                    startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
                }
                else {
                    Intent venmoIntent = VenmoLibrary.openVenmoPayment(appId, appName, recipient, amount, note, txn);
                    startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("RESULT CODE:", Integer.toString(resultCode));
        if (requestCode == REQUEST_CODE_VENMO_APP_SWITCH) {
            if (resultCode == getActivity().RESULT_OK) {
                String signedrequest = data.getStringExtra("signedrequest");
                if (signedrequest != null) {
                    VenmoLibrary.VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, app_secret);
                    if (response.getSuccess().equals("1")) {
                        //Payment successful.  Use data from response object to display a success message
                        String note = response.getNote();
                        String amount = response.getAmount();
                    }
                } else {
                    String error_message = data.getStringExtra("error_message");
                    //An error ocurred.  Make sure to display the error_message to the user
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //The user cancelled the payment
            }
        }
    }
}
