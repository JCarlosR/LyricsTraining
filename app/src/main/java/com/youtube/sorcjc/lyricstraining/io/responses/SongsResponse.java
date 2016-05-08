package com.youtube.sorcjc.lyricstraining.io.responses;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.lyricstraining.domain.Song;

import java.util.ArrayList;

public class SongsResponse {

    @SerializedName("songs")
    private ArrayList<Song> songs;

    public ArrayList<Song> getSongs() {
        return songs;
    }

}
