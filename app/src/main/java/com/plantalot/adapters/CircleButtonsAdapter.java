package com.plantalot.adapters;

import android.os.Bundle;
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
import com.plantalot.components.CircleButton;

import java.util.List;


public class CircleButtonsAdapter extends RecyclerView.Adapter<CircleButtonsAdapter.ViewHolder> {
	
	private final List<CircleButton> mData;
	
	public CircleButtonsAdapter(List<CircleButton> data) {
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
		int icon = mData.get(position).getIcon();
		String label = mData.get(position).getLabel();
		int idFragment = mData.get(position).getIdFragment();
		Bundle bundle = mData.get(position).getBundle();
//		int iconOn = mData.get(position).getIconOn();
//		int collection = mData.get(position).getCollection();
		
		viewHolder.mButton.setIconResource(icon);
		viewHolder.mTextView.setText(label);
		ViewGroup.LayoutParams params = viewHolder.mTextView.getLayoutParams();
		viewHolder.mTextView.setLayoutParams(params);
		if (idFragment != -1) {
			viewHolder.mCard.setOnClickListener(view -> Navigation.findNavController(view).navigate(idFragment, bundle));
		}
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		MaterialCardView mCard;
		MaterialButton mButton;
		TextView mTextView;
		
		public ViewHolder(View view) {
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
