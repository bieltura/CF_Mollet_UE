package com.app.bieltv3.cfmollet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ContactarFragment extends Fragment {

    TextView txtInfo;
    Button txtEmail, txtTlf;
    String info, email, tlf;

    public static ContactarFragment newInstance(int position) {
        ContactarFragment fragment = new ContactarFragment();

        // Afegim les variables necesaries
        Bundle args = new Bundle();

        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Rebem la informació
        int position = getArguments().getInt("position");

        info    = getResources().getStringArray(R.array.info_contactar)[position];
        email   = getResources().getStringArray(R.array.email_contactar)[position];
        tlf     = getResources().getStringArray(R.array.tlf_contactar)[position];

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contactar, container, false);

        txtInfo     = (TextView) view.findViewById(R.id.info_contactar);
        txtTlf      = (Button) view.findViewById(R.id.tlf_contactar);
        txtEmail    = (Button) view.findViewById(R.id.email_contactar);

        SpannableStringBuilder infoBuilder = new SpannableStringBuilder(info);
        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
        infoBuilder.setSpan(bold, 0, info.indexOf(":"), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        txtInfo.setText(Html.fromHtml(info));
        txtTlf.setText(tlf);
        txtEmail.setText(email);

        txtTlf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Truquen al nombre del botó
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+txtTlf.getText().toString()));
                startActivity(i);
            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sendEmail(txtEmail.getText().toString());
            }
        });

        return view;
    }

    public void sendEmail(String email) {
        // Enviem un email amb titul: desde la app
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email enviat desde l'app");
        startActivity(emailIntent);
    }
}