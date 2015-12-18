package com.example.zghadyali.carshareapp.Borrower;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

/**
 * Created by Jordan on 12/18/15.
 */
public class EndTripAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Intent appIntent = new Intent(context, BorrowerActivity.class);
            appIntent.putExtra("isStart", false);
            appIntent.putExtra("id", extras.getString("id"));
            context.startActivity(appIntent);
        }

        wakeLock.release();
    }
}
