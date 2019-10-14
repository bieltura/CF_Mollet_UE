package com.app.bieltv3.cfmollet;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EquipsFragment extends Fragment {
    Spinner spnEquips;

    String equipSeleccionat;

    // Variables per al link de fcf
    private String any, tipusFutbol, grup, competicio;

    // 6 dades: dia, hora, local, visitant, resultat, grup
    String[] dades = new String[6];

    // 5 dades: dia i hora, loca, visitant, resultat, grup
    TextView[] txtDades = new TextView[dades.length-1];

    // Escuts
    ImageView[] escuts = new ImageView[2];

    String horari;

    List<String> nomEquips;

    private final String[][] equips = { //ESTAN FETS PREF, PRIMERA
            // NOM DEL EQUIP	TIPUS	    COMPETICIÓ						        GRUP
            { "1er EQUIP",		"11",	    "primera-catalana",				        "1" },
            { "JUVENIL A", 		"11",	    "lliga-nacional-juvenil", 	        	"7e" },
            { "JUVENIL B", 		"11",   	"juvenil-primera-divisio", 		        "7" },
            { "JUVENIL C", 		"11",	    "juvenil-primera-divisio", 	            "8" },
            { "JUVENIL D", 		"11",	    "juvenil-segona-divisio", 	            "30" },
            { "CADET A", 		"11",   	"preferent-cadet", 			        	"2" },
            { "CADET B", 		"11",   	"preferent-cadet",	                  	"3" },
            { "CADET C", 		"11",   	"cadet-primera-divisio",	        	"7" },
            { "CADET D", 		"11",   	"cadet-segona-divisio", 	        	"32" },
            { "INFANTIL A", 	"11",   	"preferent-infantil", 		        	"2" },
            { "INFANTIL B", 	"11",   	"infantil-primera-divisio",         	"7" },
            { "INFANTIL C", 	"11",   	"infantil-primera-divisio",	        	"4" },
            { "INFANTIL D", 	"11",   	"infantil-primera-divisio",	        	"3" },
            { "INFANTIL E", 	"11",   	"infantil-segona-divisio", 	        	"42" },
            { "INFANTIL F", 	"11",   	"infantil-segona-divisio", 	        	"45" },

            { "ALEVÍ A", 		"7",	    "preferent-alevi", 	        	        "2" },
            { "ALEVÍ B", 		"7",    	"alevi-primera-divisio", 	        	"4" },
            { "ALEVÍ C", 		"7",	    "alevi-primera-divisio", 	        	"6" },
            { "ALEVÍ D", 		"7",    	"alevi-segona-divisio", 	        	"9" },
            { "ALEVÍ E", 		"7",    	"alevi-segona-divisio", 	        	"10" },
            { "ALEVÍ F", 		"7",    	"alevi-tercera-divisio", 	        	"43" },
            { "ALEVÍ G", 		"7",    	"alevi-tercera-divisio", 	        	"45" },
            { "ALEVÍ H", 		"7",    	"alevi-tercera-divisio", 	        	"41" },
            { "ALEVÍ I", 		"7",    	"alevi-tercera-divisio", 	        	"39" },
            { "BENJAMÍ A", 		"7",    	"preferent-benjami", 		        	"2" },
            { "BENJAMÍ B", 		"7",    	"benjami-7-primera-divisio",        	"8" },
            { "BENJAMÍ C",		"7",    	"benjami-7-primera-divisio",        	"7" },
            { "BENJAMÍ D", 		"7",    	"benjami-7-segona-divisio",         	"16" },
            { "BENJAMÍ E", 		"7",    	"benjami-7-segona-divisio",         	"14" },
            { "BENJAMÍ F", 		"7",    	"benjami-7-tercera-divisio",        	"37" },
            { "BENJAMÍ G", 		"7",    	"benjami-7-tercera-divisio",        	"36" },
            { "PRE BENJAMÍ A", 	"7",    	"prebenjami-7", 			        	"36" },
            { "PRE BENJAMÍ B", 	"7",    	"prebenjami-7", 			        	"37" },
            { "PRE BENJAMÍ C", 	"7",    	"prebenjami-7", 			        	"38" },
            { "PRE BENJAMÍ D", 	"7",    	"prebenjami-7", 			        	"39" },
            { "PRE BENJAMÍ E", 	"7",    	"prebenjami-7", 			        	"41" },

            { "1er EQUIP FEMENÍ",   "femeni",   "segona-divisio-femeni",                            "3"},
            { "INFANTIL/ALEVÍ",     "femeni",   "segona-diviso-femeni-infantil-alevi",              "4"},
            { "BENJAMÍ/PREBENJAMÍ", "femeni",   "segona-divisio-femeni-alevi-benjami-prebenjami",   "2"}
    };

    public static EquipsFragment newInstance(String any, String tipus, String horari) {
        EquipsFragment equipsFragment = new EquipsFragment();

        // Afegim les variables que volem utilitzar
        Bundle args = new Bundle();

        // Passem el any, el tipus i l'horari
        args.putString("any", any);
        args.putString("tipus", tipus);
        args.putString("horari", horari);

        // Enviem les dadesz en els arguments
        equipsFragment.setArguments(args);

        return equipsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicialitzem les variables
        any = getArguments().getString("any");
        tipusFutbol = getArguments().getString("tipus");
        nomEquips = new ArrayList<>();
        horari = getArguments().getString("horari");

        // Si un equip te de tipus igual que el nom de la pestanya ñ'afegim a la llista
        for (String[] e : equips) {
            if (e[1].equalsIgnoreCase(tipusFutbol))
                nomEquips.add(e[0]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Layout del Fragment
        View view = inflater.inflate(R.layout.fragment_equips, container, false);
        spnEquips = (Spinner) view.findViewById(R.id.equipsSpinner);

        // Esborrem si hi havia llista antiga
        spnEquips.setSelection(SpinnerAdapter.NO_SELECTION);

        // Creem de nou la llista de equips
        SpinnerAdapter equipsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nomEquips);
        spnEquips.setAdapter(equipsAdapter);

        // Text de dia, hora, local, visitant, resultat
        txtDades[0] 	= (TextView) view.findViewById(R.id.diaHoraTxt);
        txtDades[1] 	= (TextView) view.findViewById(R.id.localTxt);
        txtDades[2] 	= (TextView) view.findViewById(R.id.visitantTxt);
        txtDades[3] 	= (TextView) view.findViewById(R.id.resultatTxt);
        txtDades[4]     = (TextView) view.findViewById(R.id.grupTxt);

        // Escuts, local i vistant
        escuts[0]       = (ImageView) view.findViewById(R.id.imgLocal);
        escuts[1]       = (ImageView) view.findViewById(R.id.imgVisitant);

        // Botó de la fcf
        FloatingActionButton fcf = (FloatingActionButton) view.findViewById(R.id.fcf);

        // Botó de navegació
        FloatingActionButton map = (FloatingActionButton) view.findViewById(R.id.map);

        spnEquips.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                equipSeleccionat = nomEquips.get(position);

                // Repasem tots els equips
                for (String[] e : equips) {

                    // Si el seleccionat coincideix amb algun de la llista
                    if (e[0] .equalsIgnoreCase(equipSeleccionat)) {

                        // Guardem els seus valors de la taula
                        competicio = e[2];
                        grup = e[3];

                        carregaHorari(position);
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

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Agafem el valor de local i tornem al seu valor inicial (sense punts i apart)
                String local = dades[2].replace("<br>", " ").replace("</br>","");

                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps/?daddr=CAMP+FUTBOL+" + local);

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);

                // Make the Intent explicit by setting the Google Maps package
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                // Attempt to start an activity that can handle the Intent
                startActivity(intent);

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void carregaHorari(int equip) {

        // Agafem on comença la taula del equip slecionat i sumem els valors del nom del equip i la negreta amb HTML
        int start = horari.indexOf(nomEquips.get(equip)) + (nomEquips.get(equip) + "</strong></td>").length();

        // Final del text dels valors del equip
        int end;

        // Si no es l'ultim
        if (!nomEquips.get(equip).equalsIgnoreCase(nomEquips.get(nomEquips.size()-1))) {
            // Agafem on acaba (trovant on comença el següent equip) - equip + 1
            end = horari.indexOf(nomEquips.get(equip+1));
        }
        else {
            end = horari.lastIndexOf("</table>");
        }

        // Guardem la taula en un String
        String taulaPerEquip = horari.substring(start, end);

        // 5 different dades: Data, hora, local, resultat, visitant (la ultima a mà)
        for (int i = 0; i < dades.length-1; i++) {
            // Agafem la paraula passat el - px;">
            int dadaStart = taulaPerEquip.indexOf(">") + ">".length();

            // La tallem fins que apareix el </td> = nova columna
            int dadaEnd = taulaPerEquip.indexOf("</td>");

            dades[i] = taulaPerEquip.substring(dadaStart, dadaEnd).toUpperCase();

            // Per cada nom de un equip, retallem
            if (i == 2 || i == 3) {

                // Punts i comes
                dades[i] = dades[i].replace(",", "").replace(".", "")
                        .replace("\"","").replace("”","").replace("“","")

                        // Lletres de equips
                        .replace("AEC ", "").replace(" AEC", "")
                        .replace("RCD ", "").replace(" RCD", "")
                        .replace("UE ", "").replace(" UE", "")
                        .replace("UD ", "").replace(" UD", "")
                        .replace("CF ", "").replace(" CF", "")
                        .replace("CD ", "").replace(" CD", "")
                        .replace("CP ", "").replace(" CP", "")
                        .replace("FC ", "").replace(" FC", "")
                        .replace("CE ", "").replace(" EC", "")
                        .replace("EC ", "").replace(" EC", "")
                        .replace("EF ", "").replace(" EF", "")
                        .replace("EFB ", "").replace(" EFB", "")
                        .replace("CFU ", "").replace(" CFU", "");
                try {
                    // Si té una lletra suelta A - B - C ... La Treiem
                    if ((dades[i].length() - dades[i].substring(0, dades[i].lastIndexOf(" ")).length()) <= 2)
                        dades[i] = dades[i].substring(0, dades[i].lastIndexOf(" "));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Saltem a l'altre linia de la taula, retallant passat el </td> (5 caracters mes) fins al final
            taulaPerEquip = taulaPerEquip.substring(dadaEnd + "</td>".length(), taulaPerEquip.length());
        }

        // Descansa => no té resultat ni (rival o equip)
        if (dades[4].equals("") && (dades[3].equals("") || dades[2].equals(""))) {
            dades[4] = "X";
            dades[0] = "Descansa";
        }

        // Si alguna de la dada és major a 12 caràcters, fem dos linies si té espais
        for (int i = 0; i < dades.length-1; i++) {
            if (dades[i].length() > 12 && dades[i].contains(" ")) {
                // Trobem on està l'ultim espai en blanc
                int indexLastSpace = dades[i].lastIndexOf(" ");

                // Substituïm aquest espai per un intro HTML i li afegim al final
                dades[i] = new StringBuilder(dades[i]).replace(indexLastSpace, indexLastSpace + 1,"<br>").toString() + "</br>";
            }
        }

        // La dada 5 és el grup: No li apliquem el retall
        dades[5] = competicio.substring(0,1).toUpperCase() + competicio.substring(1);
        dades[5] = dades[5].replace("-"," ") + "\r\n<br>Grup: " + grup + "</br>";

        mostraDades();
    }

    private void mostraDades() {

        // Dia i hora en un mateix txtView
        if (!dades[0].equalsIgnoreCase("Descansa"))
            txtDades[0].setText(dades[0] + " - " + dades[1]);
        else
            txtDades[0].setText(dades[0]);

        // Si som locals o visitants:
        if (dades[2].equalsIgnoreCase("MOLLET")) {              // Si el mollet és local
            escuts[0].setImageResource(R.drawable.escut);
            escuts[1].setImageResource(R.drawable.visitant);
        } else if (dades[3].equalsIgnoreCase("MOLLET")) {       // Si el mollet és visitant
            escuts[0].setImageResource(R.drawable.visitant);
            escuts[1].setImageResource(R.drawable.escut);
        } else {
            for (ImageView escut : escuts)
                escut.setImageResource(0);
        }

        // Afegim totes les dades restants
        for (int i = 1; i < txtDades.length; i++) {

            // fromHtml pel el salt de línea <br>
            txtDades[i].setText(Html.fromHtml(dades[i+1]));
        }
    }

}