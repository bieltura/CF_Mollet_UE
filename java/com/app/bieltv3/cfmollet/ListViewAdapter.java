package com.app.bieltv3.cfmollet;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
 
    // Declare Variables
    private Context context;
    private String[] texts1;
    private String[] texts2;
    private String[] texts3;
    private Bitmap[] photos;
 
    public ListViewAdapter(Context context, String[] texts1, String[] texts2, String[] texts3, Bitmap[] photos) {
        this.context = context;
        this.texts1 = texts1;
        this.texts2 = texts2;
        this.texts3 = texts3;
        this.photos = photos;
    }
 
    @Override
    public int getCount() {
        return texts1.length;
    }
 
    @Override
    public Object getItem(int position) {
        return null;
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
 
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        
        // Locate the TextViews in listview_item.xml
        TextView txt1 = (TextView) itemView.findViewById(R.id.titul);
        TextView txt2 = (TextView) itemView.findViewById(R.id.data);
        TextView txt3 = (TextView) itemView.findViewById(R.id.entrada);
        
        // Locate the ImageView in listview_item.xml
        ImageView img = (ImageView) itemView.findViewById(R.id.foto);
 
        // Capture position and set to the TextViews
        txt1.setText(Html.fromHtml(texts1[position]), TextView.BufferType.SPANNABLE);
        txt2.setText(texts2[position]);
        SingleLineTransformationMethod noline = new SingleLineTransformationMethod();
        txt3.setText(noline.getTransformation(Html.fromHtml(texts3[position]).toString().replace((char) 65532, (char) 32).substring(0,70) + " [...]",itemView), TextView.BufferType.SPANNABLE);
 
        // Capture position and set to the ImageView
        img.setImageBitmap(photos[position]);
 
        return itemView;
    }
}