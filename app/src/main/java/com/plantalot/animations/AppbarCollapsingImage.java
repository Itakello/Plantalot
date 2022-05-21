package com.plantalot.animations;

import android.content.Context;
import android.util.AttributeSet;

import com.plantalot.R;

public class AppbarCollapsingImage extends AppbarCollapsingBehavior {
	
	public AppbarCollapsingImage(Context context, AttributeSet attrs) {
		super(context, attrs, R.styleable.CollapsingImageBehavior, R.styleable.CollapsingImageBehavior_collapsedImageTarget);
	}
}
