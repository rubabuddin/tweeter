package com.codepath.apps.tweeter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.apps.tweeter.adapters.TweetsArrayAdapter;
import com.codepath.apps.tweeter.helpers.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweeter.helpers.Utils;
import com.codepath.apps.tweeter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private List<Tweet> tweets = new ArrayList<Tweet>();

    RecyclerView rvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        client = TwitterApplication.getRestClient(); //used for all endpoints across the app

        initializeTimeline();
    }

    private void initializeTimeline() {
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        rvTweets.setHasFixedSize(true);

        aTweets = new TweetsArrayAdapter(this, tweets);
        rvTweets.setAdapter(aTweets);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager){
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        });

        if(!Utils.isOnline()) {
                //get offline tweets
        } else {
            populateTimeline(-1);
        }
    }

    public void loadNextDataFromApi(int offset) {
        if (offset > 14) {
            return; // limit to 15 calls
        }

        //Return tweets that have an id less than the specified id (get older tweets)
        long maxId = tweets.get(tweets.size() - 1).getUid() - 1;
        populateTimeline(maxId);
    }

    //Send an API request to get the timeline json
    //Fill the listview by creating the tweet objects from the json
    private void populateTimeline(final long maxId){
        client.getHomeTimeline(new JsonHttpResponseHandler(){
           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                   if (maxId == -1) {
                       int tweetsListSize = tweets.size();
                       tweets.clear();
                       aTweets.notifyItemRangeRemoved(0, tweetsListSize);
                       Log.d("DEBUG", aTweets.toString());
                   }
                   int curSize = tweets.size();
                   tweets.addAll(Tweet.fromJSONArray(json));
                   Log.d("DEBUG", tweets.toString());
                   //aTweets.notifyItemRangeInserted(curSize, tweets.size());
                    aTweets.notifyDataSetChanged();
                   Log.d("DEBUG", aTweets.toString());
           }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                Log.d("DEBUG", jsonObject.toString());
            }
        });
    }
}
