package com.plantalot.components;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.plantalot.adapters.CircleButtonsAdapter;

import java.util.List;

public class CircleButton {
	
	public static final int CARRIOLA = 0;
	public static final int PREFERITI = 0;
	
	private final String label;
	private final int icon;
	private final int idFragment;
	private final Bundle bundle;
//	private final int iconOn;
//	private final int collection;

	public CircleButton(String label, int icon){
		this(label, icon, -1, (Bundle) null);
	}

	public CircleButton(String label, int icon, int idFragment) {
		this(label, icon, idFragment, (Bundle) null);
	}
	
	public CircleButton(String label, int icon, int idFragment, Bundle bundle) {
		this.label = label;
		this.icon = icon;
		this.idFragment = idFragment;
		this.bundle = bundle;
//		this.iconOn = -1;
//		this.collection = -1;
	}

//	public CircleButton(String label, int icon, int iconOn, int collection) {
//		this.label = label;
//		this.icon = icon;
//		this.iconOn = iconOn;
//		this.idFragment = -1;
//		this.bundle = null;
//		this.collection = collection;
//	}
	
	public String getLabel() {
		return label;
	}
	
	public int getIcon() {
		return icon;
	}

	public int getIdFragment() {
		return idFragment;
	}

	public Bundle getBundle() {
		return bundle;
	}
	
//	public int getIconOn() {
//		return iconOn;
//	}
//
//	public int getCollection() {
//		return collection;
//	}
	
	public static void setupRecycler(List<CircleButton> mButtons, RecyclerView recyclerView, Context context) {
		CircleButtonsAdapter circleButtonsAdapter = new CircleButtonsAdapter(mButtons);
		FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
		flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
		recyclerView.setLayoutManager(flexboxLayoutManager);
		recyclerView.setAdapter(circleButtonsAdapter);
	}
	
}
