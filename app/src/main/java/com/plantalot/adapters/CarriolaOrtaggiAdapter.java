package com.plantalot.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plantalot.R;
import com.plantalot.database.Db;
import com.plantalot.utils.ColorUtils;
import com.plantalot.utils.Utils;

import java.util.List;

public class CarriolaOrtaggiAdapter extends RecyclerView.Adapter<CarriolaOrtaggiAdapter.ViewHolder> {
	
	private final List<Pair<String, List<String>>> mData;
	
	public CarriolaOrtaggiAdapter(@NonNull List<Pair<String, List<String>>> data) {
		this.mData = data;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carriola_ortaggio, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		String ortaggio = mData.get(position).first;
		
		Drawable mCardHeaderBkg = viewHolder.mCardHeader.getBackground();
		mCardHeaderBkg.setTint(ColorUtils.alphaColor(Db.getIconColor(ortaggio), 35));
		viewHolder.mCardHeader.setBackground(mCardHeaderBkg);
		viewHolder.mBackground.setBackgroundColor(ColorUtils.alphaColor(Db.getIconColor(ortaggio), 25));
		viewHolder.mImage.setImageResource(Db.getImageId(ortaggio));
		viewHolder.mTvName.setText(ortaggio);
		viewHolder.mTvSubtitle.setText(mData.get(position).second.size() + " varietà");
		
//		OrtaggioCardRowAdapter ortaggioCardRowAdapter = new OrtaggioCardRowAdapter(row.second, context, prev_fragment);
//		viewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
//		viewHolder.mRecyclerView.setAdapter(ortaggioCardRowAdapter);
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final MaterialCardView mCard;
		private final View mBackground;
		private final ConstraintLayout mCardHeader;
		private final ImageView mImage;
		private final TextView mTvName;
		private final TextView mTvSubtitle;
//		private final RecyclerView mRecyclerView;
		
		ViewHolder(View view) {
			super(view);
			mBackground = view.findViewById(R.id.carriola_ortaggio_background);
			mCard = view.findViewById(R.id.carriola_ortaggio_card);
			mCardHeader = view.findViewById(R.id.carriola_ortaggio_card_header);
			mImage = view.findViewById(R.id.carriola_ortaggio_image);
			mTvName = view.findViewById(R.id.carriola_ortaggio_name);
			mTvSubtitle = view.findViewById(R.id.carriola_ortaggio_subtitle);
		}
	}
	
}
