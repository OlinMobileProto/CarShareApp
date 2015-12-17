package com.example.zghadyali.carshareapp.Borrower;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jordan on 12/15/15.
 */
public class RequestAdapter extends ArrayAdapter<Request> {

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Request request = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.requests_list, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.request_title);
        TextView date = (TextView) convertView.findViewById(R.id.request_date);
        TextView time = (TextView) convertView.findViewById(R.id.request_time);

        name.setText("You requested " + request.getOwnerName());
        date.setText(request.getDate());
        String dispTime = displayTime(request.getFromTime(), request.getToTime());
        time.setText(dispTime);
        return convertView;
    }

    public String displayTime(String fromTime, String toTime){
        int fromHourOfDay;
        int fromMinute;
        int toHourOfDay;
        int toMinute;
        int colon_index;
        colon_index = fromTime.indexOf(":");
        fromHourOfDay = Integer.valueOf(fromTime.substring(0, colon_index));
        fromMinute = Integer.valueOf(fromTime.substring(colon_index + 1, fromTime.length()));

        colon_index = toTime.indexOf(":");
        toHourOfDay = Integer.valueOf(toTime.substring(0, colon_index));
        toMinute = Integer.valueOf(toTime.substring(colon_index + 1, fromTime.length()));

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
