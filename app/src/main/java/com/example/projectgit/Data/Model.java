package com.example.projectgit.Data;

import java.util.ArrayList;

public class Model {

    private String task, description, id, date;
    private boolean check;

    public Model() {
    }

    public Model(String task, String description, String id, String date, boolean check) {
        this.task = task;
        this.description = description;
        this.id = id;
        this.date = date;
        this.check = check;
    }


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public  String getTask() { return task;}

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}