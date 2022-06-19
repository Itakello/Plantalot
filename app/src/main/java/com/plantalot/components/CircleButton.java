package com.plantalot.components;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.plantalot.adapters.CircleButtonsAdapter;
import com.plantalot.R;

import java.util.List;

public class CircleButton {
	
	private String label;
	private int icon;
	private int id_fragment;
	private Bundle bundle = null;

	public CircleButton(String label, int icon){
		this(label, icon, -1);
	}

	public CircleButton(String label, int icon, int id_fragment) {
		this.label = label;
		this.icon = icon;
		this.id_fragment = id_fragment;
	}
	public CircleButton(String label, int icon, int id_fragment, Bundle bundle) {
		this(label, icon, id_fragment);
		this.bundle = bundle;
	}
	
	public String getLabel() {
		return label;
	}
	
	public int getIcon() {
		return icon;
	}

	public int getId_fragment() {
		return id_fragment;
	}

	public Bundle getBundle() {
		return bundle;
	}
	
	public static void setRecycler(List<CircleButton> mButtons, RecyclerView recyclerView, Context context) {
		CircleButtonsAdapter circleButtonsAdapter = new CircleButtonsAdapter(mButtons);
		FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
		flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
		recyclerView.setLayoutManager(flexboxLayoutManager);
		recyclerView.setAdapter(circleButtonsAdapter);
	}
	
}
