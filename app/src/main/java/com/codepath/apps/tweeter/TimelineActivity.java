package com.codepath.apps.tweeter;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.apps.tweeter.adapters.TweetsArrayAdapter;
import com.codepath.apps.tweeter.fragments.ComposeTweetDialogFragment;
import com.codepath.apps.tweeter.helpers.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweeter.helpers.Utils;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogListener {

    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private List<Tweet> tweets = new ArrayList<Tweet>();
    private User authenticatedUser;

    @BindView(R.id.fabComposeTweet) FloatingActionButton fabComposeTweet;
    @BindView(R.id.rvTweets) RecyclerView rvTweets;
    @BindView(R.id.toolbar) Toolbar toolbar;
    //@BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        client = TwitterApplication.getRestClient(); //used for all endpoints across the app

        setAuthenticatedUser();
        initializeTimeline();
    }

    private void initializeTimeline() {
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

    @OnClick(R.id.fabComposeTweet)
    public void composeTweet() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment composeTweetDialogFragment = ComposeTweetDialogFragment.newInstance(authenticatedUser);
        composeTweetDialogFragment.show(fm, "fragment_compose");
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
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler(){
           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                   if (maxId == -1) {
                       int tweetsListSize = tweets.size();
                       tweets.clear();
                       aTweets.notifyItemRangeRemoved(0, tweetsListSize);
                       Log.d("DEBUG", aTweets.toString());
                   }
                   //int curSize = tweets.size();
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

    public void onUpdateStatusSuccess(Tweet newTweet) {
        Log.d("DEBUG", "New tweet composed: " + newTweet.getBody());
        if (newTweet != null) {
            //add to top of timeline
            tweets.add(0, newTweet);
            aTweets.notifyItemInserted(0);
            //scroll to top after tweet post
            rvTweets.scrollToPosition(0);
        }
    }

    public void onTweetComposed(String tweetText) {
        client.postStatus(tweetText, -1, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject jsonObject) {
                tweets.add(Tweet.fromJSON(jsonObject));
                aTweets.notifyItemInserted(0);
                //aTweets.notifyDataSetChanged();
                //scroll to top after tweet post
                rvTweets.smoothScrollToPosition(0);
            }
            public void onFailure(Throwable e, String str) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }

    private void setAuthenticatedUser() {
        Log.d("DEBUG", "Fetching user");
        client.getUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                authenticatedUser = User.fromJSON(response);
                if (authenticatedUser == null) {
                    Log.e("ERROR", "User =NULL ");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                Log.d("DEBUG", jsonObject.toString());
            }
        });
    }
}
