package com.codepath.apps.tweeter.helpers;

import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.codepath.apps.tweeter.models.Tweet;

import java.util.List;

/**
 * Created by rubab.uddin on 10/30/2016.
 */

public class SQLHelper {

    private static SQLHelper sqlHelper;

    public static SQLHelper getHelper() {
        if (sqlHelper == null) {
            sqlHelper = new SQLHelper();
        }
        return sqlHelper;
    }

    public List<Tweet> getOfflineTweets() {
        List<Tweet> tweets = new Select()
                .from(Tweet.class)
                .orderBy("TweetId ASC").execute();
        return tweets;
    }

    public void saveOfflineTweets(List<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            if (tweet.user != null) {
                tweet.user.save();
            }
            if (tweet.embeddedMedia != null) {
                tweet.embeddedMedia.save();
            }
            tweet.save();
        }
    }

    public void clearTweets() {
        SQLiteUtils.execSql("DELETE FROM Tweet");
    }
}
