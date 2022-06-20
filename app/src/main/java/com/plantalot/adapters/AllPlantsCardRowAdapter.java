package com.plantalot.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plantalot.R;
import com.plantalot.database.Db;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllPlantsCardRowAdapter extends RecyclerView.Adapter<AllPlantsCardRowAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final Context context;
	
	public AllPlantsCardRowAdapter(@NonNull List<String> data, Context context) {
		this.mData = data;
		this.context = context;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_plants_fl_card_item, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		String ortaggio = mData.get(position);
		viewHolder.mTextView.setText(ortaggio);
		
		viewHolder.mImageView.setImageResource(Db.getImageId(context, ortaggio));
		viewHolder.mCardView.setCardBackgroundColor(Db.iconColors.get(ortaggio));
		viewHolder.mCardView.setOnClickListener(view -> {  // fixme best practice ???
			Bundle bundle = new Bundle();
			bundle.putString("ortaggio", ortaggio);
			Navigation.findNavController(view).navigate(R.id.action_goto_ortaggio, bundle);
		});
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final MaterialCardView mCardView;
		private final TextView mTextView;
		private final ImageView mImageView;
		
		ViewHolder(View view) {
			super(view);
			mCardView = view.findViewById(R.id.all_plants_card_item);
			mTextView = view.findViewById(R.id.all_plants_card_label);
			mImageView = view.findViewById(R.id.all_plants_card_image);
		}
	}
	
}
