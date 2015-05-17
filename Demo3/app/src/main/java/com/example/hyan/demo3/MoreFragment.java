package com.example.hyan.demo3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hyan on 2015/4/19.
 */
public class MoreFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.more_fragment, null);
        TextView createEvent = (TextView) view.findViewById(R.id.create_event);
        TextView createGroup = (TextView) view.findViewById(R.id.create_group);
        TextView info = (TextView) view.findViewById(R.id.info);
        TextView website = (TextView) view.findViewById(R.id.zhaole_url);

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(),CreateEventActivity.class);
                startActivity(intent);*/
                Uri uri = Uri.parse("http://www.zhaole365.com/zlevent/add");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(),CreateGroupActivity.class);
                startActivity(intent);*/
                Uri uri = Uri.parse("http://www.zhaole365.com/zlevent/add");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),InfoActivity.class);
                startActivity(intent);
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.zhaole365.com/");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
        return view;
    }
}