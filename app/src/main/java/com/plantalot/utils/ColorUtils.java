package com.plantalot.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by Hemant chand on 05/07/17.
 */

public class ColorUtils {
	
	// This default color int
	public static final int defaultColorID = android.R.color.black;
	public static final String defaultColor = "000000";
	public static final String TAG = "ColorTransparentUtils";
	
	/**
	 * This method convert numver into hexa number or we can say alphaparent code
	 *
	 * @param alpha number of transparency you want
	 * @return it return hex decimal number or transparency code
	 */
	public static String convert(int alpha) {
		String hexString = Integer.toHexString(Math.round(255 * alpha / 100));
		return (hexString.length() < 2 ? "0" : "") + hexString;
	}
	
	public static int alphaColor(int colorCode, int alpha) {
		return Color.parseColor(convertIntoColor(colorCode, alpha));
	}
		
	public static int attrColor(int attr, Context context, int alpha) {
		TypedValue typedValue = new TypedValue();
		context.getTheme().resolveAttribute(attr, typedValue, true);
		return alphaColor(typedValue.data, alpha);
	}
	
	public static int attrColor(int attr, Context context) {
		return attrColor(attr, context, 100);
	}
	
	
	/**
	 * Convert color code into alphaparent color code
	 *
	 * @param colorCode color code
	 * @param alphaCode alphaparent number
	 * @return alphaparent color code
	 */
	public static String convertIntoColor(int colorCode, int alphaCode) {
		// convert color code into hexa string and remove starting 2 digit
		
		String color = defaultColor;
		try {
			color = Integer.toHexString(colorCode).toUpperCase().substring(2);
		} catch (Exception ignored) {
		}
		
		if (!color.isEmpty() && alphaCode < 100) {
			if (color.trim().length() == 6) {
				return "#" + convert(alphaCode) + color;
			} else {
				Log.d(TAG, "Color is already with transparency");
				return convert(alphaCode) + color;
			}
		}
		// if color is empty or any other problem occur then we return deafult color;
		return "#" + Integer.toHexString(defaultColorID).toUpperCase().substring(2);
	}
}