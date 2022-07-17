package com.plantalot.components;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.plantalot.R;
import com.plantalot.fragments.NuovoOrtoFragment;
import com.plantalot.utils.IntPair;
import com.plantalot.utils.Utils;

public class NuovoOrtoNumberSelector extends LinearLayout {
	
	IntPair mValues;
	
	public NuovoOrtoNumberSelector(NuovoOrtoFragment parent, Context context, IntPair values, int minValue, int maxValue, int iconL, int iconR) {
		super(context);
		this.mValues = values;
		View view = inflate(context, R.layout.nuovo_orto_number_selector, this);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		NumberPicker pickerX = view.findViewById(R.id.nuovo_orto_picker_aiuola_dim_x);
		NumberPicker pickerY = view.findViewById(R.id.nuovo_orto_picker_aiuola_dim_y);
		ImageView imageL = view.findViewById(R.id.nuovo_orto_number_icon_left);
		ImageView imageR = view.findViewById(R.id.nuovo_orto_number_icon_right);
		
		pickerX.setMinValue(minValue);
		pickerX.setMaxValue(maxValue);
		pickerX.setValue(mValues.x);
		pickerX.setWrapSelectorWheel(false);
		
		pickerY.setMinValue(minValue);
		pickerY.setMaxValue(maxValue);
		pickerY.setValue(mValues.y);
		pickerY.setWrapSelectorWheel(false);
		
		imageL.setImageResource(iconL);
		imageR.setImageResource(iconR);
		
		pickerX.setOnValueChangedListener((numberPicker, i, j) -> {
			mValues.x = pickerX.getValue();
			parent.updateTable();
		});
		pickerY.setOnValueChangedListener((numberPicker, i, j) -> {
			mValues.y = pickerY.getValue();
			parent.updateTable();
		});
	}
	
	public IntPair getValues() {
		return mValues;
	}
}
