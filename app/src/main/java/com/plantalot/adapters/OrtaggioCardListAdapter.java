package com.plantalot.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;
import com.plantalot.utils.Utils;

import java.util.List;

// Riempe la card con le icone
public class OrtaggioCardListAdapter extends RecyclerView.Adapter<OrtaggioCardListAdapter.ViewHolder> {
	
	private final List<Pair<String, List<Pair<String, Integer>>>> mData;
	Context context;
	
	public OrtaggioCardListAdapter(@NonNull List<Pair<String, List<Pair<String, Integer>>>> data) {
		this.mData = data;
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
		Pair<String, List<Pair<String, Integer>>> row = mData.get(position);
		viewHolder.mTextView.setText(row.first);
		
		OrtaggioCardRowAdapter ortaggioCardRowAdapter = new OrtaggioCardRowAdapter(row.second, position % 2 == 0, context);
		viewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
		viewHolder.mRecyclerView.setAdapter(ortaggioCardRowAdapter);
		
		if (position == 1) {  // FIXME ?
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT
			);
			params.setMargins(0, 0, 0, Utils.dp2px(16, context));
			viewHolder.mRecyclerView.setLayoutParams(params);
		}
		
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final TextView mTextView;
		private final RecyclerView mRecyclerView;
		
		ViewHolder(View view) {
			super(view);
			mTextView = view.findViewById(R.id.ortaggio_bl_card_row_title);
			mRecyclerView = view.findViewById(R.id.ortaggio_bl_card_row_recycler);
		}
	}
	
}
