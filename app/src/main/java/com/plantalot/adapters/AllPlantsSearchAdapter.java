package com.plantalot.adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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

import java.util.HashMap;
import java.util.List;

public class AllPlantsSearchAdapter extends RecyclerView.Adapter<AllPlantsSearchAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final List<String> mSearchText;
	private final HashMap<String, List<String>> mMap;
	private final LayoutInflater mInflater;
	private final Context mContext;
	
	public AllPlantsSearchAdapter(Context context, List<String> data, HashMap<String, List<String>> map, List<String> searchText) {
		this.mData = data;
		this.mMap = map;
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mSearchText = searchText;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.all_plants_bl_search_result, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		String ortaggio = mData.get(position);
		viewHolder.mVarietaTv.setVisibility(mMap.get(ortaggio).isEmpty() ? View.GONE : View.VISIBLE);
		String searchText = mSearchText.get(position);
		if (!searchText.isEmpty()) {
			viewHolder.mOrtaggioTv.setText(Html.fromHtml(proper(ortaggio.toLowerCase().replaceFirst(searchText, "<b>" + searchText + "</b>"))));
			StringBuilder varietaTv = new StringBuilder();
			for (String varieta : mMap.get(ortaggio)) {
				varietaTv.append(proper(varieta.toLowerCase().replaceFirst(searchText, "<b>" + searchText + "</b>"))).append("<br>");
			}
			viewHolder.mVarietaTv.setText(Html.fromHtml(varietaTv.substring(0, Math.max(varietaTv.length() - "</br>".length() + 1, 0))));
		} else {
			viewHolder.mOrtaggioTv.setText(ortaggio);
		}
		viewHolder.mImage.setImageResource(Db.getImageId(mContext, ortaggio));
		viewHolder.mContent.setOnClickListener(view -> {
			Bundle bundle = new Bundle();
			bundle.putString("ortaggio", ortaggio);
			Navigation.findNavController(view).navigate(R.id.action_goto_ortaggio, bundle);
		});
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
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
	
	private String proper(String str) {
		String ret = str;
		if (ret.charAt(0) != '<') {
			ret = ret.substring(0, 1).toUpperCase() + ret.substring(1);
		} else {
			ret = ret.substring(0, 3) + ret.substring(3, 4).toUpperCase() + ret.substring(4);
		}
		return ret;
	}
}
