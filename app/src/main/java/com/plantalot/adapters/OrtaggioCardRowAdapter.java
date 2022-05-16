package com.plantalot.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plantalot.R;
import com.plantalot.utils.ColorUtils;

import java.util.List;

public class OrtaggioCardRowAdapter extends RecyclerView.Adapter<OrtaggioCardRowAdapter.ViewHolder> {
	
	private final List<Pair<String, Integer>> mData;
	private final Boolean good;
	private final Context context;
	
	public OrtaggioCardRowAdapter(@NonNull List<Pair<String, Integer>> data, Boolean good, Context context) {
		this.mData = data;
		this.good = good;
		this.context = context;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ortaggio_bl_card_item_good, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		Pair<String, Integer> specs = mData.get(position);
		viewHolder.mTextView.setText(specs.first);
		viewHolder.mImageView.setImageResource(specs.second);
		if (!good) {
//			viewHolder.mCardView.setAlpha(0.5f);
//			viewHolder.mCardView.setStrokeColor(ColorUtils.attrColor(com.google.android.material.R.attr.colorError, context, 40));
		}
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
			mCardView = view.findViewById(R.id.ortaggio_bl_card_item);
			mTextView = view.findViewById(R.id.ortaggio_bl_card_label);
			mImageView = view.findViewById(R.id.ortaggio_bl_card_image);
		}
	}
	
}
