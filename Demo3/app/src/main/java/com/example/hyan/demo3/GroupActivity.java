package com.example.hyan.demo3;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class GroupActivity extends ActionBarActivity {
    ActionBar actionBar;
    ListView listView;
    EventsListAdapter adapter;
    Context context;
    String id;
    Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        context = this;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("详情");
        Drawable drawable = this.getResources().getDrawable(R.drawable.action_bar_background);
        actionBar.setBackgroundDrawable(drawable);
        id = getIntent().getExtras().getString("id");
        location = (Location) getIntent().getExtras().get("coords");

        TextView groupName = (TextView) findViewById(R.id.groupName);
        groupName.setText(getIntent().getExtras().getString("title"));

        TextView groupTags = (TextView) findViewById(R.id.groupTags);
        groupTags.setText(getIntent().getExtras().getString("tags"));

        TextView groupDesc = (TextView) findViewById(R.id.groupDesc);
        groupDesc.setText(getIntent().getExtras().getString("description"));

        listView = (ListView) findViewById(R.id.groupEvents);
        Task task = new Task();
        task.execute();
    }

    class Task extends AsyncTask<String, String, List<Event>>{

        @Override
        protected List<Event> doInBackground(String... params){
            List<Event> list = null;
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

            try {
                String string = getString("http://www.zhaole365.com:8983/solr/events/select?q=groupid%3A" +
                        id +
                        "&fq=eventdate%3A%5B" +
                        dateFormat.format(date) +
                        "+TO+*%5D&sort=eventdate+asc&wt=json&indent=true");
                list = getEvents(string);

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
            adapter = new EventsListAdapter(context,events);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
