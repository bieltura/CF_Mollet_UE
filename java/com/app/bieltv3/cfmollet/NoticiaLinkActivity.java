package com.app.bieltv3.cfmollet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

public class NoticiaLinkActivity extends NoticiaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void assignarDades(Intent i) {
        Log.e("Notiica",url);
        String jsonString = Connect.getHtml(url+"?json=1");

        JSONObject jsonObject;
        JSONObject jsonObjectPage;


        try {
            jsonObject = new JSONObject(jsonString);
            jsonObjectPage = jsonObject.getJSONObject("post");

            title = jsonObjectPage.getString("title");
            date = jsonObjectPage.getString("date");
            text = jsonObjectPage.getString("content");
            //sectionName = jsonObjectPage.getJSONArray("categories").getJSONObject(0).getString("title");
            foto = jsonObjectPage.getJSONArray("attachments").getJSONObject(0).getString("url");

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void assignarTitol(String title) {
        // Blanc, no podem actualitzar UI fora de context
    }
}