package com.youtube.sorcjc.lyricstraining.domain;

import com.google.gson.annotations.SerializedName;

public class Song {

    // {"id":"1","name":"Dont stop me now"}
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("archivo")
    private String fileName;

    @SerializedName("autor")
    private String author;

    @SerializedName("duracion")
    private String duration;

    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String archivo) {
        this.fileName = archivo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
