package com.example.zghadyali.carshareapp.Borrower;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.R;

import java.util.ArrayList;

/**
 * Created by Jordan on 12/15/15.
 */

// this is the request adapter that takes in all of the requests and displays them. This adapter
// does not do anything else other than displaying each request
public class RequestAdapter extends ArrayAdapter<Request> {

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Request request = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.borrower_request_list, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.request_title);
        TextView message = (TextView) convertView.findViewById(R.id.opt_message);
        TextView status = (TextView) convertView.findViewById(R.id.request_status);
        TextView date = (TextView) convertView.findViewById(R.id.request_date);
        TextView time = (TextView) convertView.findViewById(R.id.request_time);

        name.setText("You requested " + request.getOwnerName());
        message.setText(request.getOptMessage());
        if (request.getStatus().equals("PENDING")){
            status.setText(request.getStatus());
        }
        if (request.getStatus().equals("OWNER_ACCEPTED")){
            status.setText("ACCEPTED");
            status.setTextColor(Color.GREEN);
        }
        if (request.getStatus().equals("OWNER_DENIED")){
            status.setText("DENIED");
            status.setTextColor(Color.RED);
        }
        if (request.getStatus().equals("DONE")){
            status.setText(request.getStatus());
            status.setTextColor(Color.LTGRAY);
        }


        date.setText(request.getDate());
        String dispTime = displayTime(request);
        time.setText(dispTime);
        return convertView;
    }

    //takes in a request, gets the hour and gets the minute and places it in a string for us to display for each request
    public String displayTime(Request request){
        String convertFromTime = convertTime(request.getHour(request.getFromTime()), request.getMinute(request.getFromTime()));
        String convertToTime = convertTime(request.getHour(request.getToTime()), request.getMinute(request.getToTime()));
        return convertFromTime + " - " + convertToTime;
    }

    // takes in 2 integers and returns a string that contains the time in 24 hour format
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
