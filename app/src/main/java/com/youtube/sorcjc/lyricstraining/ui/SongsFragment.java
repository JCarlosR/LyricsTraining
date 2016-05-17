package com.youtube.sorcjc.lyricstraining.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.youtube.sorcjc.lyricstraining.R;
import com.youtube.sorcjc.lyricstraining.domain.Song;
import com.youtube.sorcjc.lyricstraining.io.LyricsTrainingApiAdapter;
import com.youtube.sorcjc.lyricstraining.io.responses.SongsResponse;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFragment extends Fragment {

    private static final String ARG_GENRE_NAME = "genreName";
    private static final String ARG_GENRE_ID = "genreId";

    private String mGenreName;
    private int mGenreId;

    // UI components
    private TextView tvTitle, tvBody;

    // Temporary data
    private ArrayList<Song> songs;

    private OnFragmentInteractionListener mListener;

    public SongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param genreId Parameter 2.
     * @return A new instance of fragment SongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFragment newInstance(String param1, int genreId) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GENRE_NAME, param1);
        args.putInt(ARG_GENRE_ID, genreId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mGenreName = getArguments().getString(ARG_GENRE_NAME);
            mGenreId = getArguments().getInt(ARG_GENRE_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_songs, container, false);

        tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        tvTitle.setText("Musical genre: " + mGenreName);

        tvBody = (TextView) v.findViewById(R.id.tvBody);

        // Load songs from webservice
        loadSongs();

        return v;
    }

    public void loadSongs() {
        // Perform a request
        Call<SongsResponse> call = LyricsTrainingApiAdapter.getApiService().getSongsResponse(mGenreId);

        // Async callback
        call.enqueue(new Callback<SongsResponse>() {
            @Override
            public void onResponse(Response<SongsResponse> response, Retrofit retrofit) {
                if (response != null) {
                    songs = response.body().getSongs();

                    if (songs == null) {
                        Log.d("Test/SongsFragment", "No se encontraron canciones en el género " + mGenreName + " (" + mGenreId + ")");
                        return;
                    }

                    Log.d("Test/SongsFragment", "Cantidad de canciones " + mGenreName + " => " + songs.size());

                    String songsList = "";
                    for (Song song : songs) {
                        songsList += song+"\n";
                    }
                    tvBody.setText(songsList);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
