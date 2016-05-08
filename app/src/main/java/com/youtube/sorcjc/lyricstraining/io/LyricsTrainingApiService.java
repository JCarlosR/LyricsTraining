package com.youtube.sorcjc.lyricstraining.io;

import com.youtube.sorcjc.lyricstraining.io.responses.GenresResponse;
import com.youtube.sorcjc.lyricstraining.io.responses.SongsResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface LyricsTrainingApiService {


    // http://redemnorte.pe/wslyrics/generos.php
    @GET("generos.php")
    Call<GenresResponse> getGenresResponse();

    // http://redemnorte.pe/wslyrics/canciones.php?genre=1
    @GET("canciones.php")
    Call<SongsResponse> getSongsResponse(@Query("genre") int idGenre);

}
