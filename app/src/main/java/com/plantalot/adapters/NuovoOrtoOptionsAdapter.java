package com.plantalot.adapters;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.plantalot.components.NuovoOrtoNumberSelector;
import com.plantalot.R;
import com.plantalot.classes.Orto;
import com.plantalot.utils.Utils;

import java.util.List;

import kotlin.Triple;

public class NuovoOrtoOptionsAdapter extends RecyclerView.Adapter<NuovoOrtoOptionsAdapter.ViewHolder> {
	
	private final String TAG = "NuovoOrtoOprionsAdapter";
	
	private final List<Triple<String, String, View>> mData;
	private final LayoutInflater mInflater;
	private final View mView;
	private final LinearLayout mExpanded;
	private final MaterialCardView mCard;
	private final RecyclerView mRecycler;
	private final Context context;
	private String nomeOrto;
	private Orto mOrto;
	
	public NuovoOrtoOptionsAdapter(Context context, List<Triple<String, String, View>> data, Orto orto, View view) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = data;
		this.mOrto = orto;
		this.mView = view;
		this.mCard = mView.findViewById(R.id.nuovo_orto_options_card);
		this.mExpanded = mView.findViewById(R.id.nuovo_orto_card_expanded);
		this.mRecycler = mView.findViewById(R.id.nuovo_orto_options_recycler);
		this.nomeOrto = orto.getNome();
	}
	
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
	
	
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		String field = mData.get(position).getFirst();
		String value = mData.get(position).getSecond();
		View mChild = mData.get(position).getThird();
		viewHolder.mField.setText(field);
		viewHolder.mValue.setText(value);
		
		// Add back button handler for
		if (mChild != null) {
			View target = mExpanded.findViewById(R.id.nuovo_orto_card_back);
			back_button_handler(mChild, target);
		}
		
		
		viewHolder.mRow.setOnClickListener(view -> {
			
			if (position == 0) {  // FIXME !?
				
				MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
				builder.setTitle(R.string.nome_orto);
				
				EditText input = new EditText(context);
				input.setText(nomeOrto);
				input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				input.requestFocus();
				Utils.showKeyboard(context);
				
				FrameLayout inputLayout = new FrameLayout(context);
				int dp = Utils.dp2px(1, context);
				inputLayout.setPadding(24 * dp, 20 * dp, 24 * dp, 12 * dp);
				inputLayout.addView(input);
				builder.setView(inputLayout);
				
				builder.setPositiveButton(R.string.conferma, (dialog, which) -> {
					nomeOrto = input.getText().toString();
					mOrto.setNome(nomeOrto);
					viewHolder.mValue.setText(nomeOrto);
					Utils.closeKeyboard(context);
				});
				builder.setNegativeButton(R.string.annulla, (dialog, which) -> {
					Utils.closeKeyboard(context);
					dialog.cancel();
				});
				
				builder.show();
				
			} else if (mChild != null) {
				
				mRecycler.setVisibility(View.INVISIBLE);
				mExpanded.setVisibility(View.VISIBLE);
				((TextView) mExpanded.findViewById(R.id.nuovo_orto_card_header)).setText(field);
				mExpanded.addView(mChild);
				mExpanded.findViewById(R.id.nuovo_orto_card_back).setOnClickListener(v -> close_card(mChild, viewHolder));
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
	
	public class ViewHolder extends RecyclerView.ViewHolder {
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