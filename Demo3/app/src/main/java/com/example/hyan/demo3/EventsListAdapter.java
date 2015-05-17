package com.example.hyan.demo3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.List;

public class EventsListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Event> list;
    public EventsListAdapter(Context c,List<Event> l){
        context = c;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = l;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Event getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = layoutInflater.inflate(R.layout.list_item_layout, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(getItem(position).title);
        SmartImageView image = (SmartImageView) convertView.findViewById(R.id.image);
        image.setImageUrl(getItem(position).imageurl);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        distance.setText(getItem(position).distance + "公里内");
        TextView tags = (TextView) convertView.findViewById(R.id.tags);
        tags.setText(getItem(position).tags);
        return convertView;
    }
}
