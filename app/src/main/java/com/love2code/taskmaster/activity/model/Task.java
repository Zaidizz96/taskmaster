package com.love2code.taskmaster.activity.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    String title;
    String body;
    public java.util.Date dateCreated;
    TaskState state;

    public Task() {
    }

    public Task(String title, String body, Date dateCreated , TaskState state) {
        this.title = title;
        this.body = body;
        this.state = state;
        this.dateCreated=dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }
}
