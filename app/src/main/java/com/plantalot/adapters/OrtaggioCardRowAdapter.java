package com.plantalot.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plantalot.R;
import com.plantalot.database.Db;
import com.plantalot.utils.ColorUtils;

import java.util.List;

public class OrtaggioCardRowAdapter extends RecyclerView.Adapter<OrtaggioCardRowAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final Boolean good;
	private final Context context;
	private final int prev_fragment;
	
	public OrtaggioCardRowAdapter(@NonNull List<String> data, Boolean good, Context context, int prev_fragment) {
		this.mData = data;
		this.good = good;
		this.context = context;
		this.prev_fragment = prev_fragment;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ortaggio_bl_card_item_good, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		String ortaggio = mData.get(position);
		viewHolder.mTextView.setText(ortaggio);
		
		String imageFile = Db.icons.get(ortaggio);
		if (imageFile != null) {
			Resources res = context.getResources();
			int imageId = res.getIdentifier(imageFile.split("\\.")[0], "mipmap", context.getPackageName());
			if (imageId > 0) {
				viewHolder.mImageView.setImageResource(imageId);
			}
		}
		
		if (!good) {
//			viewHolder.mCardView.setAlpha(0.5f);
//			viewHolder.mCardView.setStrokeColor(ColorUtils.attrColor(com.google.android.material.R.attr.colorError, context, 40));
		}
		
		viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {  // fixme best practice ???
				Bundle bundle = new Bundle();
				bundle.putString("ortaggio", ortaggio);
				bundle.putInt("prev_fragment", prev_fragment);
				Navigation.findNavController(view).navigate(R.id.action_goto_ortaggio, bundle);
			}
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
			mCardView = view.findViewById(R.id.ortaggio_bl_card_item);
			mTextView = view.findViewById(R.id.ortaggio_bl_card_label);
			mImageView = view.findViewById(R.id.ortaggio_bl_card_image);
		}
	}
	
}
