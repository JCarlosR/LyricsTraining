package com.youtube.sorcjc.lyricstraining.ui;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.youtube.sorcjc.lyricstraining.R;
import com.youtube.sorcjc.lyricstraining.domain.Song;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    private static final String URL_SONG_BASE = "http://redemnorte.pe/wslyrics/music/";

    // Selected song
    private Song song;

    // UI components
    private TextView tvName;
    private ImageButton btnPlay;
    private TextView tvStatus;
    private SeekBar seekBar;
    private TextView tvStartTime, tvFinalTime;

    // Media player
    private MediaPlayer mediaPlayer;
    // To update the seekBar
    private Handler myHandler = new Handler();
    // Current position
    private static double startTime, finalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fetchBundleData();

        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(song.getName());

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvFinalTime = (TextView) findViewById(R.id.tvFinalTime);

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
                final String url = URL_SONG_BASE + song.getFileName();

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
            tvStartTime.setText(formatTime(startTime));
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
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
