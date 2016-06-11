package com.youtube.sorcjc.lyricstraining.io;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.youtube.sorcjc.lyricstraining.global.Utilitario;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class LyricsTrainingApiAdapter {

    private static LyricsTrainingApiService API_SERVICE;

    public static LyricsTrainingApiService getApiService(Context ctx) {

        // Creating the interceptor, and setting the log level
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Ruta global
        String lsRuta= Utilitario.readProperties(ctx).getProperty("IP_SERVER");

        // Adding the interceptor to a client
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);


        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(lsRuta)
                    .addConverterFactory(buildGsonConverter())
                    .client(httpClient) // <-- using the log level
                    .build();
            API_SERVICE = retrofit.create(LyricsTrainingApiService.class);
        }

        return API_SERVICE;
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // We don't need custom deserializers :v
        return GsonConverterFactory.create(gsonBuilder.create());
    }


}
