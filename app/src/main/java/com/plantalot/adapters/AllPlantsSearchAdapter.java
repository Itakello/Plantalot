package com.plantalot.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;
import com.plantalot.database.Db;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


// Cambia il contenuto del backlyer
public class AllPlantsSearchAdapter extends RecyclerView.Adapter<AllPlantsSearchAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final HashMap<String, List<String>> mMap;
	private final LayoutInflater mInflater;
	private final Context context;
	
	// data is passed into the constructor
	public AllPlantsSearchAdapter(Context context, Pair<List<String>, HashMap<String, List<String>>> data) {
		this.mData = data.first;
		this.mMap = data.second;
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	// inflates the row layout from xml when needed
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.all_plants_bl_search_result, parent, false);
		return new ViewHolder(view);
	}
	
	// binds the data to the TextView in each row
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		String ortaggio = mData.get(position);
		viewHolder.mOrtaggioTv.setText(ortaggio);
		viewHolder.mVarietaTv.setVisibility(mMap.get(ortaggio).isEmpty() ? View.GONE : View.VISIBLE);
		viewHolder.mVarietaTv.setText(String.join("\n", mMap.get(ortaggio)));
		viewHolder.mImage.setImageResource(Db.getImageId(context, ortaggio));
		viewHolder.mContent.setOnClickListener(view -> {  // fixme best practice ???
			Bundle bundle = new Bundle();
			bundle.putString("ortaggio", ortaggio);
			Navigation.findNavController(view).navigate(R.id.action_goto_ortaggio, bundle);
		});
	}
	
	// total number of rows
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	
	// stores and recycles views as they are scrolled off screen
	public static class ViewHolder extends RecyclerView.ViewHolder {
		LinearLayout mContent;
		TextView mOrtaggioTv;
		TextView mVarietaTv;
		ImageView mImage;
		
		public ViewHolder(View itemView) {
			super(itemView);
			mContent = itemView.findViewById(R.id.all_plants_bl_search_result);
			mOrtaggioTv = itemView.findViewById(R.id.all_plants_bl_search_result_ortaggio);
			mVarietaTv = itemView.findViewById(R.id.all_plants_bl_search_result_varieta);
			mImage = itemView.findViewById(R.id.all_plants_bl_search_result_image);
		}
	}
}
