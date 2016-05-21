package com.youtube.sorcjc.lyricstraining.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youtube.sorcjc.lyricstraining.R;
import com.youtube.sorcjc.lyricstraining.domain.Song;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> songs;
    private Context context;

    public SongAdapter(Context context) {
        this.context = context;
        this.songs = new ArrayList<>();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.setName(song.getName());
        holder.setAuthor("Sora Amamiya");
        holder.setDuration("2:54");
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setAll(@NonNull ArrayList<Song> songs) {
        Log.d("Test/SongAdapter", "setAll fired !");
        this.songs.clear();
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private TextView tvAuthor;
        private TextView tvDuration;

        public SongViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);

            itemView.setOnClickListener(this);
        }

        public void setName(String name){
            tvName.setText(name);
        }

        public void setDuration(String duration) {
            if (duration==null || duration.isEmpty())
                tvDuration.setVisibility(View.GONE);
            else
                tvDuration.setText(duration);
        }

        public void setAuthor(String author) {
            tvAuthor.setText(author);
        }

        @Override
        public void onClick(View view) {
            /*Intent i = new Intent(view.getContext(), TalkActivity.class);
            Bundle b = new Bundle();
            b.putString("uid", uid);
            i.putExtras(b);
            view.getContext().startActivity(i);*/
        }

    }

}
