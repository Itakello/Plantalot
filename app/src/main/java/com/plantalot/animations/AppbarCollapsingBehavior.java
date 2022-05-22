package com.plantalot.animations;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.plantalot.utils.ColorUtils;
import com.plantalot.R;


public class AppbarCollapsingBehavior extends CoordinatorLayout.Behavior<View> {
	
	protected final static int X = 0;
	protected final static int Y = 1;
	protected final static int WIDTH = 2;
	protected final static int HEIGHT = 3;
	
	protected int mTargetId;
	protected int[] mView;
	protected int[] mTarget;
	
	protected boolean isSetUp = false;
	protected CoordinatorLayout.LayoutParams lp;
	protected AppBarLayout appBarLayout;
	protected float factor;
	protected int range;
	
	protected final Context context;
	
	
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
	
	
	protected void setup(CoordinatorLayout parent, View child, View dependency) {
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
		
		mTarget[WIDTH] = target.getWidth() - (target.getPaddingStart() + target.getPaddingEnd());
		mTarget[HEIGHT] = target.getHeight() - (target.getPaddingTop() + target.getPaddingBottom());
		
		View view = target;
		while (view != parent) {
			mTarget[X] += (int) view.getX();
			mTarget[Y] += (int) view.getY();
			view = (View) view.getParent();
		}
		mTarget[X] += target.getPaddingStart();
		mTarget[Y] += target.getPaddingTop();
		
		lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
		isSetUp = true;
		appBarLayout = (AppBarLayout) dependency;
	}
	
	protected void tranform(View child) {
		range = appBarLayout.getTotalScrollRange();
		factor = -appBarLayout.getY() / (float) range;
		
		int left = mView[X] + (int) (factor * (mTarget[X] - mView[X]));
		int top = mView[Y] + (int) (factor * (mTarget[Y] - mView[Y]));
		int width = mView[WIDTH] + (int) (factor * (mTarget[WIDTH] - mView[WIDTH]));
		int height = mView[HEIGHT] + (int) (factor * (mTarget[HEIGHT] - mView[HEIGHT]));
		
		lp.width = width;
		lp.height = height;
		child.setLayoutParams(lp);
		child.setX(left);
		child.setY(top);
	}
	
}
