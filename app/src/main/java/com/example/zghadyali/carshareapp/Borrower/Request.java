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

// request object implemented using a JSON object for each request. This object is how we use each request
// later on in the app. We have several getter functions for this object

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
            /**
             * It seems like a lot of these are used in other aspects of the code
             * it would be best to define these string literals in a separate file that
             * hold a lot of public static final Strings that all hold the values stored
             * here. That way if you change something in the server that slightly changes
             * the names you only have to change it in one spot.
             */
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

    /**
     * Just again the date stuff here could be done better
     */
    //gets the hour using the calendar object as an integer
    //This method could also be refactored if dates are used
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
    }

    //gets the minute using the calendar object as an integer
    //Same here
    public int getMinute(String time){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        try{
            cal.setTime(sdf.parse(time));
        } catch (ParseException e){
            e.getMessage();
        }
        return cal.get(Calendar.MINUTE);
    }

    //gets the date from the request as a Calendar object and converts it to a date
    //Same here
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

    //by using the date, we can compare the objects and order the requests such that the ones in the future
    // (the ones the user cares more about) appear at the top in the list view
    @Override
    public int compareTo(Request request) {
        return getCalendarDate(getDate(), getFromTime()).compareTo(request.getCalendarDate(request.getDate(), request.getFromTime()));
    }

    // Getters and setters should be at the top of the class, but thats pretty minor
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