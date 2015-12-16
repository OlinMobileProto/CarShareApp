package com.example.zghadyali.carshareapp.Owner;

/**
 * Created by Jordan on 12/15/15.
 */
public class Request {

    private String id;
    private String name;
    private String date;
    private String fromTime;
    private String toTime;

    public Request(String id, String name, String date, String fromTime, String toTime) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
