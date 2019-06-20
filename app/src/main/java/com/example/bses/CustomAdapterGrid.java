package com.example.bses;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterGrid extends BaseAdapter {

    private Context mContext;
    private String[] data;
    private Integer[] icon;
    LayoutInflater inflater;

    public CustomAdapterGrid(Context c, Integer[] icon, String[] data) {
        this.mContext = c;
        this.data = data;
        this.icon = icon;
        inflater = (LayoutInflater.from(c));
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.gridview_layout, null); // inflate the layout
        ImageView icons = view.findViewById(R.id.icon); // get the reference of ImageView
        TextView name = view.findViewById(R.id.name);
        icons.setImageResource(icon[i]); // set logo images
        name.setText(data[i]);
        return view;
    }

}
