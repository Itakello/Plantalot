package com.plantalot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plantalot.R;
import com.plantalot.database.DbPlants;
import com.plantalot.navigation.Nav;

import java.util.List;


public class OrtaggioCardRowAdapter extends RecyclerView.Adapter<OrtaggioCardRowAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final Context context;
	private final int prev_fragment;
	
	public OrtaggioCardRowAdapter(@NonNull List<String> data, Context context, int prev_fragment) {
		this.mData = data;
		this.context = context;
		this.prev_fragment = prev_fragment;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ortaggio_bl_card_item, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		String ortaggio = mData.get(position);
		viewHolder.mTextView.setText(ortaggio);
		viewHolder.mCardView.setCardBackgroundColor(DbPlants.getIconColor(ortaggio));
		viewHolder.mImageView.setImageResource(DbPlants.getImageId(ortaggio));
		viewHolder.mCardView.setOnClickListener(view -> Nav.gotoOrtaggio(ortaggio, prev_fragment, view));
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
			mCardView = view.findViewById(R.id.ortaggio_card_item);
			mTextView = view.findViewById(R.id.ortaggio_card_label);
			mImageView = view.findViewById(R.id.ortaggio_card_image);
		}
	}
	
}
