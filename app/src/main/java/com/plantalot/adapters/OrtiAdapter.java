package com.plantalot.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.plantalot.utils.Consts;
import com.plantalot.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrtiAdapter extends RecyclerView.Adapter<OrtiAdapter.ViewHolder> {
	
	private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
	private final Map<String, List<Integer>> mData;
	private final List<String> mKeys;
	Context context;
	
	public OrtiAdapter(Map<String, List<Integer>> data) {
		this.mData = data;
		this.mKeys = new ArrayList<>(mData.keySet());
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.card_orto, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		String ortoName = mKeys.get(i);
		int specie = mData.get(ortoName).size();
		
		viewHolder.mFrameLayout.post(new Runnable() {  // needed to get the width (fixme ?)
			public void run() {
				int width = viewHolder.mFrameLayout.getWidth();
				int padding = viewHolder.mFrameLayout.getPaddingTop();
				int imgwidth = (width - 2 * padding) / (Consts.CARD_COLUMNS / 2);
				
				FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
				layoutManager.setJustifyContent(JustifyContent.CENTER);
				CardAdapter cardAdapter = new CardAdapter(new ArrayList<>(mData.get(ortoName)), imgwidth);
				
				viewHolder.mRecyclerView.setLayoutManager(layoutManager);
				viewHolder.mRecyclerView.setAdapter(cardAdapter);
				viewHolder.mRecyclerView.setRecycledViewPool(viewPool);
				
				ViewGroup.LayoutParams params = viewHolder.mFrameLayout.getLayoutParams();
				params.height = 2 * ((width - 2 * padding) / (Consts.CARD_COLUMNS / 2) + padding);
				viewHolder.mFrameLayout.setLayoutParams(params);
				if (specie == 5 || specie == 6) {  // compact view
					viewHolder.mFrameLayout.setPadding(padding + imgwidth / 2, padding, padding + imgwidth / 2, padding);
				}
				
				viewHolder.titleTextView.setText(ortoName);
				viewHolder.labelTextView.setText(specie + " specie");
				
			}
		});
		
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final TextView titleTextView;
		private final TextView labelTextView;
		private final RecyclerView mRecyclerView;
		private final FrameLayout mFrameLayout;
		
		ViewHolder(final View itemView) {
			super(itemView);
			titleTextView = itemView.findViewById(R.id.title_card_orto);
			labelTextView = itemView.findViewById(R.id.label_specie);
			mRecyclerView = itemView.findViewById(R.id.recycler_home_ortaggi);
			mFrameLayout = itemView.findViewById(R.id.layout_home_ortaggi);
		}
	}
	
}
