package com.youtube.sorcjc.lyricstraining.domain;

import com.google.gson.annotations.SerializedName;

public class Lyric {

    // {"id":"1","start":"0:00","end":"0:12","phrase":"Tonight I'm gonna have myself a real good time "}
    @SerializedName("id")
    private int id;

    @SerializedName("start")
    private String start;

    @SerializedName("end")
    private String end;

    @SerializedName("phrase")
    private String phrase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}
