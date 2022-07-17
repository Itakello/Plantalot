package com.plantalot.components;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.plantalot.R;

public class OrtoView extends LinearLayout {
	
	public OrtoView(Context context, int width, int height) {
		super(context);
		View view = inflate(context, R.layout.component_orto_border, this);
		View orto = view.findViewById(R.id.component_orto_border);
		LayoutParams layoutParams = (LayoutParams) orto.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		orto.setLayoutParams(layoutParams);
	}
}
