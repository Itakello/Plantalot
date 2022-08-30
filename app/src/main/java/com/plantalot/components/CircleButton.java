package com.plantalot.components;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.plantalot.R;
import com.plantalot.adapters.CircleButtonsAdapter;
import com.plantalot.utils.Utils;

import java.util.List;

public class CircleButton {  // TODO extends View
	
	private final String label;
	private final int icon;
	private final String action;
	private final Bundle bundle;
	
	public CircleButton(String label, int icon) {
		this(label, icon, null, null);
	}
	
	public CircleButton(String label, int icon, String action) {
		this(label, icon, action, null);
	}
	
	public CircleButton(String label, int icon, String action, Bundle bundle) {
		this.label = label;
		this.icon = icon;
		this.action = action;
		this.bundle = bundle;
	}
	
	public String getLabel() {
		return label;
	}
	
	public int getIcon() {
		return icon;
	}
	
	public String getAction() {
		return action;
	}
	
	public Bundle getBundle() {
		return bundle;
	}
	
	public static void setupRecycler(List<CircleButton> mButtons, RecyclerView recyclerView, Context context) {
		CircleButtonsAdapter circleButtonsAdapter = new CircleButtonsAdapter(mButtons);
		FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
		flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
		recyclerView.setLayoutManager(flexboxLayoutManager);
		recyclerView.setAdapter(circleButtonsAdapter);
	}
	
}
