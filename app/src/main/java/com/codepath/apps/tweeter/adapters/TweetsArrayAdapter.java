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

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by rubab.uddin on 10/27/2016.
 */
//Takes tweet object, turns it into a view to be displayed in the list
public class TweetsArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Tweet> tweets;
    private Context context;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets){
        this.tweets = tweets;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //get Tweet
        Tweet tweet = getItem(position);
        //inflate the template
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

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

        //populate data into the subviews
        //return the view to be inserted into the list
        return convertView;
    }

    private void configureViewHolderFull(final RecyclerView.ViewHolder viewHolder, int position) {

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
            ButterKnife.bind(this, view);
        }
    }
}
