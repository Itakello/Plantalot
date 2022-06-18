package com.plantalot.animations;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;

/**
 * {@link View.OnClickListener} used to translate the product grid sheet downward on
 * the Y-axis when the navigation icon in the toolbar is pressed.
 */
public class NavigationIconClickListener2 implements MenuItem.OnMenuItemClickListener {
	
	private final AnimatorSet animatorSet = new AnimatorSet();
	private final View sheet;
	private final Interpolator interpolator;
	private boolean backdropShown = false;
	private final int translateY;
	private View view;
	
	
	public NavigationIconClickListener2(
			Context context, View view, @Nullable Interpolator interpolator, int translateY) {
		this.view = view;
		this.sheet = view.findViewById(R.id.all_plants_backdrop_frontlayer);
		this.interpolator = interpolator;
		this.translateY = translateY;
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	}
	
	private void updateIcon(View view) {
//		if (openIcon != null && closeIcon != null) {
//			if (!(view instanceof ImageView)) {
//				throw new IllegalArgumentException("updateIcon() must be called on an ImageView");
//			}
//			((ImageView) view).setImageResource(backdropShown ? closeIcon : openIcon);
//
//		}
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem menuItem) {
		backdropShown = !backdropShown;
		
		// Cancel the existing com.plantalot.animations
		animatorSet.removeAllListeners();
		animatorSet.end();
		animatorSet.cancel();
		
		updateIcon(sheet);
		
		// Translation animation
		int interval = 300;
		int interval2 = interval-20;
		ObjectAnimator animator = ObjectAnimator.ofFloat(sheet, "translationY", backdropShown ? translateY : 0);
		animator.setDuration(interval);
		if (interpolator != null) {
			animator.setInterpolator(interpolator);
		}
		animatorSet.play(animator);
		animator.start();
		
		// Add bottom margin to RecyclerView to not crop the content
//		RecyclerView fl = sheet.findViewById(R.id.all_plants_fl_card_list_recycler);  // FIXME !!!!!!!!!!!
		RecyclerView fl = view.findViewById(R.id.all_plants_fl_card_list_recycler);
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fl.getLayoutParams();
		params.bottomMargin = backdropShown ? translateY : 0;
		fl.setLayoutParams(params);
		
		// FIXME double click
		if (backdropShown) {
			view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.VISIBLE);
		} else {
			Handler handler = new Handler();
			Runnable runnable = new Runnable() {
				public void run() {
					view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.INVISIBLE);
				}
			};
			handler.postAtTime(runnable, System.currentTimeMillis() + interval2);
			handler.postDelayed(runnable, interval2);
		}
		
		
		return false;
	}
}
