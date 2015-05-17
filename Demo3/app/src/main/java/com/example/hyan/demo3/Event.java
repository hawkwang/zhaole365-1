package com.example.hyan.demo3;

import android.location.Location;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hyan on 2015/3/22.
 */
public class Event {
    String url;
    String id;
    String userid;
    String groupid;
    String title;
    String description;
    String imageurl;
    String type;
    JSONArray category;
    String tags;
    String happentime;
    String eventdate;
    String eventtime;
    String location;
    String location_description;
    String areacode;
    JSONArray RSVP_userid;

    int distance;

    public Event(JSONArray arg0,
                 JSONArray arg1,
                 JSONArray arg2,
                 String arg3,
                 String arg4,
                 String arg5,
                 String arg6,
                 String arg7,
                 String arg8,
                 String arg9,
                 String arg10,
                 String arg11,
                 String arg12,
                 String arg13,
                 String arg14,
                 String arg15,
                 String arg16,
                 Location arg17) throws Exception {
        category = arg0;
        tags = "";
        for(int i = 0; i < arg1.length(); ++i)
            tags = tags + arg1.getString(i) + " ";
        RSVP_userid = arg2;
        location_description = arg3;
        url = arg4;
        id = arg5;
        userid = arg6;
        groupid = arg7;
        title = arg8;
        description = replaceBlank(arg9);
        imageurl = arg10;
        type = arg11;
        happentime = arg12;
        eventdate = arg13;
        eventtime = arg14;
        location = arg15;
        areacode = arg16;
        distance = getDistance(arg15, arg17);
    }

    public int getDistance(String str, Location loc){

        LatLng p1 = new LatLng(Double.parseDouble(str.split(",")[0]),Double.parseDouble(str.split(",")[1]));
        LatLng p2 = new LatLng(loc.getLatitude(),loc.getLongitude());
        DistanceUtil. getDistance(p1, p2);
        return (int)DistanceUtil. getDistance(p1, p2)/1000 ;
    }

    public String replaceBlank(String str) {
        String result = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            result = m.replaceAll("");
        }
        return result;
    }
}
