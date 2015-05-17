package com.example.hyan.demo3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.List;

/**
 * Created by hyan on 2015/4/22.
 */
public class GroupsListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Group> groups;

    public  GroupsListAdapter(Context c, List<Group> list){
        context = c;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        groups = list;
    }


    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Group getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        if(convertView == null)
            convertView = layoutInflater.inflate(R.layout.group_item_layout, null);
        TextView name = (TextView) convertView.findViewById(R.id.group_title);
        name.setText(getItem(position).title);
        /*TextView introduction = (TextView) convertView.findViewById(R.id.distance);
        introduction.setText(getItem(position).description);*/
        SmartImageView image = (SmartImageView) convertView.findViewById(R.id.group_image);
        image.setImageUrl(getItem(position).imageurl);
        //TextView address = (TextView) convertView.findViewById(R.id.tags);
        //address.setText(getItem(position).tags);
        return convertView;
    }
}
