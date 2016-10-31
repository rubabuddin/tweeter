package com.codepath.apps.tweeter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweeter.models.Tweet;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvBody)
    TextView tvBody;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvRetweet)
    TextView tvRetweet;
    @BindView(R.id.tvFavorite)
    TextView tvFavorite;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private TwitterClient twitterClient;
    private Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        twitterClient = TwitterApplication.getRestClient();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("details"));
        if (tweet != null) {
            initDetails();
        } else {
            Log.e("ERROR", "Tweet not found");
        }
    }

    private void initDetails() {
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaNeue-Regular.ttf");

        //find subviews to fill with data in the template
        tvUserName.setText(tweet.user.userName);

        tvBody.setText(tweet.body);
        tvBody.setTypeface(tf);
        tvBody.setMovementMethod(LinkMovementMethod.getInstance());

        tvProfileName.setText(tweet.user.profileName);
        tvProfileName.setTypeface(tf);

        tvTime.setText(tweet.createdAt);
        tvTime.setTypeface(tf);

        tvRetweet.setText(String.valueOf(tweet.retweetCount));
        tvRetweet.setTypeface(tf);

        tvFavorite.setText(String.valueOf(tweet.favoriteCount));
        tvFavorite.setTypeface(tf);

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .placeholder(R.drawable.twitter_user)
                .error(R.drawable.twitter_user)
                .fitCenter()
                .into(ivProfile);

    }
}
