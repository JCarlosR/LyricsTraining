package com.youtube.sorcjc.lyricstraining.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.youtube.sorcjc.lyricstraining.R;
import com.youtube.sorcjc.lyricstraining.domain.Song;

import java.io.IOException;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    private static final String URL_SONG_BASE = "http://redemnorte.pe/wslyrics/music/";

    // Selected song
    private Song song;

    // UI components
    private TextView tvName;
    private ImageButton btnPlay;
    private TextView tvStatus;

    // Media player
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fetchBundleData();

        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(song.getName());

        tvStatus = (TextView) findViewById(R.id.tvStatus);

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlay:
                final String url = URL_SONG_BASE + song.getArchivo();

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
        mediaPlayer.start();
        tvStatus.setText(R.string.status_playing);
    }

}
