package com.plantalot.adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;
import com.plantalot.classes.User;
import com.plantalot.database.Db;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CarriolaVarietaAdapter extends RecyclerView.Adapter<CarriolaVarietaAdapter.ViewHolder> {
	
	private List<Pair<HashMap<String, Object>, Integer>> mData;
	
	public CarriolaVarietaAdapter(@NonNull List<Pair<HashMap<String, Object>, Integer>> data) {
		this.mData = data;
		System.out.println(data);
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carriola_varieta, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		Pair<HashMap<String, Object>, Integer> row = mData.get(position);
		String ortaggio = (String) row.first.get(Db.VARIETA_CLASSIFICAZIONE_ORTAGGIO);
		String varieta = (String) row.first.get(Db.VARIETA_CLASSIFICAZIONE_VARIETA);
		viewHolder.mTvName.setText(varieta);
		viewHolder.mTvPack.setText(row.first.get(Db.VARIETA_ALTRO_PACK) + " piante per pack");
		viewHolder.mTvCount.setText(row.second.toString());
		viewHolder.mImageDec.setOnClickListener(view -> viewHolder.mTvCount.setText(updateCount(ortaggio, varieta, -1)));
		viewHolder.mImageInc.setOnClickListener(view -> viewHolder.mTvCount.setText(updateCount(ortaggio, varieta, +1)));
	}
	
	// FIXME !!!
	private String updateCount(String ortaggio, String varieta, int step) {
		Integer newCount = Math.max(0, User.carriola.get(ortaggio).get(varieta) + step);
		User.carriola.get(ortaggio).put(varieta, newCount);
		return newCount.toString();
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final TextView mTvName;
		private final TextView mTvPack;
		private final TextView mTvCount;
		private final ImageView mImageDec;
		private final ImageView mImageInc;
		
		ViewHolder(View view) {
			super(view);
			mTvName = view.findViewById(R.id.carriola_varieta_name);
			mTvPack = view.findViewById(R.id.carriola_varieta_pack);
			mTvCount = view.findViewById(R.id.carriola_varieta_count);
			mImageDec = view.findViewById(R.id.carriola_varieta_decrement);
			mImageInc = view.findViewById(R.id.carriola_varieta_increment);
		}
	}
	
}
