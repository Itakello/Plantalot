package com.plantalot.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

public class Utils {
	
	public static int dp2px(int dp, @NonNull Context context) {
		return (int) (dp * context.getResources().getDisplayMetrics().density);
	}
	
	public static void hideSoftKeyboard(View view, Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public static double number(Object num) {  // FIXME
		switch (num.getClass().getName()) {
			case "java.lang.Double":
				return (Double) num;
			case "java.lang.Long":
				return ((Long) num).doubleValue();
		}
		return 0;
	}
	
}
