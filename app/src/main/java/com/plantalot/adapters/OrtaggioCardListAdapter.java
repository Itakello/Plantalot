package com.plantalot.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;

import java.util.ArrayList;
import java.util.List;

public class OrtaggioCardListAdapter extends RecyclerView.Adapter<OrtaggioCardListAdapter.ViewHolder> {
	
	private final List<Pair<String, List<String>>> mData;
	private final int prev_fragment;
	Context context;
	
	public OrtaggioCardListAdapter(@NonNull List<Pair<String, List<String>>> data, int prev_fragment) {
		this.mData = (List<Pair<String, List<String>>>) ((ArrayList) data).clone();  // FIXME ??
		this.prev_fragment = prev_fragment;
		for (Pair<String, List<String>> p : data) {
			if (p.second.isEmpty()) {
				mData.remove(p);
			}
		}
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ortaggio_bl_card_row, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		Pair<String, List<String>> row = mData.get(position);
		
		if (!row.second.isEmpty()) {
			
			viewHolder.mTextView.setText(row.first);
			
			OrtaggioCardRowAdapter ortaggioCardRowAdapter = new OrtaggioCardRowAdapter(row.second, context, prev_fragment);
			viewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
			viewHolder.mRecyclerView.setAdapter(ortaggioCardRowAdapter);
			viewHolder.mRecyclerView.setVisibility(View.VISIBLE);
			
			if (position == 1) {  // FIXME ?
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
				);
				viewHolder.mRecyclerView.setLayoutParams(params);
			}
		} else {
			viewHolder.mRow.setLayoutParams(viewHolder.params);
		}
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final LinearLayout mRow;
		private final TextView mTextView;
		private final RecyclerView mRecyclerView;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
		
		ViewHolder(View view) {
			super(view);
			mRow = view.findViewById(R.id.ortaggio_bl_card_row);
			mTextView = view.findViewById(R.id.ortaggio_bl_card_row_title);
			mRecyclerView = view.findViewById(R.id.ortaggio_bl_card_row_recycler);
		}
	}
	
}
