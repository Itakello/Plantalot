package com.plantalot.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
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
import com.plantalot.classes.Carriola;
import com.plantalot.classes.Varieta;
import com.plantalot.database.DbPlants;
import com.plantalot.database.DbUsers;
import com.plantalot.fragments.CarriolaFragment;
import com.plantalot.utils.ColorUtils;

import java.util.List;
import java.util.Locale;

public class CarriolaVarietaAdapter extends RecyclerView.Adapter<CarriolaVarietaAdapter.ViewHolder> {
	
	private final List<Pair<Varieta, Integer>> mData;
	private final CarriolaOrtaggiAdapter mParentAdapter;
	private Context context;
	private Carriola carriola;
	private final int DELAY = 600;
	private boolean holding = false;
	private CarriolaFragment fragment;
	
	public CarriolaVarietaAdapter(@NonNull List<Pair<Varieta, Integer>> data, Carriola carriola,
	                              CarriolaFragment fragment, CarriolaOrtaggiAdapter parentAdapter) {
		this.mData = data;
		this.mParentAdapter = parentAdapter;
		this.carriola = carriola;
		this.fragment = fragment;
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
		Pair<Varieta, Integer> row = mData.get(position);
		int pack = row.first.getAltro_pack();
		
		String ortaggio = row.first.getClassificazione_ortaggio();
		String varieta = row.first.getClassificazione_varieta();
		Varieta varietaObj = row.first;
		viewHolder.mTvName.setText(varieta);
		viewHolder.mTvDist.setText(row.first.getDistanze_piante() + " × " + row.first.getDistanze_file() + " cm");
		viewHolder.mTvCount.setText(String.format(Locale.ITALIAN, "%d", row.second));
		
		Drawable mBtnBkg = viewHolder.mBtnDec.getBackground();
		mBtnBkg.setTint(ColorUtils.alphaColor(DbPlants.getIconColor(ortaggio), 35));
		viewHolder.mBtnDec.setBackground(mBtnBkg);
		viewHolder.mBtnInc.setBackground(mBtnBkg);
		
		viewHolder.mBtnDec.setOnClickListener(view -> {
			if (!holding) {
				viewHolder.mTvCount.setText(updateCount(ortaggio, varietaObj, -1, viewHolder));
			} else {
				holding = false;
			}
		});
		viewHolder.mBtnInc.setOnClickListener(view -> {
			if (!holding) {
				viewHolder.mTvCount.setText(updateCount(ortaggio, varietaObj, +1, viewHolder));
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
						if (carriola.getPianteCount(ortaggio, varieta) == 0) {
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
					viewHolder.mTvCount.setText(updateCount(ortaggio, varietaObj, -pack, viewHolder));
					if (carriola.getPianteCount(ortaggio, varieta) > 0) {
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
					viewHolder.mTvCount.setText(updateCount(ortaggio, varietaObj, +pack, viewHolder));
					mHandler.postDelayed(this, DELAY);
				}
			};
		});
	}
	
	// FIXME !!!
	@RequiresApi(api = Build.VERSION_CODES.N)
	private String updateCount(String ortaggio, Varieta varietaObj, int step, ViewHolder viewHolder) {
		String varieta = varietaObj.getClassificazione_varieta();
		int oldCount = carriola.getPianteCount(ortaggio, varieta);
		int newCount = Math.max(0, oldCount + step);
		carriola.put(ortaggio, varieta, newCount);
		fragment.updateOccupiedArea(varietaObj.calcArea() * (newCount - oldCount));
		mParentAdapter.updateCount(((View) viewHolder.mView.getParent().getParent()).findViewById(R.id.carriola_ortaggio_info), ortaggio);
		DbUsers.updateGiardinoCorrente(carriola, DbUsers.UPDATE);
		return Integer.toString(newCount);
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
