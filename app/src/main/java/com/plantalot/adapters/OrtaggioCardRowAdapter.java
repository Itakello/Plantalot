package com.plantalot.adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;

import java.util.List;

// Riempe la card con le icone
public class OrtaggioCardRowAdapter extends RecyclerView.Adapter<OrtaggioCardRowAdapter.ViewHolder> {
	
	private final List<Pair<String, Integer>> mData;
	
	public OrtaggioCardRowAdapter(@NonNull List<Pair<String, Integer>> data) {
		this.mData = data;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ortaggio_bl_card_item, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		Pair<String, Integer> specs = mData.get(position);
		viewHolder.mTextView.setText(specs.first);
		viewHolder.mImageView.setImageResource(specs.second);
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final TextView mTextView;
		private final ImageView mImageView;
		
		ViewHolder(View view) {
			super(view);
			mTextView = view.findViewById(R.id.ortaggio_bl_card_label);
			mImageView = view.findViewById(R.id.ortaggio_bl_card_image);
		}
	}
	
}
