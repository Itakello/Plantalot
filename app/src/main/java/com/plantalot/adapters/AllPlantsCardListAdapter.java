package com.plantalot.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;

import java.util.List;


public class AllPlantsCardListAdapter extends RecyclerView.Adapter<AllPlantsCardListAdapter.ViewHolder> {
	
	private final List<Pair<String, List<String>>> mData;
	Context context;
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public AllPlantsCardListAdapter(@NonNull List<Pair<String, List<String>>> data) {
		this.mData = data;
		mData.removeIf(p -> p.second.isEmpty());
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_plants_bl_card_row, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		Pair<String, List<String>> row = mData.get(position);
		viewHolder.mTextView.setText(row.first);
		AllPlantsCardRowAdapter allPlantsCardRowAdapter = new AllPlantsCardRowAdapter(row.second, context, R.id.allPlantsFragment);
		viewHolder.mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
		viewHolder.mRecyclerView.setAdapter(allPlantsCardRowAdapter);
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
