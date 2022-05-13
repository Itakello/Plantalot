package com.plantalot.animations;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.plantalot.R;
import com.plantalot.utils.Utils;

public class AppbarCollapsingBehavior extends CoordinatorLayout.Behavior<View> {
	
	private final static int X = 0;
	private final static int Y = 1;
	private final static int WIDTH = 2;
	private final static int HEIGHT = 3;
	
	private static int MIN_H;
	private static int MAX_H;
	
	protected int mTargetId;
	private int[] mView;
	private int[] mTarget;
	
	private final Context context;
	
	public AppbarCollapsingBehavior(Context context, AttributeSet attrs, int[] collapsing, int collapsed) {
		this.context = context;
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, collapsing);
			mTargetId = a.getResourceId(collapsed, 0);
			a.recycle();
		}
		if (mTargetId == 0) {
			throw new IllegalStateException("collapsedTarget attribute not specified on view for behavior");
		}
	}
	
	
	@Override
	public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
		return dependency instanceof AppBarLayout;
	}
	
	@Override
	public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
		setup(parent, child);
		AppBarLayout appBarLayout = (AppBarLayout) dependency;
		
		int range = appBarLayout.getTotalScrollRange();
		float factor = -appBarLayout.getY() / range;
		
		int left = mView[X] + (int) (factor * (mTarget[X] - mView[X]));
		int top = mView[Y] + (int) (factor * (mTarget[Y] - mView[Y]));
		int width = mView[WIDTH] + (int) (factor * (mTarget[WIDTH] - mView[WIDTH]));
		int height = mView[HEIGHT] + (int) (factor * (mTarget[HEIGHT] - mView[HEIGHT]));
		
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
		lp.width = width;
		lp.height = height;
		child.setLayoutParams(lp);
		child.setX(left);
		child.setY(top);
		
		TextView subtitle1 = parent.findViewById(R.id.ortaggio_fl_toolbar_subtitle1);
		TextView subtitle2 = parent.findViewById(R.id.ortaggio_fl_toolbar_subtitle2);
		subtitle1.setAlpha((float) Math.min((MAX_H - MIN_H) / 2, height - MIN_H) / (float) ((MAX_H - MIN_H) / 2));
		subtitle2.setAlpha((float) Math.max(0, height - MIN_H - (MAX_H - MIN_H) / 2) / (float) ((MAX_H - MIN_H) / 2));
//		subtitle1.setText("h: " + MAX_H);
		
		View dropdown = parent.findViewById(R.id.ortaggio_fl_dropdown);
		dropdown.setElevation(Utils.dp2px(height == mTarget[HEIGHT] ? 4 : 0, context));
		
		return true;
	}
	
	protected void setup(CoordinatorLayout parent, View child) {
		if (mView != null) return;
		
		mView = new int[4];
		mTarget = new int[4];
		
		mView[X] = (int) child.getX();
		mView[Y] = (int) child.getY();
		mView[WIDTH] = child.getWidth();
		mView[HEIGHT] = child.getHeight();
		
		View target = parent.findViewById(mTargetId);
		if (target == null) {
			throw new IllegalStateException("target view not found");
		}
		
		mTarget[WIDTH] += target.getWidth();
		mTarget[HEIGHT] += target.getHeight();
		
		View view = target;
		while (view != parent) {
			mTarget[X] += (int) view.getX();
			mTarget[Y] += (int) view.getY();
			view = (View) view.getParent();
		}
		
		MAX_H = mView[HEIGHT];
		MIN_H = mView[HEIGHT] - mTarget[HEIGHT];
	}
	
}
