package com.plantalot.adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.button.MaterialButton;
import com.plantalot.R;

import java.util.List;


// Navigation buttons in Home

public class NavbuttonsAdapter extends RecyclerView.Adapter<NavbuttonsAdapter.ViewHolder> {
	
	private final List<Pair<String, Integer>> mData;
	
	public NavbuttonsAdapter(List<Pair<String, Integer>> data) {
		this.mData = data;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_fl_navbutton, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		int icon = mData.get(position).second;
		String label = mData.get(position).first;
		viewHolder.mButton.setIconResource(icon);
		viewHolder.mTextView.setText(label);
		ViewGroup.LayoutParams params = viewHolder.mTextView.getLayoutParams();
		viewHolder.mTextView.setLayoutParams(params);
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		MaterialButton mButton;
		TextView mTextView;
		
		ViewHolder(View view) {
			super(view);
			mButton = view.findViewById(R.id.home_fl_navbutton_icon);
			mTextView = view.findViewById(R.id.home_fl_navbutton_label);
			
			ViewGroup.LayoutParams lp = itemView.getLayoutParams();
			if (lp instanceof FlexboxLayoutManager.LayoutParams) {
				FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
				flexboxLp.setAlignSelf(AlignItems.CENTER);
			}
			
		}
	}
	
}
