package com.youtube.sorcjc.lyricstraining.domain;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Random;

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

    // Use "transient" to exclude from deserialization
    private transient String selectedWord = null;

    // Used to calculate the score
    private boolean good = false;
    private boolean bad = false;

    public void setGood(boolean value) {
        this.good = value;
    }

    public void setBad(boolean value) {
        this.bad = value;
    }

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

    public boolean hasSelectedWord() {
        return selectedWord != null;
    }

    public String getSelectedWord() {
        return selectedWord;
    }

    public void selectRandomWord() {
        String[] words = phrase.split(" ");
        int wordsNumber = words.length;
        int randomPosition = new Random().nextInt(wordsNumber);

        int charsNumber = words[randomPosition].length(); // Generate *s

        // Save the selected random word and update the phrase
        this.selectedWord = words[randomPosition];
        words[randomPosition] = new String(new char[charsNumber]).replace("\0", "*");;
        this.phrase = TextUtils.join(" ", words);
    }

    public boolean isGood() {
        return good;
    }

    public boolean isBad() {
        return bad;
    }
}
