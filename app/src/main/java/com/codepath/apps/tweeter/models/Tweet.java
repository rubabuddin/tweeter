package com.codepath.apps.tweeter.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by rubab.uddin on 10/25/2016.
 */
//parse JSON, store data, encapsulate state logic or display logic
public class Tweet {

    private String body; //tweet content
    private long uid; //unique id for the tweet
    private User user; // store embedded User object
    private String createdAt;
    private long retweetCount;
    private long favoriteCount;

    public String getTimeAgo() { return getRelativeTimeAgo(getCreatedAt()); }

    public long getRetweetCount() { return retweetCount; }

    public long getFavoriteCount() { return favoriteCount; }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public User getUser(){ return user; }

    //Deserialize the JSON and build tweet object
    //Tweet.fromJSON("..") -> <Tweet>
    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();

        try{
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getLong("retweet_count");
            tweet.favoriteCount = jsonObject.getLong("favorite_count"); //where is the favourites count????
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return tweet object
        return tweet;
    }

    //Tweet.fromJSONArray => List<Tweet>
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i=0; i<jsonArray.length(); i++){
            try{
            JSONObject tweetJson = jsonArray.getJSONObject(i);
            Tweet tweet = Tweet.fromJSON(tweetJson);
            if(tweet != null)
                tweets.add(tweet);
            } catch (JSONException e){
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}

/*
		[
		  {
		    "coordinates": null,
		    "truncated": false,
		    "created_at": "Tue Aug 28 21:16:23 +0000 2012",
		    "favorited": false,
		    "id_str": "240558470661799936",
		    "in_reply_to_user_id_str": null,
		    "entities": {
		      "urls": [
		 
		      ],
		      "hashtags": [
		 
		      ],
		      "user_mentions": [
		 
		      ]
		    },
		    "text": "just another test",
		    "contributors": null,
		    "id": 240558470661799936,
		    "retweet_count": 0,
		    "in_reply_to_status_id_str": null,
		    "geo": null,
		    "retweeted": false,
		    "in_reply_to_user_id": null,
		    "place": null,
		    "source": "<a href="//realitytechnicians.com%5C%22" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
		    "user": {
		      "name": "OAuth Dancer",
		      "profile_sidebar_fill_color": "DDEEF6",
		      "profile_background_tile": true,
		      "profile_sidebar_border_color": "C0DEED",
		      "profile_image_url":"http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
		      "created_at": "Wed Mar 03 19:37:35 +0000 2010",
		      "location": "San Francisco, CA",
		      "follow_request_sent": false,
		      "id_str": "119476949",
		      "is_translator": false,
		      "profile_link_color": "0084B4",
		      "entities": {
		        "url": {
		          "urls": [
		            {
		              "expanded_url": null,
		              "url": "http://bit.ly/oauth-dancer",
		              "indices": [
		                0,
		                26
		              ],
		              "display_url": null
		            }
		          ]
		        },
		        "description": null
		      },
		      "default_profile": false,
		      "url": "http://bit.ly/oauth-dancer",
		      "contributors_enabled": false,
		      "favourites_count": 7,
		      "utc_offset": null,
		      "profile_image_url_https":"https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
		      "id": 119476949,
		      "listed_count": 1,
		      "profile_use_background_image": true,
		      "profile_text_color": "333333",
		      "followers_count": 28,
		      "lang": "en",
		      "protected": false,
		      "geo_enabled": true,
		      "notifications": false,
		      "description": "",
		      "profile_background_color": "C0DEED",
		      "verified": false,
		      "time_zone": null,
		      "profile_background_image_url_https":"https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
		      "statuses_count": 166,
		      "profile_background_image_url":"http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
		      "default_profile_image": false,
		      "friends_count": 14,
		      "following": false,
		      "show_all_inline_media": false,
		      "screen_name": "oauth_dancer"
		    },
		    "in_reply_to_screen_name": null,
		    "in_reply_to_status_id": null
		  },

 */