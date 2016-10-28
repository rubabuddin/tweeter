package com.codepath.apps.tweeter.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.models.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.CamcorderProfile.get;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by rubab.uddin on 10/27/2016.
 */
//Takes tweet object, turns it into a view to be displayed in the list
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder>{

    private List<Tweet> tweets;
    private Context context;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets){
        this.tweets = tweets;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public TweetsArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetsArrayAdapter.ViewHolder viewHolder, int position) {
        Tweet tweet = tweets.get(position);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaNeue-Regular.ttf");

        //find subviews to fill with data in the template
        viewHolder.tvUserName.setText(tweet.getUser().getUserName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvBody.setTypeface(tf);
        viewHolder.tvProfileName.setText(tweet.getUser().getProfileName());
        viewHolder.tvProfileName.setTypeface(tf);
        viewHolder.tvTime.setText(tweet.getTimeAgo());
        viewHolder.tvTime.setTypeface(tf);
        viewHolder.tvRetweet.setText(String.valueOf(tweet.getRetweetCount()));
        viewHolder.tvRetweet.setTypeface(tf);
        viewHolder.tvFavorite.setText(String.valueOf(tweet.getFavoriteCount()));
        viewHolder.tvFavorite.setTypeface(tf);
        Glide.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .placeholder(R.drawable.twitter_user)
                .error(R.drawable.twitter_user)
                .fitCenter()
                .into(viewHolder.ivProfile);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        if (tweets != null) {
            return tweets.size();
        }
        return 0;
    }
}
