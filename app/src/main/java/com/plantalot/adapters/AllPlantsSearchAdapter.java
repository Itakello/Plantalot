package com.plantalot.adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


// Cambia il contenuto del backlyer
public class AllPlantsSearchAdapter extends RecyclerView.Adapter<AllPlantsSearchAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final List<String> mSearchText;
	private final HashMap<String, List<String>> mMap;
	private final LayoutInflater mInflater;
	private final Context mContext;
	
	// data is passed into the constructor
	public AllPlantsSearchAdapter(Context context, List<String> data, HashMap<String, List<String>> map, List<String> searchText) {
		this.mData = data;
		this.mMap = map;
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mSearchText = searchText;
	}
	
	// inflates the row layout from xml when needed
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.all_plants_bl_search_result, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
//		onBindViewHolder(viewHolder, position, new ArrayList<>(Collections.singletonList("")));
//	}
//	@Override
//	public void onBindViewHolder(ViewHolder viewHolder, int position, List<Object> payload) {
		String ortaggio = mData.get(position);
//		String mSearchText = (String) payload.get(0);
//		String mSearchText = payload.toString();
		viewHolder.mVarietaTv.setVisibility(mMap.get(ortaggio).isEmpty() ? View.GONE : View.VISIBLE);
		String searchText = mSearchText.get(position);
		System.out.println("##### " + searchText);
		if (!searchText.isEmpty()) {
			String ortaggioTv = ortaggio.toLowerCase().replaceFirst(searchText, "<b>" + searchText + "</b>");
			if (ortaggioTv.charAt(0) != '<') {
				ortaggioTv = ortaggioTv.substring(0, 1).toUpperCase() + ortaggioTv.substring(1);
			} else {
				ortaggioTv = ortaggioTv.substring(0, 3) + ortaggioTv.substring(3, 4).toUpperCase() + ortaggioTv.substring(4);
			}
			viewHolder.mOrtaggioTv.setText(Html.fromHtml(ortaggioTv));
			
			StringBuilder varietaTv = new StringBuilder();
			for (String varieta : mMap.get(ortaggio)) {
				String tmp = varieta.toLowerCase().replaceFirst(searchText, "<b>" + searchText + "</b>");
				if (tmp.charAt(0) != '<') {
					tmp = tmp.substring(0, 1).toUpperCase() + tmp.substring(1);
				} else {
					tmp = tmp.substring(0, 3) + tmp.substring(3, 4).toUpperCase() + tmp.substring(4);
				}
				varietaTv.append(tmp).append("<br>");
			}
			viewHolder.mVarietaTv.setText(Html.fromHtml(varietaTv.toString()));
		} else {
			viewHolder.mOrtaggioTv.setText(ortaggio);
		}
		viewHolder.mImage.setImageResource(Db.getImageId(mContext, ortaggio));
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
