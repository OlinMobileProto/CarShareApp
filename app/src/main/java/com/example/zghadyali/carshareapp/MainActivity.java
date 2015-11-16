package com.example.zghadyali.carshareapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {

    public loginFacebook loginfb;
    private Payment venmoPayment = new Payment();
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        String appId = getString(R.string.appId);
        String appName = getString(R.string.app_name);
        String recipient = "venmo@venmo.com";
        String amount = "0.10";
        String note = "Thanks!";
        String txn = "pay";
        final int REQUEST_CODE_VENMO_APP_SWITCH = Integer.parseInt(getString(R.string.appId));
        String app_secret = getString(R.string.appSecret);
//        if(VenmoLibrary.isVenmoInstalled(getApplicationContext())) {
//            Intent venmoIntent = VenmoLibrary.openVenmoPayment(appId, appName, recipient, amount, note, txn);
//            startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("market://details?id=com.venmo"));
//            startActivity(intent);
//
//        }


        loginfb = new loginFacebook();
//        transitionToFragment(loginfb);
        transitionToFragment(venmoPayment);
    }

    public void transitionToFragment(Fragment fragment){
        //This function takes as input a fragment, initializes the fragment manager and replaces
        //the container with the provided fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void venmoPaymentFunction() {
        String appId = getString(R.string.appId);
        String appName = getString(R.string.app_name);
        String recipient = "venmo@venmo.com";
        String amount = "0.10";
        String note = "Thanks!";
        String txn = "pay";
        final int REQUEST_CODE_VENMO_APP_SWITCH = Integer.parseInt(getString(R.string.appId));
        String app_secret = getString(R.string.appSecret);
        if(VenmoLibrary.isVenmoInstalled(getApplicationContext())) {
            Intent venmoIntent = VenmoLibrary.openVenmoPayment(appId, appName, recipient, amount, note, txn);
            startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
            alertDialog.setTitle("Install Venmo");

            alertDialog.setPositiveButton("Google Play Store", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.venmo"));
                    startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.create();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        loginfb.callbackManager.onActivityResult(requestCode, resultCode, data);
        final int REQUEST_CODE_VENMO_APP_SWITCH = Integer.parseInt(getString(R.string.appId));
        String app_secret = getString(R.string.appSecret);
        if (requestCode == REQUEST_CODE_VENMO_APP_SWITCH) {
            if (resultCode == RESULT_OK) {
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
            } else if (resultCode == RESULT_CANCELED) {
                //The user cancelled the payment
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
