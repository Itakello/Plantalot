package com.plantalot.components;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.plantalot.adapters.CircleButtonsAdapter;
import com.plantalot.R;

import java.util.List;

public class CircleButton {
	
	private String label;
	private int icon;
	
	public CircleButton(String label, int icon) {
		this.label = label;
		this.icon = icon;
	}
	
	public String getLabel() {
		return label;
	}
	
	public int getIcon() {
		return icon;
	}
	
	public static void setRecycler(List<CircleButton> mButtons, RecyclerView recyclerView, Context context) {
		CircleButtonsAdapter circleButtonsAdapter = new CircleButtonsAdapter(mButtons);
		FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
		flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
		recyclerView.setLayoutManager(flexboxLayoutManager);
		recyclerView.setAdapter(circleButtonsAdapter);
	}
	
}
