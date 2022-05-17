package com.plantalot.animations;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.plantalot.utils.ColorUtils;
import com.plantalot.R;

public class AppbarCollapsingBehavior extends CoordinatorLayout.Behavior<View> {
	
	private final static int X = 0;
	private final static int Y = 1;
	private final static int WIDTH = 2;
	private final static int HEIGHT = 3;
	
	protected int mTargetId;
	private int[] mView;
	private int[] mTarget;
	
	private boolean isSetUp = false;

//	private float factorPrev = 0f;
	
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
	
	// FIXME animation not completed
	@Override
	public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
		if (!isSetUp) {
			setup(parent, child);
			isSetUp = true;
		}
		AppBarLayout appBarLayout = (AppBarLayout) dependency;
		
		int range = appBarLayout.getTotalScrollRange();
		float factor = -appBarLayout.getY() / (float) range;
		
		
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

//		if (factor < 0.5) {
//			factor = 0;
//		}
//		if (factor > 0.5) {
//			factor = 1;
//		}

//		int left;
//		int top;
//		int width;
//		int height;
//		float epsilon = 0.005f;

//		if (Math.abs(factor - factorPrev) > 0.5f) {
//			if (factor > factorPrev) {
//				left = mTarget[X];
//				top = mTarget[Y];
//				width = mTarget[WIDTH];
//				height = mTarget[HEIGHT];
//				System.out.println("---------------");
//			} else {
//				left = mView[X];
//				top = mView[Y];
//				width = mView[WIDTH];
//				height = mView[HEIGHT];
//				System.out.println("+++++++++++++++");
//			}
//		} else {
		
		int left = mView[X] + (int) (factor * (mTarget[X] - mView[X]));
		int top = mView[Y] + (int) (factor * (mTarget[Y] - mView[Y]));
		int width = mView[WIDTH] + (int) (factor * (mTarget[WIDTH] - mView[WIDTH]));
		int height = mView[HEIGHT] + (int) (factor * (mTarget[HEIGHT] - mView[HEIGHT]));
		
		System.out.println("" + left);
		
//		}
//		factorPrev = factor;
		
		lp.width = width;
		lp.height = height;
		child.setLayoutParams(lp);
		child.setX(left);
		child.setY(top);
		
		appBarLayout.setBackgroundColor(ColorUtils.attrColor(
				com.google.android.material.R.attr.colorPrimary,
				context,
				5 + (int) (11 * factor)
		));
		
		float alphaOffset = 0.5f;
		TextView subtitle = parent.findViewById(R.id.ortaggio_fl_appbar_subtitle);
		subtitle.setAlpha((float) Math.max(0, (factor - alphaOffset) * (1f / (1f - alphaOffset))));
		
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
		
		mTarget[WIDTH] = target.getWidth();
		mTarget[HEIGHT] = target.getHeight();
		
		View view = target;
		while (view != parent) {
			mTarget[X] += (int) view.getX();
			mTarget[Y] += (int) view.getY();
			view = (View) view.getParent();
		}
	}
	
}
