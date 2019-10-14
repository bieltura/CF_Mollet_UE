package com.app.bieltv3.cfmollet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

public class NewService extends Service {

    String url = "http://www.cfmolletue.com/category/noticies";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    final String DATE = "DATA";
    final String TITLE = "TITLE";

    String title;
    String text = null;
    String urlJ = null;
    Bitmap thumbnail = null;


    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;


    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

            preferences = getSharedPreferences(IntroActivity.PREFERENCE_NAME, MODE_PRIVATE);

            //try {
                //while(true) {

                  //  Thread.sleep(5000);

                    Log.e("PREFERE DATE",String.valueOf(preferences.getLong(IntroActivity.DATE, 0)));
                    Log.e("UPDATED DATE",String.valueOf(Connect.getModifiedDate(url)));


                    // Busquem si s'ha modificat la data
                    long updatedDate = Connect.getModifiedDate(url);

                    // Si la data original es diferent a la nova refrescada
                    //if (!true) {
                    if (preferences.getLong(IntroActivity.DATE, 0) != updatedDate) {

                        editor = getSharedPreferences(IntroActivity.PREFERENCE_NAME, MODE_PRIVATE).edit();

                        // Pasarem a comparar amb la nova
                        editor.putLong(IntroActivity.DATE, updatedDate);

                        // Rebem totes les dades de la primera noticia

                        // URL a JSON Object
                        String URLJson = url + "/?json=1";
                        String jsonString = Connect.getHtml(URLJson);

                        // Dades d'una noticia
                        title = null;
                        text = null;
                        thumbnail = null;

                        JSONObject jsonObject;
                        JSONArray jsonArray;

                        try {
                            jsonObject = new JSONObject(jsonString);
                            jsonArray = jsonObject.getJSONArray("posts");

                            // Valors de text
                            title = Html.fromHtml(Connect.getStringFromJSONArray(jsonArray, 0, "title_plain")).toString();
                            text = Html.fromHtml(Connect.getStringFromJSONArray(jsonArray, 0, "content")).toString();
                            urlJ = Html.fromHtml(Connect.getStringFromJSONArray(jsonArray, 0, "url")).toString();

                            // Imatge per mostrar a la notificació - thumbnail
                            int index = jsonArray.getJSONObject(0).getJSONArray("attachments").length() - 1;
                            String thumbnailImageURL = Html.fromHtml(jsonArray.getJSONObject(0).getJSONArray("attachments").getJSONObject(index).getJSONObject("images").getJSONObject("thumbnail").getString("url")).toString();
                            thumbnail = Connect.getImage(thumbnailImageURL);

                        } catch (JSONException | FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        // Si l'antic titol és el mateix, no notifiquem de canvi

                        editor.apply();

                        //if (!preferences.getString(TITLE, "").equalsIgnoreCase(title)) {

                        // Actualitzem el valor
                        editor.putString(TITLE, title);

                        // Intent que obrirà al clickar la notificacio: NoticiaLinkActivity
                        Intent resultIntent = new Intent(getApplicationContext(), NoticiaLinkActivity.class);
                        resultIntent.putExtra("url", url);

                        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Notifiquem la nova noticia amb les dades seleccionades d'abans
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.escut)
                                .setContentTitle(title)
                                .setLargeIcon(thumbnail)
                                .setContentText(text)
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_MAX)
                                .setDefaults(Notification.DEFAULT_ALL);

                        mBuilder.setContentIntent(resultPendingIntent);

                        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

                        // mId allows you to update the notification later on.
                        mNotificationManager.notify(0, mBuilder.build());

                   // }



                }
           // } catch (InterruptedException e) {
                // Restore interrupt status.
             //   Thread.currentThread().interrupt();
            //}
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

}