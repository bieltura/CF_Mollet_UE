package com.app.bieltv3.cfmollet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class NoticiaActivity extends AppCompatActivity {

    // Variables enviades de NewsFragment
    protected String title, date, text, sectionName, url, foto;

    // Elements del layout
    protected TextView txtDate, txtTitle, txtText;
    protected ImageView imgFoto;

    // Barra de progres de carregar
    protected ProgressBar loading;

    // Intent d'on ve la informacio
    protected Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recuperem el intent anterior
        i = getIntent();

        // La URL la passem abans
        url = i.getStringExtra("url");

        // Localitzar cada element en el layout
        txtTitle = (TextView) findViewById(R.id.titleNoticia);
        txtDate = (TextView) findViewById(R.id.dataNoticia);
        txtText = (TextView) findViewById(R.id.textNoticia);
        imgFoto = (ImageView) findViewById(R.id.imgNoticia);

        // Barra de progress
        loading = (ProgressBar) findViewById(R.id.progess);

        // Boto de share
        FloatingActionButton shareNoticia = (FloatingActionButton) findViewById(R.id.shareNoticia);

        // Carregue la imatge de portada
        Load imageTask = new Load();
        imageTask.execute(url);

        assert shareNoticia != null;
        shareNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Preparem un intent de enviar
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");

                // Text a enviar: Seccio, titul i link
                String share = sectionName + ": " + title + "\n" + url;
                i.putExtra(Intent.EXTRA_TEXT, share);

                startActivity(i);
            }
        });
    }

    protected void assignarDades(Intent i) {

        // Conseguir els valors
        title = i.getStringExtra("title");
        date = i.getStringExtra("date");
        text = i.getStringExtra("text");
        sectionName = i.getStringExtra("sectionName");
        foto = i.getStringExtra("imageURL");
    }

    protected void assignarTitol(String title) {
        getSupportActionBar().setTitle(title);
    }

    protected Bitmap descarregarImatge(String url) {
        Bitmap p = null;
        try {
            p = Connect.getImage(url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return p;
    }

    protected class Load extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            // Asigenem les dades
            assignarDades(i);
            assignarTitol(sectionName);

            // Retornem la imatge que anirà adalt de la noticia
            return descarregarImatge(foto);
        }

        protected void onPostExecute(Bitmap result) {
            // Deixem de mostrar l'icona de carregar
            loading.setVisibility(View.INVISIBLE);

            // Afegim el text i la imatge
            imgFoto.setImageBitmap(result);
            txtText.setText(Html.fromHtml(text).toString().replace((char) 65532, (char) 32));
            txtTitle.setText(Html.fromHtml(title), TextView.BufferType.SPANNABLE);
            txtDate.setText("Publicat en " + date.substring(0, 10));

        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}