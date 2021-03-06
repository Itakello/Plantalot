package com.plantalot.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plantalot.R;
import com.plantalot.classes.User;
import com.plantalot.classes.Varieta;
import com.plantalot.database.Db;
import com.plantalot.utils.ColorUtils;

import java.util.List;
import java.util.Locale;

public class CarriolaVarietaAdapter extends RecyclerView.Adapter<CarriolaVarietaAdapter.ViewHolder> {
	
	private final List<Pair<Varieta, Integer>> mData;
	private final CarriolaOrtaggiAdapter mParentAdapter;
	private Context context;
	private final int DELAY = 600;
	private boolean holding = false;
	
	public CarriolaVarietaAdapter(@NonNull List<Pair<Varieta, Integer>> data, CarriolaOrtaggiAdapter parentAdapter) {
		this.mData = data;
		this.mParentAdapter = parentAdapter;
		System.out.println(data);
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carriola_varieta, viewGroup, false);
		this.context = viewGroup.getContext();
		return new ViewHolder(view);
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		Resources res = context.getResources();
		Pair<Varieta, Integer> row = mData.get(position);
		int pack = row.first.getAltro_pack();
		
		String ortaggio = row.first.getClassificazione_ortaggio();
		String varieta = row.first.getClassificazione_varieta();
		viewHolder.mTvName.setText(varieta);
		viewHolder.mTvDist.setText(row.first.getDistanze_piante() + " ?? " + row.first.getDistanze_file() + " cm");
		viewHolder.mTvCount.setText(String.format(Locale.ITALIAN, "%d", row.second));
		
		Drawable mBtnBkg = viewHolder.mBtnDec.getBackground();
		mBtnBkg.setTint(ColorUtils.alphaColor(Db.getIconColor(ortaggio), 35));
		viewHolder.mBtnDec.setBackground(mBtnBkg);
		viewHolder.mBtnInc.setBackground(mBtnBkg);
		
		viewHolder.mBtnDec.setOnClickListener(view -> {
			if (!holding) {
				viewHolder.mTvCount.setText(updateCount(ortaggio, varieta, -1, viewHolder));
			} else {
				holding = false;
			}
		});
		viewHolder.mBtnInc.setOnClickListener(view -> {
			if (!holding) {
				viewHolder.mTvCount.setText(updateCount(ortaggio, varieta, +1, viewHolder));
			} else {
				holding = false;
			}
		});
		
		viewHolder.mBtnDec.setOnTouchListener(new View.OnTouchListener() {
			Handler mHandler;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (mHandler != null) return true;
						if (User.carriola.get(ortaggio).get(varieta) == 0) {
							holding = false;
							return true;
						}
						mHandler = new Handler();
						mHandler.postDelayed(mAction, DELAY);
						break;
					case MotionEvent.ACTION_UP:
						if (mHandler == null) return true;
						mHandler.removeCallbacks(mAction);
						mHandler = null;
						break;
					case MotionEvent.ACTION_MOVE:
						holding = false;
						if (mHandler == null) return true;
						mHandler.removeCallbacks(mAction);
						mHandler = null;
						break;
				}
				return false;
			}
			
			final Runnable mAction = new Runnable() {
				@Override
				public void run() {
					holding = true;
					viewHolder.mTvCount.setText(updateCount(ortaggio, varieta, -pack, viewHolder));
					if (User.carriola.get(ortaggio).get(varieta) > 0) {
						mHandler.postDelayed(this, DELAY);
					}
				}
			};
		});
		
		viewHolder.mBtnInc.setOnTouchListener(new View.OnTouchListener() {
			Handler mHandler;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (mHandler != null) return true;
						mHandler = new Handler();
						mHandler.postDelayed(mAction, DELAY);
						break;
					case MotionEvent.ACTION_UP:
						if (mHandler == null) return true;
						mHandler.removeCallbacks(mAction);
						mHandler = null;
						break;
					case MotionEvent.ACTION_MOVE:
						holding = false;
						if (mHandler == null) return true;
						mHandler.removeCallbacks(mAction);
						mHandler = null;
						break;
				}
				return false;
			}
			
			final Runnable mAction = new Runnable() {
				@Override
				public void run() {
					holding = true;
					viewHolder.mTvCount.setText(updateCount(ortaggio, varieta, +pack, viewHolder));
					mHandler.postDelayed(this, DELAY);
				}
			};
		});
	}
	
	// FIXME !!!
	@RequiresApi(api = Build.VERSION_CODES.N)
	private String updateCount(String ortaggio, String varieta, int step, ViewHolder viewHolder) {
		Integer newCount = Math.max(0, User.carriola.get(ortaggio).get(varieta) + step);
		User.carriola.get(ortaggio).put(varieta, newCount);
		mParentAdapter.updateCount(((View) viewHolder.mView.getParent().getParent()).findViewById(R.id.carriola_ortaggio_info), ortaggio);
		return newCount.toString();
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final View mView;
		private final TextView mTvName;
		private final TextView mTvDist;
		private final TextView mTvCount;
		private final MaterialCardView mBtnDec;
		private final MaterialCardView mBtnInc;
		
		ViewHolder(View view) {
			super(view);
			mView = view.findViewById(R.id.carriola_varieta);
			mTvName = view.findViewById(R.id.carriola_varieta_name);
			mTvDist = view.findViewById(R.id.carriola_varieta_dist);
			mTvCount = view.findViewById(R.id.carriola_varieta_count);
			mBtnDec = view.findViewById(R.id.carriola_varieta_decrement);
			mBtnInc = view.findViewById(R.id.carriola_varieta_increment);
		}
	}
	
}
