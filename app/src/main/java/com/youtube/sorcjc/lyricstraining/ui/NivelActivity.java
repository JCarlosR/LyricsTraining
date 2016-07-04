package com.youtube.sorcjc.lyricstraining.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.youtube.sorcjc.lyricstraining.R;
import com.youtube.sorcjc.lyricstraining.domain.Song;

public class NivelActivity extends AppCompatActivity implements View.OnClickListener{

    private Context contexto;
    private Button btnEasy;
    private Button btnMedium;
    private Button btnHard;
    private Button btnExpert;
    private Song song;
    private int nivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel);
        contexto = this;
        getBundleData();
        inicializar();
    }

    private void getBundleData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String songJSON = extras.getString("song");
            song = new Gson().fromJson(songJSON, Song.class);
        }
    }

    private void inicializar(){
        // Creando instancias
        btnEasy = (Button) findViewById(R.id.activity_leveL_buttonEasy);
        btnMedium = (Button) findViewById(R.id.activity_level_buttonMedium);
        btnHard = (Button) findViewById(R.id.activity_level_buttonHard);
        btnExpert = (Button) findViewById(R.id.activity_level_buttonExpert);

        // Declarando eventos
        btnEasy.setOnClickListener(this);
        btnMedium.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        btnExpert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_leveL_buttonEasy:
                nivel = 7;
                break;
            case R.id.activity_level_buttonMedium:
                nivel = 5;
                break;
            case R.id.activity_level_buttonHard:
                nivel = 3;
                break;
            case R.id.activity_level_buttonExpert:
                nivel = 2;
                break;
        }

        Intent i = new Intent(v.getContext(), GameActivity.class);
        Bundle b = new Bundle();
        b.putString("song", new Gson().toJson(song));
        b.putInt("nivel", nivel);
        i.putExtras(b);
        v.getContext().startActivity(i);
        finish();
    }
}
