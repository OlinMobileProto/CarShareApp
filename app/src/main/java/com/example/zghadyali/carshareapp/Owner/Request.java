package com.example.zghadyali.carshareapp.Owner;

import android.util.Log;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Request object
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

    /**
     * Returns the hour given a string of time HH:mm
     * @param time a string containing time formatted HH:mm
     * @return int representing the hour
     */
    public int getHour(String time){
        int colon_index = time.indexOf(":");
        int HourOfDay = Integer.valueOf(time.substring(0, colon_index));
        return HourOfDay;
    }

    /**
     * Returns the minute given a string of time HH:mm
     * @param time a string containing time formatted HH:mm
     * @return int representing the minute
     */
    public int getMinute(String time){
        int colon_index = time.indexOf(":");
        int minute = Integer.valueOf(time.substring(colon_index + 1, time.length()));
        return minute;
    }

    /**
     * Given a string of a date (MM/dd/yyyy) and a string of a time (HH:mm), returns a Date object
     * @param date string representing a date formatted (MM/dd/yyyy)
     * @param time string representing a time formatted (HH:mm)
     * @return Date representing the given date and time
     */
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

    /**
     * gets the id of the request
     * @return String of request's ID
     */
    public String getId() {
        return id;
    }

    /**
     * gets the request's borrower's name
     * @return String of the request's borrower's name
     */
    public String getBorrowerName() {
        return borrowerName;
    }

    /**
     * gets the request's owner's name
     * @return String of the request's owner's name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * gets the request's date
     * @return String of the request's date
     */
    public String getDate() {
        return date;
    }

    /**
     * gets the request's starting time
     * @return String of the request's start time
     */
    public String getFromTime() {
        return fromTime;
    }

    /**
     * gets the request's ending time
     * @return String of the request's ending time
     */
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
