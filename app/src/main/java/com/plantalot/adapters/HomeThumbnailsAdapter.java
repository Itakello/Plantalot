package com.plantalot.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.card.MaterialCardView;
import com.plantalot.utils.Consts;
import com.plantalot.R;

import java.util.List;

// Riempe la card con le icone
public class HomeThumbnailsAdapter extends RecyclerView.Adapter<HomeThumbnailsAdapter.ViewHolder> {
	
	private final List<Integer> mData;
	private final int width;
	
	public HomeThumbnailsAdapter(@NonNull List<Integer> data, int width) {
		this.mData = data.subList(0, Math.min(Consts.CARD_PLANTS, data.size()));
		this.width = width;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_fl_imageview, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		int icon = mData.get(position);
		viewHolder.mImageView.setImageResource(icon);
		ViewGroup.LayoutParams params = viewHolder.mImageView.getLayoutParams();
		params.height = params.width = width;
		viewHolder.mImageView.setLayoutParams(params);
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		ImageView mImageView;
		
		ViewHolder(View view) {
			super(view);
			mImageView = view.findViewById(R.id.home_imageview_ortaggio);
			
			ViewGroup.LayoutParams lp = itemView.getLayoutParams();
			if (lp instanceof FlexboxLayoutManager.LayoutParams) {
				FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
				flexboxLp.setAlignSelf(AlignItems.CENTER);
			}
			
		}
	}
	
}
