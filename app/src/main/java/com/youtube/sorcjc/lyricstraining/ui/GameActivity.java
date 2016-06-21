package com.youtube.sorcjc.lyricstraining.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class GameActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {


    private static String URL_SONG_BASE = null;
    private static final String TAG = "GameActivity";

    // Selected song
    private Song song;

    // UI components
    private TextView tvName;
    private ImageView btnPlay;
    private TextView tvStatus;
    private SeekBar seekBar;
    private TextView tvStartTime, tvFinalTime;
    private TextView tvLyric;
    private EditText etInput;
    private Context context;
    // Media player
    private MediaPlayer mediaPlayer;
    // To update the seekBar
    private Handler myHandler = new Handler();
    // Current position
    private static double startTime, finalTime;

    // Lyrics data
    private ArrayList<Lyric> lyrics;
    private static int currentLyric = -1;
    private static double checkPoint = 0;
    private static boolean isPlaying;

    // Game score
    // private static int good_answres;
    // private static int bad_answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        context = this;
        isPlaying = false;

        fetchBundleData();
        loadLyrics();

        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(song.getName());

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvFinalTime = (TextView) findViewById(R.id.tvFinalTime);

        tvLyric = (TextView) findViewById(R.id.tvLyric);
        etInput = (EditText) findViewById(R.id.etInput);

        btnPlay = (ImageView) findViewById(R.id.btnPlay);
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
        mediaPlayer.setOnCompletionListener(this);
        // To manage the buffer progress
        mediaPlayer.setOnBufferingUpdateListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlay:

                // To avoid problems when it is playing right now
                if (isPlaying) { // the isPlaying method returns TRUE when paused
                    Log.d(TAG, "Nothing to do here !");
                    return;
                }

                if (checkPoint != 0) { // If exist an advance, just resume
                    mediaPlayer.seekTo((int) checkPoint);
                    mediaPlayer.start();
                    isPlaying = true;
                    return;
                }

                Log.d(TAG, "Media player will be prepared");
                isPlaying = true;

                // Read the url base property the first time
                if (URL_SONG_BASE == null) {
                    URL_SONG_BASE = Utilitario.readProperties(context).getProperty("IP_SERVER");
                    URL_SONG_BASE += "music/" + song.getFileName();
                }

                try {
                    mediaPlayer.setDataSource(URL_SONG_BASE);
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
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) time);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) time) - TimeUnit.MINUTES.toSeconds(minutes);
        return minutes + ":" + (seconds<=9 ? "0"+seconds : seconds);
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
        Call<LyricsResponse> call = LyricsTrainingApiAdapter.getApiService(context).getLyricsResponse(song.getId());

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

                    Log.d(TAG, "Lyrics array length => " + lyrics.size());

                    // Select some words and replace with *s
                    selectAndReplaceRandomWords();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectAndReplaceRandomWords() {
        if (lyrics == null || lyrics.isEmpty())
            return;

        for (int i=0; i<lyrics.size(); ++i) {
            if (i%3==1)
                lyrics.get(i).selectRandomWord();
        }
    }

    private void updateLyric(final String formatTime) {
        if (lyrics == null)
            return;

        for (int i=currentLyric+1; i<lyrics.size(); ++i) {
            // We have to display the proper phrase when its time arrive
            Lyric lyric = lyrics.get(i);
            if (lyric.getStart().equals(formatTime)) {
                // To avoid additional UI updates
                if (i != currentLyric) {
                    // A change is needed
                    if (inputWordIsCorrect()) {
                        tvLyric.setText(lyric.getPhrase());
                        currentLyric = i;
                        checkPoint = mediaPlayer.getCurrentPosition();
                        // Continue :D
                        if (! isPlaying) {
                            mediaPlayer.start();
                            isPlaying = true;
                        }
                        etInput.setText("");
                        lyrics.get(currentLyric).setGood(true);
                    } else {
                        mediaPlayer.pause();
                        isPlaying = false;
                        lyrics.get(currentLyric).setBad(true);
                    }
                }
            }
        }

    }

    private boolean inputWordIsCorrect() {
        if (currentLyric == -1) return true; // Nothing to compare

        if (etInput == null) return false;

        Lyric lyric = lyrics.get(currentLyric);
        if (! lyric.hasSelectedWord()) return true; // Nothing to compare

        String input = etInput.getText().toString().trim();
        Log.d(TAG, "Input word => " + input);
        Log.d(TAG, "Selected word => " + lyric.getSelectedWord());
        return lyric.getSelectedWord().equals(input);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        int good_answers = 0, bad_answers = 0;
        // The mistakes just count one time
        for (Lyric lyric : lyrics) {
            if (lyric.isGood()) ++good_answers;
            if (lyric.isBad()) ++bad_answers;
        }
        int points = 5 * good_answers - 2 * bad_answers;
        String message = "Genial, usted obtuvo una puntuación de "+points+" puntos !";
        new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
                .setTitle(R.string.score_dialog_title)
                .setMessage(message)
                .setPositiveButton(R.string.score_yes_option, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopMediaPlayer();
                        finish();
                    }
                })
                .setNegativeButton(R.string.score_no_option, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), R.string.press_again, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

        resetGame();
    }

    private void resetGame() {
        mediaPlayer.seekTo(0);
    }

}
