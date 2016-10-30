package com.codepath.apps.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by rubab.uddin on 10/25/2016.
 */
@Parcel
public class User {
    //list attributes
    String profileName;
    String userName;
    long uid;
    String profileImageUrl;

    // empty constructor needed by the Parceler library
    public User() {
    }

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

