package com.plantalot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrtiAdapter extends RecyclerView.Adapter<OrtiAdapter.ViewHolder> {
	
	private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
	private final Map<String, List<Integer>> mData;
	private final List<String> mKeys;
	
	OrtiAdapter(Map<String, List<Integer>> data) {
		this.mData = data;
		this.mKeys = new ArrayList<>(mData.keySet());
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_orto, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		String ortoName = mKeys.get(i);
		
		viewHolder.mRecyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		int height = viewHolder.mRecyclerView.getMeasuredHeight();
		int width = viewHolder.mRecyclerView.getMeasuredWidth();
		int varieta = mData.get(ortoName).size();
		
		viewHolder.titleTextView.setText(ortoName);
		viewHolder.labelTextView.setText(varieta + " specie");
		
		GridLayoutManager layoutManager = new GridLayoutManager(viewHolder.mRecyclerView.getContext(), 8, LinearLayoutManager.VERTICAL, false);
		layoutManager.setInitialPrefetchItemCount(mData.get(ortoName).size());
		CardAdapter cardAdapter = new CardAdapter(new ArrayList<>(mData.get(ortoName)));
		
		viewHolder.mRecyclerView.setLayoutManager(layoutManager);
		viewHolder.mRecyclerView.setAdapter(cardAdapter);
		viewHolder.mRecyclerView.setRecycledViewPool(viewPool);
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final TextView titleTextView;
		private final TextView labelTextView;
		private final RecyclerView mRecyclerView;
		
		ViewHolder(final View itemView) {
			super(itemView);
			titleTextView = itemView.findViewById(R.id.title_card_orto);
			labelTextView = itemView.findViewById(R.id.label_specie);
			mRecyclerView = itemView.findViewById(R.id.recycler_home_ortaggi);
		}
	}
	
}
