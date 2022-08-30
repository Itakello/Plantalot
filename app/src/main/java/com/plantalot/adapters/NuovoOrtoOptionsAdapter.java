package com.plantalot.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.plantalot.classes.Giardino;
import com.plantalot.components.InputDialog;
import com.plantalot.components.NuovoOrtoNumberSelector;
import com.plantalot.R;
import com.plantalot.classes.Orto;

import java.util.List;

import kotlin.Triple;

public class NuovoOrtoOptionsAdapter extends RecyclerView.Adapter<NuovoOrtoOptionsAdapter.ViewHolder> {
	
	private final String TAG = "NuovoOrtoOprionsAdapter";
	
	private final List<Triple<String, String, View>> mData;
	private final LayoutInflater mInflater;
	private final LinearLayout mExpanded;
	private final RecyclerView mRecycler;
	private final Context context;
	private final Orto orto;
	private final Giardino giardino;
	private String nomeOrto;
	private String error;
	
	public NuovoOrtoOptionsAdapter(Context context, List<Triple<String, String, View>> data, Giardino giardino, Orto orto, View view) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = data;
		this.giardino = giardino;
		this.orto = orto;
		this.mExpanded = view.findViewById(R.id.nuovo_orto_card_expanded);
		this.mRecycler = view.findViewById(R.id.nuovo_orto_options_recycler);
		this.nomeOrto = orto.getNome();
		this.error = null;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.nuovo_orto_option_row, parent, false);
		return new ViewHolder(view);
	}
	
	private void back_button_handler(View mainView, View press_target) {
		mainView.setFocusableInTouchMode(true);
		mainView.requestFocus();
		mainView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// Check if osBack key event
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
					// Check if card open
					if (mExpanded.getVisibility() == View.VISIBLE) {
						press_target.performClick();
						return true;
					}
				}
				return false;
			}
		});
	}
	
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		String field = mData.get(position).getFirst();
		String value = mData.get(position).getSecond();
		View mChild = mData.get(position).getThird();
		viewHolder.mField.setText(field);
		viewHolder.mValue.setText(value);
		
		// Add back button handler for
		if (mChild != null) {  // FIXME xxx
//			mChild.setFocusableInTouchMode(true);
//			mChild.requestFocus();
//			mChild.setOnKeyListener((v, keyCode, event) -> {
//				// Check if osBack key event
//				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//					// Check if card open
//					Log.i(TAG, "Pressed osBack with card open");
//					if (mExpanded.getVisibility() == View.VISIBLE) {
//						mExpanded.findViewById(R.id.nuovo_orto_card_back).performClick();
//						return true;
//					}
//				}
//				return false;
//			});
			View target = mExpanded.findViewById(R.id.nuovo_orto_card_back);
			back_button_handler(mChild, target);
		}
		
		viewHolder.mRow.setOnClickListener(v -> {
			
			if (position == 0) {  // FIXME !?
				
				InputDialog inputDialog = new InputDialog(context.getString(R.string.nome_orto), nomeOrto, context);
				
				inputDialog.setOnConfirm(newName -> {
					nomeOrto = newName;
					error = giardino.checkNomeOrto(orto.getNome(), newName, context);
					if (error == null) {
						orto.setNome(newName);
						viewHolder.mValue.setText(newName);
					} else {
						inputDialog.setError(error);
						inputDialog.show();
					}
				});
				
				inputDialog.setOnCancelListener(dialogInterface -> {
					error = null;
					nomeOrto = orto.getNome();
				});
				
				inputDialog.show();
				
			} else if (mChild != null) {
				
				mRecycler.setVisibility(View.INVISIBLE);
				mExpanded.setVisibility(View.VISIBLE);
				((TextView) mExpanded.findViewById(R.id.nuovo_orto_card_header)).setText(field);
				mExpanded.addView(mChild);
				mExpanded.findViewById(R.id.nuovo_orto_card_back).setOnClickListener(v1 -> close_card(mChild, viewHolder));
				
			} else {
				
				MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
				builder.setTitle("TODO");
				builder.setPositiveButton("OK", (dialog, i) -> dialog.cancel());
				builder.show();
				
			}
		});
		
	}
	
	
	private void close_card(View mChild, ViewHolder viewHolder) {
		mExpanded.setVisibility(View.INVISIBLE);
		mRecycler.setVisibility(View.VISIBLE);
		mExpanded.removeView(mChild);
		viewHolder.mValue.setText(((NuovoOrtoNumberSelector) mChild).getValues().toString());  // FIXME
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		View mRow;
		TextView mField, mValue;
		
		public ViewHolder(View itemView) {
			super(itemView);
			mRow = itemView.findViewById(R.id.nuovo_orto_option_row);
			mField = itemView.findViewById(R.id.nuovo_orto_option_field);
			mValue = itemView.findViewById(R.id.nuovo_orto_option_value);
		}
	}
	
}