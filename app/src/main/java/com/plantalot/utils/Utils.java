package com.plantalot.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Field;


public class Utils {
	
	public static int dp2px(int dp, @NonNull Context context) {
		return (int) (dp * context.getResources().getDisplayMetrics().density);
	}
	
	public static ImageButton getToolbarNavigationButton(Toolbar toolbar) {
		int size = toolbar.getChildCount();
		for (int i = 0; i < size; i++) {
			View child = toolbar.getChildAt(i);
			if (child instanceof ImageButton) {
				ImageButton btn = (ImageButton) child;
				if (btn.getDrawable() == toolbar.getNavigationIcon()) {
					return btn;
				}
			}
		}
		return null;
	}
	
	
	public static void showKeyboard(Context context) {
		((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
	
	public static void hideKeyboard(Context context) {
		((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	
	public static int getResId(String resName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resName);
			return idField.getInt(idField);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
