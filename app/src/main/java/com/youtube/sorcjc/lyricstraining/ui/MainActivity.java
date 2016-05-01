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

import com.youtube.sorcjc.lyricstraining.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongsFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(SongsFragment.newInstance("UNO", "XD"), "ONE");
        adapter.addFrag(SongsFragment.newInstance("DOS", "XD"), "TWO");
        adapter.addFrag(SongsFragment.newInstance("TRES", "XD"), "THREE");
        adapter.addFrag(SongsFragment.newInstance("CUATRO", "XD"), "FOUR");
        adapter.addFrag(SongsFragment.newInstance("CINCO", "XD"), "FIVE");
        adapter.addFrag(SongsFragment.newInstance("SEIS", "XD"), "SIX");
        adapter.addFrag(SongsFragment.newInstance("SIETE", "XD"), "SEVEN");
        adapter.addFrag(SongsFragment.newInstance("OCHO", "XD"), "EIGHT");
        adapter.addFrag(SongsFragment.newInstance("NUEVE", "XD"), "NINE");
        adapter.addFrag(SongsFragment.newInstance("DIEZ", "XD"), "TEN");
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

