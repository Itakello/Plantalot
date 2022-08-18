package com.plantalot.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.Orto;
import com.plantalot.components.InputDialog;
import com.plantalot.database.DbUsers;
import com.plantalot.utils.Consts;
import com.plantalot.R;

import java.util.ArrayList;

public class HomeOrtiAdapter extends RecyclerView.Adapter<HomeOrtiAdapter.ViewHolder> {
	
	private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
	private final ArrayList<Orto> orti;
	private Giardino giardino;
	private Context context;
	
	public HomeOrtiAdapter(Giardino giardino) {
		this.giardino = giardino;
		this.orti = giardino.ortiList();
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.home_fl_card, viewGroup, false);
		return new ViewHolder(view);
	}
	
	
	// Bind elements to card
	@RequiresApi(api = Build.VERSION_CODES.N)
	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		
		Orto orto = orti.get(i);
		String ortoName = orto.getNome();
		int specie = orto.getOrtaggi().countSpecie();
		int piante = orto.getOrtaggi().countPiante();
		
		viewHolder.titleTextView.setText(ortoName);
		viewHolder.specieTextView.setText(specie + " specie");
		viewHolder.pianteTextView.setText(piante + " piante");
		
		// Card popup menu
		HomeOrtiAdapter that = this;
		viewHolder.buttonViewOption.setOnClickListener(view -> {
			PopupMenu popup = new PopupMenu(context, viewHolder.buttonViewOption);
			popup.inflate(R.menu.home_fl_card_menu);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				popup.setGravity(Gravity.END);
			}
			popup.setOnMenuItemClickListener(item -> {
				switch (item.getItemId()) {
					case R.id.home_card_menu_opt1:
						InputDialog inputDialog = new InputDialog("Nome dell'orto", ortoName, context, newName -> {
							giardino.editNomeOrto(orto, newName);
							that.notifyItemChanged(i);
							DbUsers.updateGiardino(giardino);
						});
						inputDialog.show();
						return true;
					
					case R.id.home_card_menu_opt2:
						MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
						builder.setTitle("Eliminare " + ortoName + "?");
						builder.setNegativeButton(R.string.annulla, (dialog, j) -> dialog.cancel());
						builder.setPositiveButton(R.string.conferma, (dialog, j) -> {
							dialog.cancel();
							giardino.removeOrto(orto);
							orti.remove(i);
							this.notifyItemRemoved(i);
							DbUsers.updateGiardino(giardino);
						});
						builder.show();
						return true;
					
					default:
						return false;
				}
			});
			popup.show();
		});
		
		// Icons thumbnails
		// needed to get the width (fixme ?)
		if (!orto.getOrtaggi().notEmpty()) {
			viewHolder.mFrameLayout.setVisibility(View.GONE);
		} else {
			viewHolder.mFrameLayout.setVisibility(View.VISIBLE);
			viewHolder.mFrameLayout.post(() -> {
				int width = viewHolder.mFrameLayout.getWidth();
				int padding = viewHolder.mFrameLayout.getPaddingTop();
				int imgwidth = (width - 2 * padding) / (Consts.CARD_PLANTS / 2);
				
				FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
				layoutManager.setJustifyContent(JustifyContent.CENTER);
				HomeThumbnailsAdapter homeThumbnailAdapter = new HomeThumbnailsAdapter(orto.getImages(), imgwidth);
				
				viewHolder.mRecyclerView.setLayoutManager(layoutManager);
				viewHolder.mRecyclerView.setAdapter(homeThumbnailAdapter);
				viewHolder.mRecyclerView.setRecycledViewPool(viewPool);
				viewHolder.mRecyclerView.suppressLayout(true);  // 🖕 (google: android propagate click to parent recyclerview)
				
				ViewGroup.LayoutParams params = viewHolder.mFrameLayout.getLayoutParams();
				params.height = 2 * ((width - 2 * padding) / (Consts.CARD_PLANTS / 2) + padding);
				viewHolder.mFrameLayout.setLayoutParams(params);
				if (specie == 5 || specie == 6) {  // compact view
					viewHolder.mFrameLayout.setPadding(padding + imgwidth / 2, padding, padding + imgwidth / 2, padding);
				}
			});
		}
	}
	
	@Override
	public int getItemCount() {
		return orti.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final TextView titleTextView;
		private final TextView specieTextView;
		private final TextView pianteTextView;
		private final RecyclerView mRecyclerView;
		private final FrameLayout mFrameLayout;
		private final View buttonViewOption;
		
		ViewHolder(final View view) {
			super(view);
			titleTextView = view.findViewById(R.id.home_fl_card_title__orto);
			specieTextView = view.findViewById(R.id.home_fl_card_label__specie);
			pianteTextView = view.findViewById(R.id.home_fl_card_label__piante);
			mRecyclerView = view.findViewById(R.id.home_fl_recycler__ortaggi);
			mFrameLayout = view.findViewById(R.id.home_fl_layout__ortaggi);
			buttonViewOption = view.findViewById(R.id.home_fl_card_button_menu);
		}
	}
	
}