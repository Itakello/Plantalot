package com.plantalot.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.card.MaterialCardView;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.Orto;
import com.plantalot.utils.Consts;
import com.plantalot.R;

public class HomeOrtiAdapter extends RecyclerView.Adapter<HomeOrtiAdapter.ViewHolder> {
	
	private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
	private final Giardino giardino;
	Context context;
	
	public HomeOrtiAdapter(Giardino g) {
		this.giardino = g;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.home_fl_card, viewGroup, false);
		return new ViewHolder(view);
	}
	
	
	// Bind elements to card
	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		
		Orto o = giardino.orti.get(i);
		String ortoName = o.getNome();
		int specie = o.piante.size();
		
		viewHolder.mCardView.setOnClickListener(view -> System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXx"));
		
		// Card popup menu
		viewHolder.buttonViewOption.setOnClickListener(view -> {
			PopupMenu popup = new PopupMenu(context, viewHolder.buttonViewOption);
			popup.inflate(R.menu.home_fl_card_menu);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				popup.setGravity(Gravity.END);
			}
			popup.setOnMenuItemClickListener(item -> {
				switch (item.getItemId()) {
//                    case R.id.menu1:
//                        //handle menu1 click
//                        return true;
//                    case R.id.menu2:
//                        //handle menu2 click
//                        return true;
//                    case R.id.menu3:
//                        //handle menu3 click
//                        return true;
					default:
						return false;
				}
			});
			popup.show();
		});
		
		// Icons thumbnails
		// needed to get the width (fixme ?)
		viewHolder.mFrameLayout.post(() -> {
			int width = viewHolder.mFrameLayout.getWidth();
			int padding = viewHolder.mFrameLayout.getPaddingTop();
			int imgwidth = (width - 2 * padding) / (Consts.CARD_PLANTS / 2);
			
			FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
			layoutManager.setJustifyContent(JustifyContent.CENTER);
			HomeThumbnailsAdapter homeThumbnailAdapter = new HomeThumbnailsAdapter(o.getImages(), imgwidth);
			
			viewHolder.mRecyclerView.setLayoutManager(layoutManager);
			viewHolder.mRecyclerView.setAdapter(homeThumbnailAdapter);
			viewHolder.mRecyclerView.setRecycledViewPool(viewPool);
			viewHolder.mRecyclerView.suppressLayout(true);  // ???? (google: android propagate click to parent recyclerview)
			
			ViewGroup.LayoutParams params = viewHolder.mFrameLayout.getLayoutParams();
			params.height = 2 * ((width - 2 * padding) / (Consts.CARD_PLANTS / 2) + padding);
			viewHolder.mFrameLayout.setLayoutParams(params);
			if (specie == 5 || specie == 6) {  // compact view
				viewHolder.mFrameLayout.setPadding(padding + imgwidth / 2, padding, padding + imgwidth / 2, padding);
			}
			
			viewHolder.titleTextView.setText(ortoName);
			viewHolder.labelTextView.setText(specie + " specie");
		});
	}
	
	@Override
	public int getItemCount() {
		return giardino.orti.size();
	}
	
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		// Attributes card
		private final TextView titleTextView;
		private final TextView labelTextView;
		private final RecyclerView mRecyclerView;
		private final FrameLayout mFrameLayout;
		private final View buttonViewOption;
		private final MaterialCardView mCardView;
		
		ViewHolder(final View view) {
			super(view);
			titleTextView = view.findViewById(R.id.home_fl_card_title__orto);
			labelTextView = view.findViewById(R.id.home_fl_card_label__varieta);
			mRecyclerView = view.findViewById(R.id.home_fl_recycler__ortaggi);
			mFrameLayout = view.findViewById(R.id.home_fl_layout__ortaggi);
			mCardView = view.findViewById(R.id.home_fl_card);
			buttonViewOption = view.findViewById(R.id.home_fl_card_button_menu);
		}
	}
	
}
