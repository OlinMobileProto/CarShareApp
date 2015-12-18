package com.example.zghadyali.carshareapp.Borrower;

import android.util.Log;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jordan on 12/15/15.
 */
public class Request implements Comparable<Request> {

    private String id;
    private String borrowerName;
    private String ownerName;
    private String optMessage;
    private String date;
    private String fromTime;
    private String toTime;
    private String status;

    public Request(JSONObject object) {
        try{
            this.id = object.getString("requestId");
            this.borrowerName = object.getString("borrowerName");
            this.ownerName = object.getString("ownerName");
            this.optMessage = object.getString("optmessage");
            this.date = object.getString("date");
            this.fromTime = object.getString("startTime");
            this.toTime = object.getString("endTime");
            this.status = object.getString("approved");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getHour(String time){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        try{
            cal.setTime(sdf.parse(time));
        } catch (ParseException e){
            e.getMessage();
        }
        Log.d("HOUR: ", String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        return cal.get(Calendar.HOUR_OF_DAY);

//        int colon_index = time.indexOf(":");
//        int HourOfDay = Integer.valueOf(time.substring(0, colon_index));
//        return HourOfDay;
    }

    public int getMinute(String time){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        try{
            cal.setTime(sdf.parse(time));
        } catch (ParseException e){
            e.getMessage();
        }
        return cal.get(Calendar.MINUTE);

//        int colon_index = time.indexOf(":");
//        int minute = Integer.valueOf(time.substring(colon_index + 1, time.length()));
//        return minute;
    }

    public Date getCalendarDate(String date, String time){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
        try {
            cal.setTime(sdf.parse(date + " " + time));
        } catch (ParseException e){
            Log.e("Error: ", e.getMessage());
        }
        return cal.getTime();
    }

    @Override
    public int compareTo(Request request) {
        return getCalendarDate(getDate(), getFromTime()).compareTo(request.getCalendarDate(request.getDate(), request.getFromTime()));
    }

    public String getId() {
        return id;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getDate() {
        return date;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public String getStatus() {
        return status;
    }

    public String getOptMessage() {
        return optMessage;
    }

    public void setBorrowername(String newName) {
        this.borrowerName = newName;
    }

    public void setOwnername(String newName) {
        this.ownerName = newName;
    }


    public void setDate(String newDate) {
        this.date = newDate;
    }

    public void setFromTime(String newFromTime) {
        this.fromTime = newFromTime;
    }

    public void setToTime(String newToTime) {
        this.toTime = newToTime;
    }

}