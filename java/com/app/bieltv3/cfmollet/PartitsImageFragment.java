package com.app.bieltv3.cfmollet;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PartitsImageFragment extends Fragment {

    // Variables del spinner
    Spinner spnEquips;
    String equipSeleccionat;

    // Variables per al link de fcf
    private String any, tipusFutbol, grup, competicio;

    // Variables del horari
    ImageView horariImg;
    String horari;

    // Barra de progres de carregar
    protected ProgressBar loading;

    List<String> nomEquips;

    private final String[][] equips = { //ESTAN FETS PREF, PRIMERA
            // NOM DEL EQUIP	TIPUS	    COMPETICIÓ						        GRUP
            { "1er EQUIP",		"11",	    "primera-catalana",				        "1" },
            { "JUVENIL A", 		"11",	    "lliga-nacional-juvenil", 	        	"7e" },
            { "JUVENIL B", 		"11",   	"juvenil-primera-divisio", 		        "7" },
            { "JUVENIL C", 		"11",	    "juvenil-primera-divisio", 	            "8" },
            { "CADET A", 		"11",   	"preferent-cadet", 			        	"2" },
            { "CADET B", 		"11",   	"preferent-cadet",	                  	"7" },
            { "CADET C", 		"11",   	"cadet-primera-divisio",	        	"4" },
            { "CADET D", 		"11",   	"cadet-segona-divisio", 	        	"40" },
            { "INFANTIL A", 	"11",   	"preferent-infantil", 		        	"2" },
            { "INFANTIL B", 	"11",   	"infantil-primera-divisio",         	"8" },
            { "INFANTIL C", 	"11",   	"infantil-primera-divisio",	        	"7" },
            { "INFANTIL D", 	"11",   	"infantil-primera-divisio",	        	"6" },
            { "INFANTIL E", 	"11",   	"infantil-segona-divisio", 	        	"52" },
            { "INFANTIL F", 	"11",   	"infantil-segona-divisio", 	        	"53" },

            { "ALEVÍ A", 		"7",	    "preferent-alevi", 	        	        "2" },
            { "ALEVÍ B", 		"7",    	"alevi-primera-divisio", 	        	"6" },
            { "ALEVÍ C", 		"7",	    "alevi-primera-divisio", 	        	"5" },
            { "ALEVÍ D", 		"7",    	"alevi-segona-divisio", 	        	"10" },
            { "ALEVÍ E", 		"7",    	"alevi-segona-divisio", 	        	"11" },
            { "ALEVÍ F", 		"7",    	"alevi-tercera-divisio", 	        	"45" },
            { "ALEVÍ G", 		"7",    	"alevi-tercera-divisio", 	        	"41" },
            { "ALEVÍ H", 		"7",    	"alevi-tercera-divisio", 	        	"40" },
            { "BENJAMÍ A", 		"7",    	"preferent-benjami", 		        	"2" },
            { "BENJAMÍ B", 		"7",    	"benjami-7-primera-divisio",        	"8" },
            { "BENJAMÍ C",		"7",    	"benjami-7-primera-divisio",        	"7" },
            { "BENJAMÍ D", 		"7",    	"benjami-7-segona-divisio",         	"9" },
            { "BENJAMÍ E", 		"7",    	"benjami-7-segona-divisio",         	"10" },
            { "BENJAMÍ F", 		"7",    	"benjami-7-tercera-divisio",        	"44" },
            { "BENJAMÍ G", 		"7",    	"benjami-7-tercera-divisio",        	"41" },
            { "BENJAMÍ H", 		"7",    	"benjami-7-tercera-divisio",        	"43" },
            { "PRE BENJAMÍ A", 	"7",    	"prebenjami-7", 			        	"34" },
            { "PRE BENJAMÍ B", 	"7",    	"prebenjami-7", 			        	"37" },
            { "PRE BENJAMÍ C", 	"7",    	"prebenjami-7", 			        	"38" },
            { "PRE BENJAMÍ D", 	"7",    	"prebenjami-7", 			        	"39" },

            { "1er EQUIP FEMENÍ",   "femeni",   "segona-divisio-femeni",                            "3"},
            { "INFANTIL/ALEVÍ",     "femeni",   "segona-diviso-femeni-infantil-alevi",              "4"},
            { "BENJAMÍ/PREBENJAMÍ", "femeni",   "primera-divisio-femeni-alevi-benjami-prebenjami",   "1"}
    };

    // Constructor buit necesari en els Fragments
    public PartitsImageFragment(){}

    // Creem un metode per a insanciar el fragment i passar els valors per parametres
    public static PartitsImageFragment newInstance(String any, String horari) {
        PartitsImageFragment partitsFragment = new PartitsImageFragment();

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
        any = getArguments().getString("any");
        nomEquips = new ArrayList<>();
        horari = getArguments().getString("horari");

        // Si un equip te de tipus igual que el nom de la pestanya ñ'afegim a la llista
        for (String[] e : equips) {
                nomEquips.add(e[0]);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Layout del Fragment
        View view = inflater.inflate(R.layout.fragment_partits_imatge, container, false);
        spnEquips = (Spinner) view.findViewById(R.id.equipsSpinner);

        // Esborrem si hi havia llista antiga
        spnEquips.setSelection(SpinnerAdapter.NO_SELECTION);

        // Creem de nou la llista de equips
        SpinnerAdapter equipsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nomEquips);
        spnEquips.setAdapter(equipsAdapter);

        // Imatge gran del horari:
        horariImg  = (ImageView) view.findViewById(R.id.imgHorari);

        // Barra de progress
        loading = (ProgressBar) view.findViewById(R.id.progessImg);

        // Botó de la fcf
        FloatingActionButton fcf = (FloatingActionButton) view.findViewById(R.id.fcf);

        spnEquips.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                equipSeleccionat = nomEquips.get(position);

                // Repasem tots els equips
                for (String[] e : equips) {

                    // Si el seleccionat coincideix amb algun de la llista
                    if (e[0].equalsIgnoreCase(equipSeleccionat)) {

                        // Guardem els seus valors de la taula
                        tipusFutbol = e[1];
                        competicio = e[2];
                        grup = e[3];
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }
        });

        fcf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getContext(), FCFTabActivity.class);
                i.putExtra("any", any);
                i.putExtra("tipusFutbol", tipusFutbol);
                i.putExtra("competicio", competicio);
                i.putExtra("grup", grup);

                startActivity(i);
            }
        });

        // Carregue la imatge de portada
        Load imageTask = new Load();
        imageTask.execute(getImgURL());

        return view;
    }

    protected String getImgURL() {

        return horari.substring(horari.lastIndexOf("http://"), horari.lastIndexOf(".png") + ".png".length());
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

    private class Load extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            // Retornem la imatge que anirà adalt de la noticia
            return descarregarImatge(getImgURL());
        }

        protected void onPostExecute(Bitmap result) {
            // Deixem de mostrar l'icona de carregar
            loading.setVisibility(View.INVISIBLE);

            // Afegim el text i la imatge
            horariImg.setImageBitmap(result);

        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
