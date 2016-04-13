package com.challenge.challengeflickrflip;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by sandeeprana on 13/04/16.
 */
public class GridAdapter extends BaseAdapter {


   ArrayList<FlickrImages> arrFlicker;
   LayoutInflater layoutInflater;
   private Context context;



   public GridAdapter(ArrayList<FlickrImages> flickerList, LayoutInflater inflater, Context
		   context) {
	  this.arrFlicker = flickerList;
	  this.layoutInflater = inflater;
	  this.context = context;
   }

   @Override
   public int getCount() {
	  return arrFlicker.size();
   }

   @Override
   public FlickrImages getItem(int position) {
	  return arrFlicker.get(position);
   }

   @Override
   public long getItemId(int position) {
	  return position;
   }

   @Override
   public View getView(int position, View grid, ViewGroup parent) {
	  FlickrImages flickrImages = arrFlicker.get(position);




	  grid = layoutInflater.inflate(R.layout.grid_item, parent,false);
	  final ViewHolder holder = new ViewHolder();

	  int width=grid.getWidth();

	  holder.image = (ImageView) grid.findViewById(R.id.front_image);
	  holder.tName = (TextView) grid.findViewById(R.id.name);
	  holder.tResolution = (TextView) grid.findViewById(R.id.resolution);


	  holder.relLayRootParent = (RelativeLayout) grid.findViewById(R.id.relRootGrid);
	  holder.relLayInfoRoot = (RelativeLayout) grid.findViewById(R.id.relRootInfo);

	  Glide
			  .with(this.context)
			  .load(flickrImages.getSource())
			  .centerCrop()
			  .fitCenter()
			  .placeholder(android.R.drawable.ic_menu_camera)
			  .crossFade()
			  .into(holder.image);


	  holder.tName.setText(flickrImages.getTitle());
	  holder.resolution=flickrImages.getWidth() + "X" + flickrImages.getHeight();
	  holder.tResolution.setText(holder.resolution);

	  holder.image.setMinimumHeight(width/2);

	  final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this.context,
			  R.animator.flip_rightout);

	  final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this.context,
			  R.animator.flip_leftin);


	  holder.relLayRootParent.setOnClickListener(new View.OnClickListener() {

		 boolean isBackVisible = false; // Boolean variable to check if the back image is visible currently
		 @Override
		 public void onClick(View v) {

			if (!isBackVisible) {
			   setRightOut.setTarget(holder.image);
			   setLeftIn.setTarget(holder.relLayInfoRoot);
			   setRightOut.start();
			   setLeftIn.start();
			   isBackVisible = true;
			} else {
			   setRightOut.setTarget(holder.relLayInfoRoot);
			   setLeftIn.setTarget(holder.image);
			   setRightOut.start();
			   setLeftIn.start();
			   isBackVisible = false;
			}
		 }
	  });

	  return grid;
   }


   static class ViewHolder {
	  ImageView image;
	  RelativeLayout relLayRootParent, relLayInfoRoot;
	  TextView tName, tResolution;
	  String resolution;
   }
}

