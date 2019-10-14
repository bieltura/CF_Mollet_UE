package com.app.bieltv3.cfmollet;

import android.os.Bundle;

public class FCFFragment extends WebFragment {

    public static FCFFragment newInstance(String url) {
        FCFFragment fragment = new FCFFragment();

        // Afegim les variables necesaries
        Bundle args = new Bundle();

        args.putString("url", url);
        fragment.setArguments(args);

        return fragment;
    }

    protected String formatURL(String fcf) {
        // Eliminem la barra de adalt i cookies
        fcf = fcf.substring(0, fcf.indexOf("<body")) + fcf.substring(fcf.indexOf("<main class=\"page-row page-row-expanded\">"));

        // Provem si existeix publicitat i barra
        try {
            // Eliminem la publicitat
            fcf = fcf.substring(0, fcf.indexOf("<div class=\"col-md-12 grey bg-white mt-70 mb-10 d-n_ml\">")) + fcf.substring(fcf.indexOf("<div class=\"col-md-12 p-0\">"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Eliminem barra menu
            fcf = fcf.substring(0, fcf.indexOf("<div class=\"tabs-llista-competicio\">")) + fcf.substring(fcf.indexOf("<div class=\"widjet-body"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Eliminem Estrella damm panel inferior
        fcf = fcf.substring(0, fcf.indexOf("<footer class=\"page-row copyright\">")) + fcf.substring(fcf.lastIndexOf("</footer>"));

        // Eliminem el marge superior
        fcf = fcf.replace("mt-20_ml", "mt-0_ml");
        fcf = fcf.replace("130px", "0px");
        //fcf = fcf.replace("row bg-competicio","widjet-body");

        return fcf;
    }
}