package com.example.ukk2025_xiipplg1;

public class CategoryModel {

    private String id;
    private String category;

    public CategoryModel(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

