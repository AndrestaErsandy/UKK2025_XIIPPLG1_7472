package com.example.ukk2025_xiipplg1;

public class    TaskModel {
    private String id;
    private String category;
    private String task;
    private String status;

    public TaskModel(String id, String category, String task, String status) {
        this.id = id;
        this.category = category;
        this.task = task;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTask() {
        return task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}