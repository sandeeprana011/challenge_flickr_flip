package com.challenge.challengeflickrflip;

/**
 * Created by sandeeprana on 13/04/16.
 */
public class URLCreater {

   public String getUserInfo(String method, String apiKey, String userId, String currentFormat,
							 String perPageLimit, boolean jsonCallBack) {

	  String URL = Fields.BASE_URL + "?" +
			  "&method=" + method +
			  "&api_key=" + apiKey +
			  "&user_id=" + userId +
			  "&format=" + currentFormat +
			  "&per_page=" + perPageLimit;

	  if (jsonCallBack) {
		 URL = URL.concat("&nojsoncallback=?");
		 return URL;
	  } else {
		 return URL;
	  }
   }

   public String getPhotoInfo(String method, String apiKey, String photoId, String currentFormat,
							  boolean jsonCallBack) {

	  String URL_Image = "https://api.flickr.com/services/rest/?" +
			  "&method=" + method +
			  "&api_key=" + apiKey +
			  "&photo_id=" + photoId +
			  "&format=" + currentFormat;

	  if (jsonCallBack) {
		 URL_Image = URL_Image.concat("&nojsoncallback=?");
		 return URL_Image;
	  } else {
		 return URL_Image;
	  }
   }


}
