package com.example.zghadyali.carshareapp.Owner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zghadyali.carshareapp.Borrower.*;
import com.example.zghadyali.carshareapp.R;

import java.util.ArrayList;

/**
 * Same as RequestAdapter for BorrowerTrips, but for OwnerTrips
 */
public class AllRequestsAdapter extends ArrayAdapter<Request> {
    /**
     * Since this is almost entirely the same as RequestAdapter could you just define one
     * and then figure out a way to either conditionally change the things that are
     * different or pass in the different things? I think its just the textView differences
     */
    public AllRequestsAdapter(Context context, ArrayList<Request> requests) {
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

        //This should be in string.xml, well the second part
        name.setText(request.getBorrowerName() +" requested your car");
        message.setText(request.getOptMessage());
        //THese should be in a constants file so you only have to change it in one
        //spot if its changes
        if (request.getStatus().equals("PENDING")){
            status.setText(request.getStatus());
        }
        if (request.getStatus().equals("OWNER_ACCEPTED")){
            //Strings.xml
            status.setText("ACCEPTED");
            status.setTextColor(Color.GREEN);
        }
        if (request.getStatus().equals("OWNER_DENIED")){
            //Strings.xml
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

    public String displayTime(Request request){
        String convertFromTime = convertTime(request.getHour(request.getFromTime()), request.getMinute(request.getFromTime()));
        String convertToTime = convertTime(request.getHour(request.getToTime()), request.getMinute(request.getToTime()));
        return convertFromTime + " - " + convertToTime;
    }

    /**
     * Just the same date comments as in Borrower, these could be a lot shorter
     * if you used Date
     */
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
