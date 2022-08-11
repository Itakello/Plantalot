package com.plantalot.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.plantalot.components.NuovoOrtoNumberSelector;
import com.plantalot.R;
import com.plantalot.classes.Orto;
import com.plantalot.utils.Utils;

import java.util.List;

import kotlin.Triple;

public class NuovoOrtoOptionsAdapter extends RecyclerView.Adapter<NuovoOrtoOptionsAdapter.ViewHolder> {
	
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
	
	
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		String field = mData.get(position).getFirst();
		String value = mData.get(position).getSecond();
		View mChild = mData.get(position).getThird();
		viewHolder.mField.setText(field);
		viewHolder.mValue.setText(value);
		viewHolder.mRow.setOnClickListener(view -> {
			if (position == 0) {  // FIXME !?
				MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
				builder.setTitle(R.string.nome_orto);
				
				EditText input = new EditText(context);
				input.setText(nomeOrto);
				input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				input.requestFocus();
				showKeyboard();
				
				FrameLayout inputLayout = new FrameLayout(context);
				int dp = Utils.dp2px(1, context);
				inputLayout.setPadding(24 * dp, 20 * dp, 24 * dp, 12 * dp);
				inputLayout.addView(input);
				builder.setView(inputLayout);
				
				builder.setPositiveButton(R.string.conferma, (dialog, which) -> {
					nomeOrto = input.getText().toString();
					mOrto.setNome(nomeOrto);
					viewHolder.mValue.setText(nomeOrto);
					closeKeyboard();
				});
				builder.setNegativeButton(R.string.annulla, (dialog, which) -> {
					closeKeyboard();
					dialog.cancel();
				});
				
				builder.show();
			} else if (mChild != null) {
				mRecycler.setVisibility(View.INVISIBLE);
				mExpanded.setVisibility(View.VISIBLE);
				((TextView) mExpanded.findViewById(R.id.nuovo_orto_card_header)).setText(field);
				mExpanded.addView(mChild);
				mExpanded.findViewById(R.id.nuovo_orto_card_back).setOnClickListener(v -> {
					mExpanded.setVisibility(View.INVISIBLE);
					mRecycler.setVisibility(View.VISIBLE);
					mExpanded.removeView(mChild);
					viewHolder.mValue.setText(((NuovoOrtoNumberSelector) mChild).getValues().toString());  // FIXME
				});
			} else {
				MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
				builder.setTitle("TODO");
				builder.setPositiveButton("OK", (dialog, i) -> dialog.cancel());
				builder.show();
			}
		});
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
	
	public void showKeyboard() {
		((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
	
	public void closeKeyboard() {
		((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	
}
