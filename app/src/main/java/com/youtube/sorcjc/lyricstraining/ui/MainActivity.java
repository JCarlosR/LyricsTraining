package com.youtube.sorcjc.lyricstraining.ui;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.youtube.sorcjc.lyricstraining.R;
import com.youtube.sorcjc.lyricstraining.domain.Genre;
import com.youtube.sorcjc.lyricstraining.io.LyricsTrainingApiAdapter;
import com.youtube.sorcjc.lyricstraining.io.responses.GenresResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements SongsFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        genresRequest();
    }

    private void genresRequest() {
        // Load genres data from the webservice
        Call<GenresResponse> call = LyricsTrainingApiAdapter.getApiService().getGenresResponse();
        Log.d("Test/Main", "Se lanzó el llamado al WS");

        // Async callback
        call.enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Response<GenresResponse> response, Retrofit retrofit) {
                Log.d("Test/Main", "Se recibió una respuesta");
                if (response != null) {
                    ArrayList<Genre> genres = response.body().getGenres();
                    Log.d("Test/Main", "Cantidad de géneros obtenidos => " + genres.size());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Test/Main", "Se recibió una respuesta errada");
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Temporary data
        ArrayList<String> genres = new ArrayList();
        genres.add("Pop");
        genres.add("Rock");
        genres.add("HipHop");
        genres.add("Metal");
        genres.add("Soul");
        genres.add("Jazz");

        // Add each genre like a fragment
        for (String genre : genres) {
            adapter.addFrag(SongsFragment.newInstance("Musical genre: " + genre, "Other param"), genre);
        }

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

