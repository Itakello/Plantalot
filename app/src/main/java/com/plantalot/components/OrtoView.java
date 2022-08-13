package com.plantalot.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.color.MaterialColors;
import com.plantalot.R;
import com.plantalot.classes.Orto;
import com.plantalot.utils.IntPair;


public class OrtoView extends LinearLayout {
	
	Orto orto;
	FrameLayout ortoView;
	Context context;
	
	public OrtoView(Context context, Orto orto, IntPair size) {
		super(context);
		this.context = context;
		this.orto = orto;
		View view = inflate(context, R.layout.component_orto_border, this);
		ortoView = view.findViewById(R.id.component_orto_border);
		setSize(size);
	}
	
	public void setSize(IntPair size) {
		int weight = (int) (6000f / (double) orto.getOrtoDim().getMax());
		LayoutParams params = (LayoutParams) ortoView.getLayoutParams();
		params.width = size.x;
		params.height = size.y;
		ortoView.setLayoutParams(params);
		View ortoBkg = ortoView.findViewById(R.id.component_orto_border_background);
		GradientDrawable bkg = (GradientDrawable) ortoBkg.getBackground();
		bkg.setColor(MaterialColors.harmonizeWithPrimary(context, Color.parseColor("#eeddcc")));  // FIXME colors
		bkg.setStroke(weight, MaterialColors.harmonizeWithPrimary(context, Color.parseColor("#603325")));  // FIXME colors
		bkg.setCornerRadius(weight);
		ortoView.setPadding(weight, weight, weight, weight);
	}
}
