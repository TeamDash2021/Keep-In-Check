package com.trystar.keepincheck.Attendance;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trystar.keepincheck.R;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<MyItem> {

    private final ArrayList<MyItem> items;

    public MyListAdapter(Context context, ArrayList<MyItem> items) {
        super(context, 0, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_list_item, parent, false);
        }

        MyItem item = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        titleTextView.setText(item.getTitle());


        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(item.getDescription());
        if(item.getDescription().equals("Absent"))
        {
            descriptionTextView.setTextColor( Color.RED );
        }
        else if (item.getDescription().equals("Present")){
            descriptionTextView.setTextColor( Color.GREEN );
        }
        if (position==0){
            descriptionTextView.setTextColor( Color.BLACK );
            descriptionTextView.setTypeface(null, Typeface.BOLD);
        }

        return convertView;
    }
}

