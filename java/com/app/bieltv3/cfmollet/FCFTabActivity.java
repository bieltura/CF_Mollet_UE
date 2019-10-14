package com.app.bieltv3.cfmollet;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

public class FCFTabActivity extends AppCompatActivity {

    private String FCFurl;
    private String seccions[];
    private boolean FCFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Rebem les dades del equip seleccionat
        Intent i = getIntent();

        String any          = i.getStringExtra("any");
        String tipusFutbol  = i.getStringExtra("tipusFutbol");
        String grup         = i.getStringExtra("grup");
        String competicio   = i.getStringExtra("competicio");

        // La URL de fcf: seccio/any/futbo-X/competicio/grup-X
        FCFurl = "/" + any + "/futbol-" + tipusFutbol + "/" + competicio + "/grup-"+ grup;
        seccions = new String[] {"resultats", "classificacio", "calendari"};

        // Utiltizem la versio mobil de fcf?
        FCFm = false;

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            String url = "http://fcf.cat/";

            if (FCFm)
                // Creem la nova URL cada cop que cambien de pagina
                url = "http://m.fcf.cat/";

            url = url + seccions[position] + FCFurl;

            // Creem la nova instancia de FCF amb aquesta URL per a cada pagina
            return FCFFragment.newInstance(url);
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.seccio_fcf)[position];
        }
    }
}