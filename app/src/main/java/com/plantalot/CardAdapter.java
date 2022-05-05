package com.plantalot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
	
	private final List<Integer> mData;
	private final int width;
	
	CardAdapter(List<Integer> data, int width) {
		this.mData = data.subList(0, Math.min(Const.CARD_COLUMNS, data.size()));
		this.width = width;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_view, viewGroup, false);
		Resources r = viewGroup.getContext().getResources();
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
			mImageView = view.findViewById(R.id.image_home_ortaggio);
			
			ViewGroup.LayoutParams lp = itemView.getLayoutParams();
			if (lp instanceof FlexboxLayoutManager.LayoutParams) {
				FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
				flexboxLp.setAlignSelf(AlignItems.CENTER);
			}
			
		}
	}
	
}
