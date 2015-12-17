package com.example.zghadyali.carshareapp.Borrower;

import org.json.JSONObject;

/**
 * Created by Jordan on 12/15/15.
 */
public class Request {

    private String id;
    private String borrower_name;
    private String owner_name;
    private String date;
    private String fromTime;
    private String toTime;
    private int status;

    public Request(JSONObject object) {
        try{
            this.id = object.getString("requestId");
            this.borrower_name = object.getString("borrowerName");
            this.owner_name = object.getString("ownerName");
            this.date = object.getString("date");
            this.fromTime = object.getString("startTime");
            this.toTime = object.getString("endTime");
            this.status = object.getInt("approved");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public String getBorrowerName() {
        return borrower_name;
    }

    public String getOwnerName() {
        return owner_name;
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

    public int getStatus() {
        return status;
    }

    public void setBorrowername(String newName) {
        this.borrower_name = newName;
    }

    public void setOwnername(String newName) {
        this.owner_name = newName;
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