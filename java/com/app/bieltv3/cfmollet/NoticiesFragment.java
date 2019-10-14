package com.app.bieltv3.cfmollet;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;


public class NoticiesFragment extends Fragment {

    // List View
    ListView list;
    ListViewAdapter adapter;

    // Butons de la barra inferior
    FloatingActionButton seguent;
    FloatingActionButton anterior;

    // Cadena de strings i fotos, cada posició una noticia
    String[] titles;
    String[] dates;
    String[] entrades;
    String[] url;
    Bitmap[] fotos;
    Bitmap defaultThumbnail;

    // Pagina de comentaris actual i maxim
    int page = 1;
    int pages = 0;

    // Qualitat de les imatges a descarregar:
    String qualitat = "medium_large";

    // Nom de la seccio - v4 s'utilitza en la noticia
    String sectionName;

    // URL de la secció i URL JSON
    String URL;
    String[] thumbnailImageURL;
    String[] mediumImageURL;
    String URLJson;

    // Tasca de carregar
    Load mTask;

    // Context de la aplicacio
    Context context;

    public static Fragment newInstance(String seccio, String seccioTitul) {
        NoticiesFragment noticiesFragment = new NoticiesFragment();

        // Afegim les variables que volem utilitzar
        Bundle args = new Bundle();

        // Passem el any de la competicio
        args.putString("seccio", seccioTitul);
        args.putString("url", "http://www.cfmolletue.com/category/" + seccio + "/");

        // Enviem les dades en els arguments (onCreate)
        noticiesFragment.setArguments(args);

        return noticiesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

        // Rebem la variable del newInstance
        sectionName = getArguments().getString("seccio");
        URL = getArguments().getString("url");
        URLJson = updateURL(page);

        // Get the view from listview_main.xml
        View view = inflater.inflate(R.layout.fragment_noticies, container, false);

        // Assignem cada element el seu respectio del layout
        list = (ListView) view.findViewById(R.id.listview);
        seguent = (FloatingActionButton) view.findViewById(R.id.seguent);
        anterior = (FloatingActionButton) view.findViewById(R.id.anterior);

        // Pagina 1, no hi ha anterior
        anterior.setVisibility(View.GONE);

        // Comprovem si hi ha internet
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        // Si hi ha connexió
        if (netInfo != null && netInfo.isConnectedOrConnecting() && URL != null) {
            // Accio de descarregar
            mTask = new Load(this.getActivity());

            // Descarreguem
            mTask.execute();

        }
        // Si no hi ha
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Error al intentar connectar ");
            alertDialog.setMessage("S'ha produït un error. Revisi la seva connexió i provi més tard.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Tancar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        // Capturem que s'ha pulsat un element de la llista
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), NoticiaActivity.class);
                i.putExtra("title", titles[position]);
                i.putExtra("date", dates[position]);
                i.putExtra("text", entrades[position]);
                i.putExtra("sectionName", sectionName);
                i.putExtra("url", url[position]);
                i.putExtra("imageURL", mediumImageURL[position]);
                startActivity(i);
            }
        });

        seguent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                URLJson = updateURL(page);

                // Task can only be executed once
                mTask = new Load(getContext());
                mTask.execute();
            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page -= 1;
                URLJson = updateURL(page);
                mTask = new Load(getContext());
                mTask.execute();
            }
        });

        return view;
    }

    public String updateURL(int page) {
        return URL + "page/" + String.valueOf(page) + "/?json=1";
    }

    public int getPercentatge(int value, int base) {
        return value * 100 / base;
    }

    private class Load extends AsyncTask<String, Integer, String> {

        private Context context;
        private ProgressDialog dialog;

        public Load(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonString = Connect.getHtml(URLJson);
            JSONObject jsonObject;
            JSONArray jsonArray;

            try {
                jsonObject = new JSONObject(jsonString);
                jsonArray = jsonObject.getJSONArray("posts");

                titles = new String[jsonArray.length()];
                dates = new String[jsonArray.length()];
                entrades = new String[jsonArray.length()];
                url = new String[jsonArray.length()];
                thumbnailImageURL = new String[jsonArray.length()];
                mediumImageURL = new String[jsonArray.length()];
                fotos = new Bitmap[jsonArray.length()];

                // Carreguem un cop la imatge defecte
                defaultThumbnail = Connect.getImage("http://www.cfmolletue.com/wp-content/uploads/2012/11/Mollet_mollet_escu.jpg");

                pages = jsonObject.getInt("pages");

                for (int i = 0; i < jsonArray.length(); i++) {

                    titles[i] = Connect.getStringFromJSONArray(jsonArray, i, "title_plain");
                    entrades[i] = Connect.getStringFromJSONArray(jsonArray, i, "content");
                    dates[i] = Connect.getStringFromJSONArray(jsonArray, i, "date");
                    url[i] = Connect.getStringFromJSONArray(jsonArray, i, "url");

                    try {
                        // A vegades es crean dos array de attachments, l'ultim sempre es el millor (normalment aixó val 0)
                        int index = jsonArray.getJSONObject(i).getJSONArray("attachments").length() - 1;

                        // Nove manera de descarregar imatges
                        thumbnailImageURL[i] = Html.fromHtml(jsonArray.getJSONObject(i).getJSONArray("attachments").getJSONObject(index).getJSONObject("images").getJSONObject("thumbnail").getString("url")).toString();
                        mediumImageURL[i] = Html.fromHtml(jsonArray.getJSONObject(i).getJSONArray("attachments").getJSONObject(index).getJSONObject("images").getJSONObject(qualitat).getString("url")).toString();
                    } catch (JSONException e) {
                        try {
                            // Manera antiga de descarregar imatges
                            thumbnailImageURL[i] = Html.fromHtml(jsonArray.getJSONObject(i).getJSONObject("thumbnail_images").getJSONObject("thumbnail").getString("url")).toString();
                            mediumImageURL[i] = Html.fromHtml(jsonArray.getJSONObject(i).getJSONObject("thumbnail_images").getJSONObject(qualitat).getString("url")).toString();
                        } catch (JSONException p) {
                            // Carreguem la imatge d'un escut sinó podem recuperar l'antigua
                            p.printStackTrace();
                            thumbnailImageURL[i] = "http://www.cfmolletue.com/wp-content/uploads/2012/11/Mollet_mollet_escu.jpg";
                            mediumImageURL[i] = thumbnailImageURL[i];
                            fotos[i] = defaultThumbnail;
                        }
                    }
                    if (fotos[i] != defaultThumbnail)
                        try {
                            fotos[i] = Connect.getImage(thumbnailImageURL[i]);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    // Fem el tan per cent
                    publishProgress(getPercentatge(i, jsonArray.length()));
                }
            } catch (JSONException | FileNotFoundException e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (page > 1)
                anterior.setVisibility(View.VISIBLE);
            else
                anterior.setVisibility(View.INVISIBLE);

            if (page >= pages)
                seguent.setVisibility(View.INVISIBLE);
            else
                seguent.setVisibility(View.VISIBLE);

            // Pass results to ListViewAdapter Class
            adapter = new ListViewAdapter(context, titles, dates, entrades, fotos);

            // Binds the Adapter to the ListView
            list.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage(getResources().getString(R.string.descarregant));
            dialog.setProgressNumberFormat(null);
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            dialog.setMessage(getResources().getString(R.string.carregant));
            dialog.setProgress(value[0]);
        }
    }
}
