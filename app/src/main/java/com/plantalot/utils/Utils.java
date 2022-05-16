package com.plantalot.utils;

import android.content.Context;

import androidx.annotation.NonNull;

public class Utils {
	
	public static int dp2px(int dp, @NonNull Context context) {
		return (int) (dp * context.getResources().getDisplayMetrics().density);
	}
	
}
