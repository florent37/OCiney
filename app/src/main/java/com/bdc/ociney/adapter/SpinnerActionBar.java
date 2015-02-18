package com.bdc.ociney.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bdc.ociney.R;

/**
 * Created by devandroid on 23/04/14.
 */
public class SpinnerActionBar extends ArrayAdapter<String> {

    // CUSTOM SPINNER ADAPTER
    private Context mContext;
    private String[] items;
    private String titre;

    public SpinnerActionBar(Context context, int textViewResourceId,
                            String[] objects, String titre) {
        super(context, textViewResourceId, objects);

        mContext = context;
        items = objects;
        this.titre = titre;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        DropDownViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.dropdown_title, parent, false);
            holder = new DropDownViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.textView_dropdown_title);
            convertView.setTag(holder);

        } else {
            holder = (DropDownViewHolder) convertView.getTag();
        }

        // Should have some sort of data set to go off of, we'll assume
        // there is a some array called mData.
        holder.mTitle.setText(items[position]);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.actionbar_spinner, null);
            holder = new ViewHolder();
            holder.txt01 = (TextView) convertView.findViewById(R.id.TextView01);
            holder.txt02 = (TextView) convertView.findViewById(R.id.TextView02);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt01.setText(items[position]);
        holder.txt02.setText(titre);

        return convertView;
    }

    public class DropDownViewHolder {
        TextView mTitle;
    }

    class ViewHolder {
        TextView txt01;
        TextView txt02;
    }


}