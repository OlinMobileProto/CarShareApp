package com.example.zghadyali.carshareapp.Owner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jordan on 12/15/15.
 */
public class RequestAdapter extends ArrayAdapter<Request> {

    final private String ACCEPT = "OWNER_ACCEPTED";
    final private String DENY = "OWNER_DENIED";
    private ArrayList<Request> requests;

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.requests = requests;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Request request = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.owner_pending_request_list, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.request_name);
        TextView reason = (TextView) convertView.findViewById(R.id.request_reason_text);
        TextView date = (TextView) convertView.findViewById(R.id.date_text);
        TextView time = (TextView) convertView.findViewById(R.id.time_text);
        Button approveButton = (Button) convertView.findViewById(R.id.approve_button);
        Button denyButton = (Button) convertView.findViewById(R.id.deny_button);

        name.setText(request.getBorrowerName());
        reason.setText(request.getOptMessage());
        date.setText(request.getDate());
        time.setText(displayTime(request.getFromTime(), request.getToTime()));
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequests handler = new VolleyRequests(getContext());
                JSONObject requestDeny = new JSONObject();
                try {
                    requestDeny.put("approved", DENY);
                } catch (Exception e) {
                    Log.e("JSONException", e.getMessage());
                }
                handler.editrequest(request.getId(), requestDeny);
                requests.remove(position);
                notifyDataSetChanged();
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequests handler = new VolleyRequests(getContext());
                JSONObject requestApprove = new JSONObject();
                try {
                    requestApprove.put("approved", ACCEPT);
                } catch (Exception e) {
                    Log.e("JSONException", e.getMessage());
                }
                handler.editrequest(request.getId(), requestApprove);
                requests.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public String displayTime(String fromTime, String toTime) {
        int fromHourOfDay;
        int fromMinute;
        int toHourOfDay;
        int toMinute;
        int colonIndex;
        try{
            Log.d("TIME", fromTime);
        } catch (Exception e) {
            Log.e("ERROR", "sadness"+e.getMessage());
            e.printStackTrace();
        }

        colonIndex = fromTime.indexOf(":");
        fromHourOfDay = Integer.valueOf(fromTime.substring(0, colonIndex));
        fromMinute = Integer.valueOf(fromTime.substring(colonIndex + 1, fromTime.length()));

        colonIndex = toTime.indexOf(":");
        toHourOfDay = Integer.valueOf(toTime.substring(0, colonIndex));
        toMinute = Integer.valueOf(toTime.substring(colonIndex + 1, fromTime.length()));

        String convertFromTime = convertTime(fromHourOfDay, fromMinute);
        String convertToTime = convertTime(toHourOfDay, toMinute);

        return convertFromTime + " - " + convertToTime;
    }

    public String convertTime(int hourOfDay, int minute) {
        String res;
        if (hourOfDay < 12) {
            if (hourOfDay == 0) {
                if (minute < 10) {
                    res = (hourOfDay + 12) + ":" + "0" + minute + " AM";
                } else {
                    res = (hourOfDay + 12) + ":" + minute + " AM";
                }
            } else {
                if (minute < 10) {
                    res = hourOfDay + ":" + "0" + minute + " AM";
                } else {
                    res = hourOfDay + ":" + minute + " AM";
                }
            }
        } else {
            if (hourOfDay == 12) {
                if (minute < 10) {
                    res = hourOfDay + ":" + "0" + minute + " PM";
                } else {
                    res = hourOfDay + ":" + minute + " PM";
                }
            } else {
                if (minute < 10) {
                    res = (hourOfDay - 12) + ":" + "0" + minute + " PM";
                } else {
                    res = (hourOfDay - 12) + ":" + minute + " PM";
                }
            }
        }
        return res;
    }

}
