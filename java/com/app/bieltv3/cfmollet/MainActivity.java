package com.app.bieltv3.cfmollet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar = null;
    DrawerLayout drawer = null;

    Fragment fragment = null;
    Intent intent = null;
    Intent download;

    String any = "1718";
    String horari;
    String share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Descàrregues previes per a iniciar la App:
        download = getIntent();
        horari = download.getStringExtra("horari");

        setContentView(R.layout.main_activity);

        // Barra superior
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Barra de navegacio (hamburguesa)
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Iniciem directament a partits
        navigationView.setCheckedItem(R.id.nav_partits);
        fragment = PartitsImageFragment.newInstance(any, horari);
        openFragment(fragment);

        navigationView.setNavigationItemSelectedListener(this);

        // Text a enviar
        String playStoreUrl = "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
        share = getResources().getString(R.string.app_official) + "\n" + playStoreUrl;

        // Service
        Intent mServiceIntent = new Intent(this, NewService.class);

        // Alarm manager que repeteix el service
        //AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        //Calendar cal = Calendar.getInstance();
        //PendingIntent servicePendingIntent = PendingIntent.getService(getApplicationContext(), 0, mServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Repetim el service cada 2 minuts:
        //int miliseconds = 2 * 60 * 1000;
        //am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 6000, servicePendingIntent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_valorar) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_partits:
                fragment = PartitsImageFragment.newInstance(any, horari);
                break;

            case R.id.nav_noticies:
                fragment = NoticiesFragment.newInstance("noticies", getResources().getString(R.string.noticies));
                break;

            case R.id.nav_croniques:
                fragment = NoticiesFragment.newInstance("croniques",  getResources().getString(R.string.croniques));
                break;

            case R.id.nav_himne:
                fragment = new HimneFragment();
                break;

            case R.id.nav_contactar:
                intent = new Intent(getApplicationContext(), ContactarTabActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_compartir:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, share);
                startActivity(intent);
                break;
        }

        openFragment(fragment);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void openFragment(final Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        //transaction.addToBackStack(null); TODO Problemes al tornar enrere recaregant fragments
        transaction.commit();
    }
}