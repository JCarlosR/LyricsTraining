package com.youtube.sorcjc.lyricstraining.io.responses;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.lyricstraining.domain.Lyric;

import java.util.ArrayList;

public class LyricsResponse {

    @SerializedName("lyrics")
    private ArrayList<Lyric> lyrics;

    public ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

}
