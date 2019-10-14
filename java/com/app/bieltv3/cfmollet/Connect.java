package com.app.bieltv3.cfmollet;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Connect {

    public static String getHtml(String URL) {
        try {
            URL url = new URL(URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            return sb.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getModifiedDate(String URL) {
        URL url = null;
        long time = 0;
        try {
            url = new URL(URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //URLConnection connection = url.openConnection();
            urlConnection.connect();
            time = urlConnection.getLastModified();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String getStringFromJSONArray(JSONArray array, int index, String value) {
    	String string = "";
		try {
			string = array.getJSONObject(index).getString(value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return string;
    }

    public static Bitmap getImage(String URL) throws FileNotFoundException {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new URL(URL).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    public static int getLength(HttpURLConnection connection) {
        return connection.getContentLength();
    }
}