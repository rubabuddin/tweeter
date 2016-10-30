package com.codepath.apps.tweeter;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "3MSB4epJnH0G7o3xj70JWiKLx";       // Change this
	public static final String REST_CONSUMER_SECRET = "zz6suUZ06Qn8ItwAdAmv1c9Fq50HNQKGgiJABEztmi8dC795yP"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cprest"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if (maxId != -1) {
			params.put("max_id", maxId);
		} else {
			params.put("since_id", 1);
		}
		getClient().get(apiUrl, params, handler);
	}

	//compose tweet
	//POST statuses/update.json
	public void postStatus(String status, long replyStatusId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);

		//for tweet detail view, add reply to feature
		if (replyStatusId != -1) {
			params.put("in_reply_to_status_id", replyStatusId);
		}
		getClient().post(apiUrl, params, handler);
	}

	//favorite tweet
	// POST favorites/create.json
	public void markFavorite(long tweetId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		getClient().post(apiUrl, params, handler);
	}

	// GET account/verify_credentials.json
	// request personal credentials
	public void getUser(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);


	Endpoints
			BASE_URL = https://api.twitter.com/1.1
	Get the home timeline from the user
	GET "/statuses/home_timeline.json"
	count=25 (number of tweets to get)
	since_id=1 (all recent tweets, sorted by most recent)
	Pasted from <https://dev.twitter.com/rest/reference/get/statuses/home_timeline>

	Create a new tweet
			POST
 */
}