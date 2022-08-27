package com.plantalot.adapters;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.MyApplication;
import com.plantalot.R;
import com.plantalot.components.InputDialog;
import com.plantalot.database.DbUsers;
import com.plantalot.fragments.HomeFragment;
import com.plantalot.utils.Utils;

import java.util.List;

// Cambia il contenuto del backlyer
public class HomeGiardiniAdapter extends RecyclerView.Adapter<HomeGiardiniAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final LayoutInflater mInflater;
	private final View fragView;
	private final Context context;
	private final MyApplication app;
	private final HomeFragment homeFragment;
	
	// data is passed into the constructor
	public HomeGiardiniAdapter(Context context, View fragView, MyApplication app, HomeFragment homeFragment) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = app.user.getGiardiniNames();
		this.fragView = fragView;
		this.context = context;
		this.app = app;
		this.homeFragment = homeFragment;
	}
	
	// inflates the row layout from xml when needed
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.home_bl_drawer_button, parent, false);
		return new ViewHolder(view);
	}
	
	// binds the data to the TextView in each row
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int i) {
		String nomeGiardino = mData.get(i);
		viewHolder.giardinoBtn.setText(nomeGiardino);
		
		viewHolder.giardinoBtn.setOnClickListener(v -> {
			String nomeGiardinoCorrente = viewHolder.giardinoBtn.getText().toString();
			app.user.setNome_giardino_corrente(nomeGiardinoCorrente);
			DbUsers.updateNomeGiardinoCorrente(nomeGiardinoCorrente);
			Toolbar toolbar = fragView.findViewById(R.id.home_bl_toolbar);
			ImageButton imgButton = Utils.getToolbarNavigationButton(toolbar);
			(new Handler()).postDelayed(imgButton::performClick, 100);
			homeFragment.setupContent();  // FIXME !?
		});
		
		HomeGiardiniAdapter that = this;
		viewHolder.editBtn.setOnClickListener(v -> {
			InputDialog inputDialog = new InputDialog("Nome del giardino", nomeGiardino, context, newName -> {
				mData.set(i, newName);
				that.notifyItemChanged(i);
				String nomeGiardinoCorrente = app.user.getGiardinoCorrente().getNome();
				Log.d("GIARDINO", nomeGiardino + " - " + nomeGiardinoCorrente);
				if (nomeGiardino.equals(nomeGiardinoCorrente)) {
					((TextView) fragView.findViewById(R.id.home_fl_title_giardino)).setText(newName);
					nomeGiardinoCorrente = newName;
				}
				app.user.editNomeGiardino(nomeGiardino, newName);
				DbUsers.updateNomeGiardino(nomeGiardino, newName, nomeGiardinoCorrente);
			});
			inputDialog.show();
		});
	}
	
	// total number of rows
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	// stores and recycles views as they are scrolled off screen
	public static class ViewHolder extends RecyclerView.ViewHolder {
		Button giardinoBtn;
		Button editBtn;
		
		public ViewHolder(View itemView) {
			super(itemView);
			giardinoBtn = itemView.findViewById(R.id.drawer_button_text);
			editBtn = itemView.findViewById(R.id.drawer_button_edit);
		}
	}
	
}
