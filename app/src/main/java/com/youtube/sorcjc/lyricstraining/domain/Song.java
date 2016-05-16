package com.youtube.sorcjc.lyricstraining.domain;

import com.google.gson.annotations.SerializedName;

public class Song {

    // {"id":"1","name":"Dont stop me now"}
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("archivo")
    private String archivo;

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

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
