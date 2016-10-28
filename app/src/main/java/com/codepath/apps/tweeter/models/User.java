package com.codepath.apps.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rubab.uddin on 10/25/2016.
 */

public class User {
    //list attributes
    private String profileName;
    private String userName;
    private long uid;
    private String profileImageUrl;


    public long getUid() {
        return uid;
    }

    //Kevin Durant
    public String getProfileName() {
        return profileName;
    }

    //@kdtrey5
    public String getUserName() {
        String mUserName = "@" + userName;
        return mUserName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    //deserialize the user JSON => USer
    public static User fromJSON(JSONObject json) {
        User u = new User();
        //Extract and fill the values
        try {
            u.userName = json.getString("screen_name");
            u.profileName = json.getString("name");
            u.uid = json.getLong("id");
            u.profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Return a user
        return u;
    }


}

