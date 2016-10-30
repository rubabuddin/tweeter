package com.codepath.apps.tweeter.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TwitterApplication;
import com.codepath.apps.tweeter.TwitterClient;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by rubab.uddin on 10/29/2016.
 */

public class ComposeTweetDialogFragment extends DialogFragment {
    private static final String TAG = ComposeTweetDialogFragment.class.getSimpleName();

    @BindView(R.id.btnTweet)
    Button btnTweet;
    @BindView(R.id.etTweetBody)
    EditText etTweetBody;
    @BindView(R.id.tvCharsLeft)
    TextView tvCharsLeft;
    @BindView(R.id.btnClose)
    ImageButton btnClose;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.ivProfile)
    ImageView ivProfilePic;
    @BindView(R.id.tvLocation)
    TextView tvLocation;

    private int charLength;
    private TwitterClient twitterClient;
    private User user;

    public interface ComposeTweetDialogListener {
        void onUpdateStatusSuccess(Tweet statusTweet);
    }

    private void sendSuccess(Tweet newTweet) {
        ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
        if (listener != null) {
            listener.onUpdateStatusSuccess(newTweet);
        }
        dismiss();
    }

    public ComposeTweetDialogFragment() {
        twitterClient = TwitterApplication.getRestClient();
    }

    public static ComposeTweetDialogFragment newInstance(User user) {
        ComposeTweetDialogFragment frag = new ComposeTweetDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        charLength = 140;
        user = Parcels.unwrap(getArguments().getParcelable("user"));
        if (user != null) {
            initDialog();
        } else {
            Log.e("ERROR", "User = NULL");
        }
        etTweetBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvCharsLeft.setText(Integer.toString(charLength));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int remainingChars = charLength - s.length();
                tvCharsLeft.setText(Integer.toString(remainingChars));
                if (remainingChars < 0) {
                    tvCharsLeft.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                    btnTweet.setEnabled(false);
                } else {
                    tvCharsLeft.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
                    btnTweet.setEnabled(true);
                }
            }
        });
    }

    @OnClick(R.id.btnTweet)
    public void postTweet() {
        twitterClient.postStatus(etTweetBody.getText().toString(), -1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Tweet newTweet = Tweet.fromJSON(response);
                if (response != null) {
                    sendSuccess(newTweet);
                } else {
                    Log.d(TAG, "Compose tweet failed: " + response.toString());
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private void initDialog() {

        btnClose.setColorFilter(R.color.twitterColor); // White Tint
        tvUser.setText(user.getUserName());
        Glide.with(getActivity()).load(user.getProfileImageUrl())
                .fitCenter().centerCrop()
                .into(ivProfilePic);

    }


    @OnClick(R.id.btnClose)
    public void closeDialog() {
        dismiss();
    }



}
