package com.plantalot.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;

import java.util.List;


public class AllPlantsCardListAdapter extends RecyclerView.Adapter<AllPlantsCardListAdapter.ViewHolder> {
	
	private final List<Pair<String, List<String>>> mData;
	private final ProgressBar mProgressBar;
	Context context;
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public AllPlantsCardListAdapter(@NonNull List<Pair<String, List<String>>> data, ProgressBar progressBar) {
		this.mData = data;
		this.mProgressBar = progressBar;
//		this.mData.removeIf(p -> p.second.isEmpty());
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		mProgressBar.setVisibility(View.GONE);
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_plants_bl_card_row, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		Pair<String, List<String>> row = mData.get(position);
		if (!row.second.isEmpty()) {
			viewHolder.mTextView.setText(row.first);
			AllPlantsCardRowAdapter allPlantsCardRowAdapter = new AllPlantsCardRowAdapter(row.second, context, R.id.allPlantsFragment);
			viewHolder.mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
			viewHolder.mRecyclerView.setAdapter(allPlantsCardRowAdapter);
		} else {
			viewHolder.mRow.setLayoutParams(viewHolder.params);
		}
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final TextView mTextView;
		private final RecyclerView mRecyclerView;
		private final LinearLayout mRow;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
		
		ViewHolder(View view) {
			super(view);
			mRow = view.findViewById(R.id.all_plants_bl_card_row);
			mTextView = view.findViewById(R.id.all_plants_bl_card_row_title);
			mRecyclerView = view.findViewById(R.id.all_plants_bl_card_row_recycler);
		}
	}
}
