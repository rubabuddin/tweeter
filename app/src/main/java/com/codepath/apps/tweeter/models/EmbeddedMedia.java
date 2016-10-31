package com.codepath.apps.tweeter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by rubab.uddin on 10/30/2016.
 */

@Table(name = "EmbeddedMedia")
@Parcel(analyze = {EmbeddedMedia.class})
public class EmbeddedMedia extends Model {

    @Column(name = "MediaId")
    public long id;

    @Column(name = "MediaUrl")
    public String mediaUrl;

    @Column(name = "Url")
    public String url;

    @Column(name = "Type")
    public String type;

    @Column(name = "Width")
    public int width;

    @Column(name = "Height")
    public int height;

    @Column(name = "Resize")
    public String resize;

    // empty constructor needed by ActiveAndroid and the Parceler library
    public EmbeddedMedia() {
        super();
    }

    public void fromJSON(JSONObject jsonObject) throws JSONException {
        //EmbeddedMedia model
        id = jsonObject.getLong("id");
        mediaUrl = jsonObject.getString("media_url");
        url = jsonObject.getString("url");
        type = jsonObject.getString("type");

        JSONObject objectSizes = jsonObject.getJSONObject("sizes");
        if (objectSizes != null) {
            JSONObject selectedSize = objectSizes.getJSONObject("large");
            if (selectedSize != null) {
                width = selectedSize.getInt("w");
                height = selectedSize.getInt("h");
                resize = selectedSize.getString("resize");
            }
        }
    }
    /*{
  ...
  "text": "Four more years. http:\/\/t.co\/bAJE6Vom",
  "entities": {
    "hashtags": [],
    "symbols": [],
    "urls": [],
    "user_mentions": [],
    "media": [{
      "id": 266031293949698048,
      "id_str": "266031293949698048",
      "indices": [17, 37],
      "media_url": "http:\/\/pbs.twimg.com\/media\/A7EiDWcCYAAZT1D.jpg",
      "media_url_https": "https:\/\/pbs.twimg.com\/media\/A7EiDWcCYAAZT1D.jpg",
      "url": "http:\/\/t.co\/bAJE6Vom",
      "display_url": "pic.twitter.com\/bAJE6Vom",
      "expanded_url": "http:\/\/twitter.com\/BarackObama\/status\/266031293945503744\/photo\/1",
      "type": "photo",
      "sizes": {
        "medium": {
          "w": 600,
          "h": 399,
          "resize": "fit"
        },
        "thumb": {
          "w": 150,
          "h": 150,
          "resize": "crop"
        },
        "small": {
          "w": 340,
          "h": 226,
          "resize": "fit"
        },
        "large": {
          "w": 800,
          "h": 532,
          "resize": "fit"
        }
      }
    }]
  }
}*/
}