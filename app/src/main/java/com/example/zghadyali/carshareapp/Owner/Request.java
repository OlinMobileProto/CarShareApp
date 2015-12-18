package com.example.zghadyali.carshareapp.Owner;

import org.json.JSONObject;

/**
 * Created by Jordan on 12/15/15.
 */
public class Request {

    private String id;
    private String name;
    private String reason;
    private String date;
    private String fromTime;
    private String toTime;

    public Request(JSONObject object) {
        try{
            this.id = object.getString("requestId");
            this.name = object.getString("borrowerName");
            this.reason = object.getString("optmessage");
            this.date = object.getString("date");
            this.fromTime = object.getString("startTime");
            this.toTime = object.getString("endTime");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
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

    public void setName(String newName) {
        this.name = newName;
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
