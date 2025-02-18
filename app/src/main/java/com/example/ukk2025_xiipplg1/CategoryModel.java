package com.example.ukk2025_xiipplg1;

public class CategoryModel {

    private String nama;
    private int id;

    public CategoryModel(String nama, int id) {
        this.nama = nama;
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public int getId() {
        return id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setId(int id) {
        this.id = id;
    }
}

