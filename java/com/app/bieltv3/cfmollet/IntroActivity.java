package com.app.bieltv3.cfmollet;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

public class IntroActivity extends Activity {
    
    ProgressBar loading;

    public static String horariTaula = "";
    public static final String PREFERENCE_NAME = "DataLastModified";
    public static final String DATE = "date";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        
        loading = (ProgressBar) findViewById(R.id.barIntro);
        
        // Comprovem si hi ha internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        
        // Si hi ha connexió
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        	Load load = new Load();
            load.execute();
        }
        // Si no hi ha
        else {
        	AlertDialog alertDialog = new AlertDialog.Builder(IntroActivity.this).create();
			alertDialog.setTitle("Error al intentar connectar ");
			alertDialog.setMessage("S'ha produït un error. Revisi la seva connexió i provi més tard.");
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Tancar",
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			            dialog.dismiss();
			            finish();
			        }
			    });
			alertDialog.show();
        }
    }
    
    public class Load extends AsyncTask<String, Integer, String> {
    	
        protected String doInBackground(String... urls) {

            publishProgress(20);
        	
        	// Horaris
            String jsonString = Connect.getHtml("http://www.cfmolletue.com/partits/?json=1");

            publishProgress(50);

            JSONObject jsonObject;
        	JSONObject jsonObjectPage;
        	
        	try {
				jsonObject = new JSONObject(jsonString);
                publishProgress(65);
				jsonObjectPage = jsonObject.getJSONObject("page");
                publishProgress(80);
				horariTaula = jsonObjectPage.getString("content");
                publishProgress(100);
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

            // Data per les notificacions
            SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit();
            editor.putLong(DATE, Connect.getModifiedDate("http://www.cfmolletue.com/category/noticies"));

            editor.commit();
        	
        	return horariTaula;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            loading.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String horariTaula) {



        	// Carreguem activity
        	Intent i = new Intent(IntroActivity.this, MainActivity.class);
            i.putExtra("horari", horariTaula);
            startActivity(i);
            finish();
        }
    }
}