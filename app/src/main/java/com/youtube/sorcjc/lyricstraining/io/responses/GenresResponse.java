package com.youtube.sorcjc.lyricstraining.io.responses;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.lyricstraining.domain.Genre;

import java.util.ArrayList;

public class GenresResponse {

    @SerializedName("genres")
    private ArrayList<Genre> genres;

    public ArrayList<Genre> getGenres() {
        return genres;
    }

}
