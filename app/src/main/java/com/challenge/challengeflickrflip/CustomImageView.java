package com.challenge.challengeflickrflip;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by sandeeprana on 14/04/16.
 */
public class CustomImageView extends ImageView {
   public CustomImageView(Context context) {
	  super(context);
   }

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

	  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	  int width = getMeasuredWidth();
	  int height = getMeasuredHeight();
	  int squareLen = width;
	  if (height > width) {
		 squareLen = height;
	  }
	  super.onMeasure(MeasureSpec.makeMeasureSpec(squareLen, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(squareLen, MeasureSpec.EXACTLY));
   }
}
