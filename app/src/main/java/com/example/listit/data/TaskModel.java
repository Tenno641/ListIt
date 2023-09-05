package com.example.listit.data;

public class TaskModel {

    private String body;
    private String time;
    private String date;
    private String id;

    public TaskModel(String body, String time, String date) {
        this.body = body;
        this.time = time;
        this.date = date;
    }

    public TaskModel(String id, String body, String time, String date) {
        this.id = id;
        this.body = body;
        this.time = time;
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return String.valueOf(this.id);
    }

    public void setId(String id) {
        this.id = id;
    }

}
