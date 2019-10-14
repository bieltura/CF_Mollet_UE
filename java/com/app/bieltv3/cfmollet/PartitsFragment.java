package com.app.bieltv3.cfmollet;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PartitsFragment extends Fragment {

    private String anyCompeticio;
    private String taulaHorari;
    private String tipusFutbol[];

    // Constructor buit necesari en els Fragments
    public PartitsFragment(){}

    // Creem un metode per a insanciar el fragment i passar els valors per parametres
    public static PartitsFragment newInstance(String any, String horari) {
        PartitsFragment partitsFragment = new PartitsFragment();

        // Afegim les variables que volem utilitzar
        Bundle args = new Bundle();

        // Passem el any de la competicio
        args.putString("any", any);
        args.putString("horari", horari);

        // Enviem les dades en els arguments (onCreate)
        partitsFragment.setArguments(args);

        return partitsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicialitzem les variables
        tipusFutbol = new String[] {"11","7","femeni"};

        //taulaHorariPerTipus = new String[tipusFutbol];

        anyCompeticio = getArguments().getString("any");
        taulaHorari = getArguments().getString("horari");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_partits, container, false);

        // Create the adapter that will return a fragment for each of the three primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Creem la nova instancia del equip depenent del tipus de futbol
            return EquipsFragment.newInstance(anyCompeticio,tipusFutbol[position], taulaHorari);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tipus_futbol)[position];
        }
    }

}
