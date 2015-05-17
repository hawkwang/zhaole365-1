package com.example.hyan.demo3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.image.SmartImageView;

import org.json.JSONArray;
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

public class DetailActivity extends ActionBarActivity {
    ActionBar actionBar;
    MapView mapView;
    BaiduMap baiduMap;
    Context context;
    String groupid;
    String path;
    ListView recommend;
    Location location;
    EventsListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_detail);
        context = this;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("详情");
        Drawable drawable = this.getResources().getDrawable(R.drawable.action_bar_background);
        actionBar.setBackgroundDrawable(drawable);

        location = (Location)getIntent().getExtras().get("location");
        groupid = getIntent().getExtras().getString("groupid");
        path = getIntent().getExtras().getString("url");
        SmartImageView myImage = (SmartImageView) this.findViewById(R.id.my_image);
        myImage.setImageUrl(getIntent().getExtras().getString("imageurl"));

        TextView title = (TextView) this.findViewById(R.id.event_title);
        title.setText(getIntent().getExtras().getString("title"));

        TextView tags = (TextView) this.findViewById(R.id.event_tags);
        tags.setText(getIntent().getExtras().getString("tags"));

        TextView date = (TextView) this.findViewById(R.id.event_date);
        date.setText("日期： " + getIntent().getExtras().getString("eventdate"));

        TextView time = (TextView) this.findViewById(R.id.event_time);
        time.setText("时间： " + getIntent().getExtras().getString("eventtime"));

        TextView location = (TextView) this.findViewById(R.id.event_loction);
        location.setText("地址： " + getIntent().getExtras().getString("place"));

        TextView desc = (TextView) this.findViewById(R.id.event_description);
        desc.setText("介绍： " + getIntent().getExtras().getString("desc"));

        TextView save = (TextView) this.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"已收藏",Toast.LENGTH_SHORT).show();
            }
        });

        TextView url = (TextView) this.findViewById(R.id.url);
        url.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(path);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        recommend = (ListView) findViewById(R.id.recommend);

        Task task = new Task();
        task.execute();

        Recommend recommendTask = new Recommend();
        recommendTask.execute();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setTitle("地图");
               // dialog.setContentView(R.layout.map_dialog);
               // MapView mapView = (MapView) dialog.findViewById(R.id.mapView);
                dialog.show();
            }
        });
        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setTitle("介绍");
                dialog.setContentView(R.layout.desc_dialog);
                TextView textView = (TextView) dialog.findViewById(R.id.dialog);
                textView.setText(getIntent().getExtras().getString("desc"));
                dialog.show();
            }
        });

    }

    class Recommend extends  AsyncTask<String, String, List<Event>>{

        @Override
        protected List<Event> doInBackground(String... params) {
            List<Event> list = null;
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String str = null;
            try {
                str = getString("http://www.zhaole365.com:8983/solr/events/select?q=tags%3A" +
                        URLEncoder.encode(getIntent().getExtras().getString("tags"), "utf-8") +
                        "&fq=eventdate%3A%5B" +
                        dateFormat.format(date) +
                        "+TO+*%5D&wt=json&indent=true");
                list = getEvents(str);
                Collections.sort(list, new Comparator<Event>() {
                    @Override
                    public int compare(Event e1, Event e2) {
                        if (e1.distance - e2.distance < 0)
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
        protected void onPostExecute(List<Event> events) {
            Log.e("number","" + events.size());
            adapter = new EventsListAdapter(context ,events);
            recommend.setAdapter(adapter);
            recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openDetail(position);
                }
            });
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
    }

    class Task extends AsyncTask<String, String, Group>{
        @Override
        protected Group doInBackground(String... params) {
            Group group = null;
            try {
                String string = getString("http://www.zhaole365.com:8983/solr/groups/select?q=id%3A" +
                        groupid + "&wt=json&indent=true");
                JSONObject data = new JSONObject(string);
                JSONObject response = data.getJSONObject("response");
                JSONArray docs = response.getJSONArray("docs");
                JSONObject info = docs.getJSONObject(0);
                group = new Group(info.getString("id"),
                        info.getString("userid"),
                        info.getString("title"),
                        info.getString("description"),
                        info.getString("type"),
                        info.getJSONArray("category"),
                        info.getJSONArray("tags"),
                        info.getString("ts_created"),
                        info.getString("location"),
                        info.getJSONArray("location_description"),
                        info.getString("areacode"),
                        info.getString("imageurl"),
                        info.getJSONArray("member_userid"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return group;
        }

        @Override
        protected void onPostExecute(Group group) {
            super.onPostExecute(group);
            TextView group_name = (TextView) findViewById(R.id.group_name);
            group_name.setText(group.title);
            TextView group_tags = (TextView) findViewById(R.id.group_tags);
            group_tags.setText(group.tags);
            SmartImageView group_icon = (SmartImageView) findViewById(R.id.group_icon);
            group_icon.setImageUrl(group.imageurl);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDetail(int position){
        Intent intent = new Intent(context,DetailActivity.class);
        Event event = adapter.getItem(position);
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
        startActivity(intent);
    }
}
