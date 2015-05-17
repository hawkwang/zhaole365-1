package com.example.hyan.demo3;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by hyan on 2015/4/22.
 */
public class Group {
    String id;
    String userid;
    String title;
    String description;
    String type;
    JSONArray category;
    String tags = "";
    String ts_created;
    String location;
    JSONArray location_description;
    String areacode;
    String imageurl;
    JSONArray member_userid;
    public Group(String arg0,
                      String arg1,
                      String arg2,
                      String arg3,
                      String arg4,
                      JSONArray arg5,
                      JSONArray arg6,
                      String arg7,
                      String arg8,
                      JSONArray arg9,
                      String arg10,
                      String arg11,
                      JSONArray arg12) throws JSONException {
        id = arg0;
        userid = arg1;
        title = arg2;
        description = arg3;
        type = arg4;
        category = arg5;
        for(int i = 0; i < arg6.length(); ++i)
        tags = tags + arg6.getString(i) + " ";
        ts_created = arg7;
        location = arg8;
        location_description = arg9;
        areacode = arg10;
        imageurl = arg11;
        member_userid = arg12;

    }

}
