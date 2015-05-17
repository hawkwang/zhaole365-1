package com.example.hyan.demo3;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupsFragmentTask extends AsyncTask<String,Fragment,List<Group>> {
    GroupsFragment groupsFragment;
    ListView listView;
    GroupsListAdapter adapter;
    Location coords;
    public GroupsFragmentTask(final GroupsFragment fragment, View view, Location loc) {
        groupsFragment = fragment;
        listView =(ListView) view.findViewById(R.id.ListView);
        coords = loc;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(groupsFragment.getActivity(), GroupActivity.class);
                Group group = adapter.getItem(position);
                intent.putExtra("title",group.title);
                intent.putExtra("coords",coords);
                intent.putExtra("description", group.description);
                intent.putExtra("id", group.id);
                intent.putExtra("tags", group.tags);
                intent.putExtra("imageurl", group.imageurl);
                groupsFragment.startActivity(intent);
            }
        });
    }

    @Override
    protected List<Group> doInBackground(String... params) {
        String string;
        List<Group> list = new ArrayList<>();
        try {
            string = getString("http://www.zhaole365.com:8983/solr/groups/select?q=tags%3A*&wt=json&indent=true");
            JSONObject data = new JSONObject(string);
            JSONObject response = data.getJSONObject("response");
            JSONArray docs = response.getJSONArray("docs");
            JSONObject info;

            for(int i = 0; i < docs.length(); ++i) {
                info = docs.getJSONObject(i);
                Group group = new Group(
                        info.getString("id"),
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
                        info.getJSONArray("member_userid")
                );
                list.add(group);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(list, new Comparator<Group>() {
            @Override
            public int compare(Group lhs, Group rhs) {
                return lhs.title.compareTo(rhs.title);
            }
        });
        return list;
    }

    @Override
    protected void onPostExecute(List<Group> list) {
        adapter = new GroupsListAdapter(groupsFragment.getActivity(),list);
        listView.setAdapter(adapter);

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
