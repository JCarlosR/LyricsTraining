package com.youtube.sorcjc.lyricstraining.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youtube.sorcjc.lyricstraining.R;
import com.youtube.sorcjc.lyricstraining.domain.Lyric;
import com.youtube.sorcjc.lyricstraining.domain.Song;
import com.youtube.sorcjc.lyricstraining.global.Utilitario;
import com.youtube.sorcjc.lyricstraining.io.LyricsTrainingApiAdapter;
import com.youtube.sorcjc.lyricstraining.io.responses.LyricsResponse;
import com.youtube.sorcjc.lyricstraining.io.responses.SongsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {


    //private static final String URL_SONG_BASE = "http://redemnorte.pe/wslyrics/music/";
    private static final String TAG = "GameActivity";

    // Selected song
    private Song song;

    // UI components
    private TextView tvName;
    private ImageButton btnPlay;
    private TextView tvStatus;
    private SeekBar seekBar;
    private TextView tvStartTime, tvFinalTime;
    private TextView tvLyric;
    private Context contexto;
    // Media player
    private MediaPlayer mediaPlayer;
    // To update the seekBar
    private Handler myHandler = new Handler();
    // Current position
    private static double startTime, finalTime;

    // Lyrics data
    private ArrayList<Lyric> lyrics;
    private static int currentLyricId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        contexto = this;
        fetchBundleData();
        loadLyrics();
        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(song.getName());

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvFinalTime = (TextView) findViewById(R.id.tvFinalTime);

        tvLyric = (TextView) findViewById(R.id.tvLyric);

        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        if (btnPlay != null)
            btnPlay.setOnClickListener(this);

        setupMediaPlayer();
    }

    private void fetchBundleData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String songJSON = extras.getString("song");
            song = new Gson().fromJson(songJSON, Song.class);
        }
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        // To manage the buffer progress
        mediaPlayer.setOnBufferingUpdateListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlay:
                String url1;
                url1= Utilitario.readProperties(contexto).getProperty("IP_SERVER");
                final String url = url1+"music/" + song.getFileName();

                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    // The file doesn't exist
                    e.printStackTrace();
                    return;
                }

                tvStatus.setText(R.string.status_loading);
                mediaPlayer.prepareAsync();
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // When the buffer is ready to start playing
        mediaPlayer.start();
        tvStatus.setText(R.string.status_playing);
        myHandler.postDelayed(UpdateSongTime,100);

        finalTime = mediaPlayer.getDuration();
        seekBar.setMax((int) finalTime);
        tvFinalTime.setText(formatTime(finalTime));
    }

    private String formatTime(final double time) {
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) time),
                TimeUnit.MILLISECONDS.toSeconds((long) time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) time)));
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            final String formatStartTime = formatTime(startTime);
            tvStartTime.setText(formatStartTime);
            updateLyric(formatStartTime);
            seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.exit_dialog_title)
                .setMessage(R.string.exit_dialog_message)
                .setPositiveButton(R.string.yes_option, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopMediaPlayer();
                        finish();
                    }
                })
                .setNegativeButton(R.string.no_option, null)
                .show();
    }

    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            myHandler.removeCallbacks(UpdateSongTime);
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        seekBar.setSecondaryProgress(percent * (int) finalTime / 100);
        // Log.d(TAG, "Percent buffer: " + percent);
        // Log.d(TAG, "Start time: " + startTime);
    }

    public void loadLyrics() {
        // Perform a request
        Call<LyricsResponse> call = LyricsTrainingApiAdapter.getApiService(contexto).getLyricsResponse(song.getId());

        // Async callback
        call.enqueue(new Callback<LyricsResponse>() {
            @Override
            public void onResponse(Response<LyricsResponse> response, Retrofit retrofit) {
                if (response != null) {
                    lyrics = response.body().getLyrics();

                    if (lyrics == null) {
                        Log.d(TAG, "No se encontró la letra de la canción => " + song.getId());
                        return;
                    }
                    Log.d(TAG, "Cantidad de frases => " + lyrics.size());
                    // Ready to show lyrics according to the music advance
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLyric(final String formatTime) {
        if (lyrics == null)
            return;

        for (Lyric lyric : lyrics) {
            // We have to display the proper phrase
            if (lyric.getStart().equals(formatTime)) {
                if (lyric.getId()  != currentLyricId) { // To avoid extra updates in UI
                    tvLyric.setText(lyric.getPhrase());
                    currentLyricId = lyric.getId();
                }
            }
        }
    }
}
