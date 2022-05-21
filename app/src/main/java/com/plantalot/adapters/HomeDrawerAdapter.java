package com.plantalot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;

import java.util.List;

// Cambia il contenuto del backlyer
public class HomeDrawerAdapter extends RecyclerView.Adapter<HomeDrawerAdapter.ViewHolder> {
	
	private final List<String> mData;
	private final LayoutInflater mInflater;
	
	// data is passed into the constructor
	public HomeDrawerAdapter(Context context, List<String> data) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = data;
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


	// stores and recycles views as they are scrolled off screen
	public class ViewHolder extends RecyclerView.ViewHolder {
		Button button;

		public ViewHolder(View itemView) {
			super(itemView);
			button = itemView.findViewById(R.id.drawer_button_text);
			button.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					System.out.println("Hai premuto il pulsante " + button.getText());
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
