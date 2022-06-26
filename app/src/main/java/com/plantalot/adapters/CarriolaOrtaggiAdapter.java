package com.plantalot.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plantalot.R;
import com.plantalot.classes.User;
import com.plantalot.classes.Varieta;
import com.plantalot.database.Db;
import com.plantalot.navigation.Nav;
import com.plantalot.utils.ColorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarriolaOrtaggiAdapter extends RecyclerView.Adapter<CarriolaOrtaggiAdapter.ViewHolder> {
	
	private final List<Pair<String, List<Pair<Varieta, Integer>>>> mData;
	Context context;
	
	public CarriolaOrtaggiAdapter(@NonNull List<Pair<String, List<Pair<Varieta, Integer>>>> data) {
		this.mData = data;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carriola_ortaggio, viewGroup, false);
		this.context = viewGroup.getContext();
		return new ViewHolder(view);
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		String ortaggio = mData.get(position).first;
		
		Drawable mCardHeaderBkg = viewHolder.mCardHeader.getBackground();
		mCardHeaderBkg.setTint(ColorUtils.alphaColor(Db.getIconColor(ortaggio), 35));
		viewHolder.mCardHeader.setBackground(mCardHeaderBkg);
		viewHolder.mBackground.setBackgroundColor(ColorUtils.alphaColor(Db.getIconColor(ortaggio), 25));
		viewHolder.mImage.setImageResource(Db.getImageId(ortaggio));
		viewHolder.mTvName.setText(ortaggio);
		updateCount(viewHolder.mTvSubtitle, ortaggio);
		
		CarriolaVarietaAdapter carriolaVarietaAdapter = new CarriolaVarietaAdapter(mData.get(position).second, this);
		viewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
		viewHolder.mRecyclerView.setAdapter(carriolaVarietaAdapter);
		
		viewHolder.mCardHeader.setOnClickListener(view -> Nav.gotoOrtaggio(ortaggio, R.id.carriolaFragment, view));
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public void updateCount(TextView tv, String ortaggio) {
		Resources res = context.getResources();
		int n_varieta = User.carriola.get(ortaggio).size();
		int count = (new ArrayList<>(User.carriola.get(ortaggio).values())).stream().mapToInt(Integer::intValue).sum();
		tv.setText(res.getQuantityString(R.plurals.n_varieta, n_varieta, n_varieta) + "   |   " + res.getQuantityString(R.plurals.n_piante, count, count));
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final View mBackground;
		private final MaterialCardView mCardHeader;
		private final ImageView mImage;
		private final TextView mTvName;
		private final TextView mTvSubtitle;
		private final RecyclerView mRecyclerView;
		
		ViewHolder(View view) {
			super(view);
			mBackground = view.findViewById(R.id.carriola_ortaggio_background);
			mCardHeader = view.findViewById(R.id.carriola_ortaggio_card_header);
			mImage = view.findViewById(R.id.carriola_ortaggio_image);
			mTvName = view.findViewById(R.id.carriola_ortaggio_name);
			mTvSubtitle = view.findViewById(R.id.carriola_ortaggio_subtitle);
			mRecyclerView = view.findViewById(R.id.carriola_varieta_recycler);
		}
	}
	
}
