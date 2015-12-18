package com.example.zghadyali.carshareapp.Borrower;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Jordan on 12/15/15.
 */
public class Request {

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