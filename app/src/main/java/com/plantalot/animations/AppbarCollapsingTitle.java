package com.plantalot.animations;

import android.content.Context;
import android.util.AttributeSet;

import com.plantalot.R;

public class AppbarCollapsingTitle extends AppbarCollapsingBehavior {
	
	public AppbarCollapsingTitle(Context context, AttributeSet attrs) {
		super(context, attrs, R.styleable.CollapsingTitleBehavior, R.styleable.CollapsingTitleBehavior_collapsedTitleTarget);
	}
	
}
