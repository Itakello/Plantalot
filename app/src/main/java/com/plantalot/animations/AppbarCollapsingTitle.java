package com.plantalot.animations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.elevation.SurfaceColors;
import com.plantalot.R;
import com.plantalot.utils.ColorUtils;

public class AppbarCollapsingTitle extends AppbarCollapsingBehavior {
	
	public AppbarCollapsingTitle(Context context, AttributeSet attrs) {
		super(context, attrs, R.styleable.CollapsingTitleBehavior, R.styleable.CollapsingTitleBehavior_collapsedTitleTarget);
	}
	
	@Override
	public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
		if (!isSetUp) {
			setup(parent, child, dependency);
		}
		
		tranform(child);

//		appBarLayout.setBackgroundColor(SurfaceColors.SURFACE_1.getColor(context));
		appBarLayout.setBackgroundColor(ColorUtils.attrColor(
				com.google.android.material.R.attr.colorPrimary,
				context,
				5 + (int) (11 * factor)
		));
		
		float alphaOffset = 0.5f;
		TextView subtitle = parent.findViewById(R.id.ortaggio_fl_appbar_subtitle);
		subtitle.setAlpha(Math.max(0f, (factor - alphaOffset) * (1f / (1f - alphaOffset))));
		
		return true;
	}
}
