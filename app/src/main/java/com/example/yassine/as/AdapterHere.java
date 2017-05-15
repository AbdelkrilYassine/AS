package com.example.yassine.as;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yassine on 19/04/2017.
 */

public class AdapterHere extends BaseAdapter {
    ArrayList<Here> list = new ArrayList<Here>();
    Context activity;

    @Override
    public int getCount() {
        return list.size();

    }

    public AdapterHere(Context a, ArrayList<Here> p) {
        super();
        this.list = p;
        this.activity = a;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_simple, parent, false);

            TextView id = (TextView) rowView.findViewById(R.id.namef);
            id.setText(list.get(position).getName());

            TextView num = (TextView) rowView.findViewById(R.id.idf);
            num.setText(String.valueOf(position + 1));
            TextView pr = (TextView) rowView.findViewById(R.id.pr);
            if (list.get(position).getP().equals("Absent")) {
                pr.setTextColor(Color.parseColor("#FF0000"));

                pr.setText(list.get(position).getP());
            } else if (list.get(position).getP().equals("Present")) {
                pr.setTextColor(Color.parseColor("#008000"));
                pr.setText(list.get(position).getP());
            }

            ImageView imageView = (ImageView) rowView.findViewById(R.id.drawable);
            Context context = imageView.getContext();
            int id_image = context.getResources().getIdentifier(list.get(position).getImg(), "drawable", context.getPackageName());
            imageView.setImageResource(id_image);
            return rowView;
        }
    }
}