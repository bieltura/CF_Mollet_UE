package com.app.bieltv3.cfmollet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LinkActivity extends AppCompatActivity {

    private final String partitsUrl = "http://www.cfmolletue.com/partits/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String url = i.getDataString();

        if(i.getDataString().equals(partitsUrl)) {
            i = new Intent(this, IntroActivity.class);
            startActivity(i);
        }
        else {
            i = new Intent(this, NoticiaLinkActivity.class);
            i.putExtra("url", url);
            startActivity(i);
        }
        this.finish();
    }

}
