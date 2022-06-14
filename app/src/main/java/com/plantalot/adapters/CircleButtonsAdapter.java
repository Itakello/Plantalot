package com.plantalot.adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.plantalot.R;

import java.util.List;


// Navigation buttons in Home

public class CircleButtonsAdapter extends RecyclerView.Adapter<CircleButtonsAdapter.ViewHolder> {
	
	private final List<Pair<String, Integer>> mData;
	
	public CircleButtonsAdapter(List<Pair<String, Integer>> data) {
		this.mData = data;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_circle_button, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		int icon = mData.get(position).second;
		String label = mData.get(position).first;
		viewHolder.mButton.setIconResource(icon);
		viewHolder.mTextView.setText(label);
		ViewGroup.LayoutParams params = viewHolder.mTextView.getLayoutParams();
		viewHolder.mTextView.setLayoutParams(params);
		viewHolder.mCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Navigation.findNavController(v).navigate(R.id.action_ortaggio);
			}
		});
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		MaterialCardView mCard;
		MaterialButton mButton;
		TextView mTextView;
		
		ViewHolder(View view) {
			super(view);
			mCard = view.findViewById(R.id.component_circle_button);
			mButton = view.findViewById(R.id.component_circle_button_icon);
			mTextView = view.findViewById(R.id.component_circle_button_label);
			ViewGroup.LayoutParams lp = itemView.getLayoutParams();
			if (lp instanceof FlexboxLayoutManager.LayoutParams) {
				FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
				flexboxLp.setAlignSelf(AlignItems.CENTER);
			}
			
		}
	}
	
}
