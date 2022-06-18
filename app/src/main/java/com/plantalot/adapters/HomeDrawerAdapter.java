package com.plantalot.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;
import com.plantalot.fragments.HomeFragment;

import java.util.List;

// Cambia il contenuto del backlyer
public class HomeDrawerAdapter extends RecyclerView.Adapter<HomeDrawerAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final LayoutInflater mInflater;
	private final View fragView;
	
	// data is passed into the constructor
	public HomeDrawerAdapter(Context context, List<String> data, View fragView) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = data;
		this.fragView = fragView;
	}
	
	// inflates the row layout from xml when needed
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.home_bl_drawer_button, parent, false);
		return new ViewHolder(view);
	}
	
	// binds the data to the TextView in each row
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		String giardino = mData.get(position);
		holder.button.setText(giardino);
	}
	
	// total number of rows
	@Override
	public int getItemCount() {
		return mData.size();
	}

	private ImageButton getToolbarNavigationButton(Toolbar toolbar) {
		int size = toolbar.getChildCount();
		for (int i = 0; i < size; i++) {
			View child = toolbar.getChildAt(i);
			if (child instanceof ImageButton) {
				ImageButton btn = (ImageButton) child;
				if (btn.getDrawable() == toolbar.getNavigationIcon()) {
					return btn;
				}
			}
		}
		return null;
	}
	
	// stores and recycles views as they are scrolled off screen
	public class ViewHolder extends RecyclerView.ViewHolder {
		Button button;
		
		public ViewHolder(View itemView) {
			super(itemView);
			button = itemView.findViewById(R.id.drawer_button_text);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println("Hai premuto il pulsante " + button.getText().toString());
					HomeFragment.setUpGiardino(fragView, button.getText().toString());
					Toolbar toolbar = fragView.findViewById(R.id.home_bl_toolbar);
					ImageButton img_button = getToolbarNavigationButton(toolbar);

					// Add delay for smooth animation
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							img_button.performClick();
						}
					}, 300);


				}
			});
		}
	}
	
	// convenience method for getting data at click position
	String getItem(int id) {
		return mData.get(id);
	}
	
	// parent activity will implement this method to respond to click events
	public interface GardenClickListener {
		void onItemClick(View view, int position);
	}
	
}
