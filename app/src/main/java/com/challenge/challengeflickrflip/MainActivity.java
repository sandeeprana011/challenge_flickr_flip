package com.challenge.challengeflickrflip;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   ImageView imgFront;
   ImageView imgBack;
   Button btnFlip;
   ProgressBar progressBar;
   TextView progressStatus;
   TextView percentStatus;

   boolean isBackVisible = false; // Boolean variable to check if the back image is visible currently
   private GridView gridView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
//	  setContentView(R.layout.activity_main);
	  setContentView(R.layout.activity_main);


	  gridView = (GridView) findViewById(R.id.grid);
	  progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
	  progressStatus= (TextView) findViewById(R.id.progressStatus);
	  percentStatus= (TextView) findViewById(R.id.percentUpdate);

if (Downloader.isNetwork(this)){
   MetaDataDownloadAndParse downloadAndParse = new MetaDataDownloadAndParse();
   downloadAndParse.execute();
}
else {
   Toast.makeText(this,"No network Available",Toast.LENGTH_LONG).show();
}

//	  imgFront = (ImageView) findViewById(R.id.imgFront);
//	  imgBack = (ImageView) findViewById(R.id.imgBack);
//	  btnFlip = (Button) findViewById(R.id.btnFlip);
//
//
//	  final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
//			  R.animator.flip_rightout);
//
//	  final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
//			  R.animator.flip_leftin);
//
//	  btnFlip.setOnClickListener(new View.OnClickListener() {
//		 @Override
//		 public void onClick(View v) {
//			if (!isBackVisible) {
//			   setRightOut.setTarget(imgFront);
//			   setLeftIn.setTarget(imgBack);
//			   setRightOut.start();
//			   setLeftIn.start();
//			   isBackVisible = true;
//			} else {
//			   setRightOut.setTarget(imgBack);
//			   setLeftIn.setTarget(imgFront);
//			   setRightOut.start();
//			   setLeftIn.start();
//			   isBackVisible = false;
//			}
//		 }
//	  });

   }


   public class MetaDataDownloadAndParse extends AsyncTask<Void, String, ArrayList<FlickrImages>> {

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();
//		 progressBar.setVisibility(View.VISIBLE);

	  }

	  @Override
	  protected ArrayList<FlickrImages> doInBackground(Void... params) {
		 ArrayList<FlickrImages> arrayList = new ArrayList<>();
		 Downloader downloader = new Downloader();
		 URLCreater urlCreater = new URLCreater();
		 int progress=0;
		 publishProgress("0","Downloading and Parsing");

		 try {
			String jsonDataString = downloader.downloadContent(urlCreater.getUserInfo(Fields
					.METHOD_USER_PUBLIC_PHOTOS
					, Fields.API_KEY, Fields.USER_ID, Fields.CURRENT_FORMAT, "10", true));
			if (jsonDataString != null) {
			   JSONObject jsonObject = new JSONObject(jsonDataString);
			   if (jsonObject.has(Fields.fPHOTOS) && !jsonObject.isNull(Fields.fPHOTOS)) {
				  JSONObject jsonPhotos = jsonObject.getJSONObject(Fields.fPHOTOS);
				  if (jsonPhotos.has(Fields.fPHOTO) && !jsonPhotos.isNull(Fields.fPHOTO)) {
					 JSONArray arrayPhoto = jsonPhotos.getJSONArray(Fields.fPHOTO);
					 int updateNo=100/arrayPhoto.length();


					 for (int i = 0; i < arrayPhoto.length(); i++) {
						JSONObject jsonPhoto = arrayPhoto.getJSONObject(i);
						FlickrImages image = new FlickrImages();

						if (jsonPhoto.has(Fields.fID) && !jsonPhoto.isNull(Fields.fID)) {
						   image.setId(jsonPhoto.getString(Fields.fID));
						}
						if (jsonPhoto.has(Fields.fTITLE) && !jsonPhoto.isNull(Fields.fTITLE)) {
						   image.setTitle(jsonPhoto.getString(Fields.fTITLE));

						}
						progress=progress+updateNo;
						publishProgress(String.valueOf(progress), image.getTitle());

						if (jsonPhoto.has(Fields.fOWNER) && !jsonPhoto.isNull(Fields.fOWNER)) {
						   image.setOwner(jsonPhoto.getString(Fields.fOWNER));
						}
						if (jsonPhoto.has(Fields.fSECRET) && !jsonPhoto.isNull(Fields.fSECRET)) {
						   image.setSecret(jsonPhoto.getString(Fields.fSECRET));
						}
						if (jsonPhoto.has(Fields.fSERVER) && !jsonPhoto.isNull(Fields.fSERVER)) {
						   image.setServer(jsonPhoto.getString(Fields.fSERVER));
						}

						String jsonDataStringPhotoMeta = downloader.downloadContent(urlCreater
								.getPhotoInfo(Fields.METHOD_PHOTOS_INFO, Fields.API_KEY, image
										.getId(), Fields.CURRENT_FORMAT, true));

						if (jsonDataStringPhotoMeta != null) {
						   JSONObject objectRoot = new JSONObject(jsonDataStringPhotoMeta);
						   if (objectRoot.has(Fields.fSTAT) && !objectRoot.isNull(Fields.fSTAT)) {
							  if (objectRoot.has(Fields.fSIZES) && !objectRoot.isNull(Fields.fSIZES)) {
								 JSONObject objSizes = objectRoot.getJSONObject(Fields.fSIZES);
								 if (objSizes.has(Fields.fSIZE) && !objSizes.isNull(Fields.fSIZE)) {
									JSONArray arrSize = objSizes.getJSONArray(Fields.fSIZE);

									String height = null, width = null, source = null;
									for (int j = 0; j < arrSize.length(); j++) {
									   JSONObject size = arrSize.getJSONObject(j);
									   if (size.has(Fields.fHEIGHT) && !size.isNull(Fields.fHEIGHT)) {
										  height = size.getString(Fields.fHEIGHT);
									   }
									   ;
									   if (size.has(Fields.fWIDTH) && !size.isNull(Fields.fWIDTH)) {
										  width = size.getString(Fields.fWIDTH);
									   }
									   ;
									   if (size.has(Fields.fSOURCE) && !size.isNull(Fields.fSOURCE)) {
										  source = size.getString(Fields.fSOURCE);
									   }
									   ;
									   if (height != null && height.equals(width) && height.equals
											   ("150")) {
										  break;
									   }
									}
									image.setHeight(height);
									image.setWidth(width);
									image.setSource(source);
									arrayList.add(image);

								 } else {
									Log.e("errorformatmeta", "Size array not found");
								 }
							  } else {
								 Log.e("errorformatmeta", "sizes not found");
							  }

						   } else {
							  Log.e("Error", "Server flagged error");
						   }
						}
					 }
				  } else {
					 Log.e("missingfield", Fields.fPHOTOS);
				  }
			   } else {
				  Log.e("missingfield", Fields.fPHOTOS);
			   }
			   publishProgress("100","Completed!");

			}
		 } catch (IOException | JSONException e) {
			e.printStackTrace();
		 }

		 return arrayList;
	  }

	  @Override
	  protected void onProgressUpdate(String... values) {
		 super.onProgressUpdate(values);
		 progressBar.setProgress(Integer.valueOf(values[0]));
		 progressStatus.setText(values[1]);
		 percentStatus.setText(values[0]+"%");
	  }

	  @Override
	  protected void onPostExecute(ArrayList<FlickrImages> flickrImages) {
		 super.onPostExecute(flickrImages);
		 if (gridView != null) {


			GridAdapter adapter = new GridAdapter(flickrImages, getLayoutInflater(),
					getApplicationContext());
			gridView.setAdapter(adapter);
			gridView.setVisibility(View.VISIBLE);


		 }
		 if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
			progressStatus.setVisibility(View.GONE);
			percentStatus.setVisibility(View.GONE);
		 }
	  }
   }


}




















