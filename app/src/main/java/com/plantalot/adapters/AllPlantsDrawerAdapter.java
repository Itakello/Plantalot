package com.plantalot.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.plantalot.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.List;
import java.util.Objects;

// Cambia il contenuto del backlyer
public class AllPlantsDrawerAdapter extends RecyclerView.Adapter<AllPlantsDrawerAdapter.ViewHolder> {
	
	private final List<Pair<String, List<String>>> mData;
	private final LayoutInflater mInflater;
	Context context;
	HashMap<String, List<String>> filters;
	
	// data is passed into the constructor
	public AllPlantsDrawerAdapter(Context context, List<Pair<String, List<String>>> data, HashMap<String, List<String>> filters) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = data;
		this.context = context;
		this.filters = filters;
	}
	
	// inflates the row layout from xml when needed
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.all_plants_bl_drawer_chips, parent, false);
		return new ViewHolder(view);
	}
	
	// binds the data to the TextView in each row
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		String title = mData.get(position).first;
		List<String> chips = mData.get(position).second;
		viewHolder.title.setText(title);
		filters.put(title, new ArrayList<>());
		if(Objects.equals(title, "Raggruppa")) {
			viewHolder.chipGroup.setSingleSelection(true);
			viewHolder.chipGroup.setSelectionRequired(true);
		}
		for (String c : chips) {
			if (c.isEmpty()) {
				viewHolder.chipGroup.addView(mInflater.inflate(R.layout.component_chips_divider, null, false));
			} else {
				Chip chip = new Chip(context);
				chip.setText(c);
				chip.setCheckable(true);
				chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
						if (filters.get(title).contains(c)) {
							filters.get(title).remove(c);
						} else {
							filters.get(title).add(c);
						}
					}
				});
				if (Objects.equals(title, "Raggruppa") && Objects.equals(c, "Famiglia")) {
					chip.setChecked(true);
				}
				viewHolder.chipGroup.addView(chip);
			}
		}
	}
	
	// total number of rows
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	
	// stores and recycles views as they are scrolled off screen
	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView title;
		ChipGroup chipGroup;
		
		public ViewHolder(View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.all_plants_bl_drawer_chips_title);
			chipGroup = itemView.findViewById(R.id.all_plants_bl_drawer_chips_chipgroup);
//			button.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					System.out.println("Hai premuto il pulsante " + button.getText());
//					Navigation.findNavController(v).navigate(R.id.action_select_giardino);
//				}
//			});
		}
	}
	
	// convenience method for getting data at click position
//	String getItem(int id) {
//		return mData.get(id);
//	}
	
	// parent activity will implement this method to respond to click events
//	public interface GardenClickListener {
//		void onItemClick(View view, int position);
//	}

}
