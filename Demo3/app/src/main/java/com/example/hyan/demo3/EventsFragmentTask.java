package com.example.hyan.demo3;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EventsFragmentTask extends AsyncTask<String,Fragment,List<Event>>{
    EventsFragment eventsFragment;
    ListView listView;
    ListAdapter adapter;
    Location location;
    String eventDate;
    String tags;
    String keyword;
    Boolean isFromSearch;
    public EventsFragmentTask(EventsFragment fragment,View view,Location currentLocation,Boolean type){
        super();
        listView = (ListView) view.findViewById(R.id.ListView);
        eventsFragment = fragment;
        location = currentLocation;
        isFromSearch = type;
    }
    public void setEventDate(String string){
        eventDate = string ;
    }
    public void setTags(String string){
        tags = string;
    }
    public void setKeyword(String string){
        keyword = string;
    }
    @Override
    protected List<Event> doInBackground(String... params) {
        List<Event> list = null;
        String str = null;
        try {
            if(isFromSearch)
                str = getFuzzyResult(keyword);
            else
                str = getRefinedResult(eventDate, tags);
            list = getEvents(str);
            Collections.sort(list, new Comparator<Event>(){
                @Override
                public int compare(Event e1, Event e2) {
                    if(e1.distance - e2.distance < 0)
                        return -1;
                    else
                        return 1;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Event> list){
        adapter = new EventsListAdapter(eventsFragment.getActivity() ,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetail(position);
            }
        });
    }

    public String getRefinedResult(String arg0, String arg1) throws Exception {
        String path = "http://www.zhaole365.com:8983/solr/events/select?q=%2Beventdate%3A"
                + arg0 + "+%2Btags%3A" + URLEncoder.encode(arg1,"utf-8") + "&wt=json&indent=true";
        return getString(path);
    }

    public String getFuzzyResult(String arg) throws Exception {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String path = "http://www.zhaole365.com:8983/solr/events/select?q=title%3A"
        + URLEncoder.encode(arg,"utf-8") +
                "+%2Btags%3A"
                + URLEncoder.encode(arg,"utf-8") +
                "+&fq=eventdate%3A%5B"
        + dateFormat.format(date) +
                "+TO+*%5D&sort=eventdate+asc&wt=json&indent=true";
        return getString(path);
    }


    public String getString(String path) throws Exception {
        URL url = new URL(path);
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6 * 1000);
            InputStream inStream = conn.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inStream.close();
            byte[] data = outStream.toByteArray();
            return new String(data, "UTF-8");
        } catch (Exception e) {
        }
        return "{\"response\":{\"docs\":[]}}";
    }

    public List<Event> getEvents(String str) throws Exception {

        List<Event> events = new ArrayList<Event>();
        JSONObject data = new JSONObject(str);
        JSONObject response = data.getJSONObject("response");
        JSONArray docs = response.getJSONArray("docs");
        JSONObject info;
        for(int i = 0; i < docs.length(); ++i) {
            info = docs.getJSONObject(i);
            Event event = new Event(
                    info.getJSONArray("category"),
                    info.getJSONArray("tags"),
                    info.getJSONArray("RSVP_userid"),
                    //info.getJSONArray("location_description").get(0).toString() +"，" +
                            info.getJSONArray("location_description").get(1).toString(),
                    info.getString("url"),
                    info.getString("id"),
                    info.getString("userid"),
                    info.getString("groupid"),
                    info.getString("title"),
                    info.getString("description"),
                    info.getString("imageurl"),
                    info.getString("type"),
                    info.getString("happentime"),
                    info.getString("eventdate"),
                    info.getString("eventtime"),
                    info.getString("location"),
                    info.getString("areacode"),
                    location
                    );
            events.add(event);
        }
        return events;
    }
    public void openDetail(int position){
        Intent intent = new Intent(eventsFragment.getActivity(),DetailActivity.class);
        Event event = (Event) adapter.getItem(position);
        intent.putExtra("title",event.title);
        intent.putExtra("tags",event.tags);
        intent.putExtra("url",event.url);
        intent.putExtra("location",location);
        intent.putExtra("groupid",event.groupid);
        intent.putExtra("eventtime",event.eventtime);
        intent.putExtra("eventdate",event.eventdate);
        intent.putExtra("imageurl",event.imageurl);
        intent.putExtra("desc",event.description);
        intent.putExtra("place",event.location_description);
        eventsFragment.startActivity(intent);
    }
}

