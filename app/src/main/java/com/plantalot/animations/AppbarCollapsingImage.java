package com.plantalot.animations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.plantalot.R;

public class AppbarCollapsingImage extends AppbarCollapsingBehavior {
	
	public AppbarCollapsingImage(Context context, AttributeSet attrs) {
		super(context, attrs, R.styleable.CollapsingImageBehavior, R.styleable.CollapsingImageBehavior_collapsedImageTarget);
	}
	
	@Override
	public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
		if (!isSetUp) {
			setup(parent, child, dependency);
			mView[X] = (parent.getWidth() - mView[WIDTH]) / 2;
			child.setX(mView[X]);
		}
		tranform(child);
		return true;
	}
	
}
