package com.example.zghadyali.carshareapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Jordan on 11/15/15.
 */
public class Payment extends Fragment {

    private View view;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.venmo_payment, container, false);
        button = (Button)view.findViewById(R.id.venmo_button);

        button.setOnClickListener(new View.OnClickListener() {
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
                if(VenmoLibrary.isVenmoInstalled(getContext())) {
//                    Intent venmoIntent = VenmoLibrary.openVenmoPayment(appId, appName, recipient, amount, note, txn);
//                    startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
                    Log.d("VENMO INSTALLED", "INSTALLED");
                } else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse("market://details?id=com.venmo"));
//                    startActivity(intent);
                    Log.d("VENMO NOT INSTALLED", "NOT INSTALLED");

                }
            }
        });


        return view;
    }

}
