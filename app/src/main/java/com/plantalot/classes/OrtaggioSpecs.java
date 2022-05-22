package com.plantalot.classes;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.plantalot.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrtaggioSpecs {
	
	// FIXME
	private final String title;
	private final String value;
	private final int icon;
	private final Boolean large;
	private SpannableString valueFormatted;
	
	public OrtaggioSpecs(String title, String value, int icon, Boolean large) {
		this.title = title;
		this.value = value;
		this.icon = icon;
		this.large = large;
	}
	
	public void bind(View view) {
		if (valueFormatted == null) {  // Add paragraph span
			String value = this.value.replaceAll("\n", "\n\n");
			valueFormatted = new SpannableString(value);
			Matcher matcher = Pattern.compile("\n\n").matcher(value);
			while (matcher.find()) {
				valueFormatted.setSpan(new AbsoluteSizeSpan(8, true), matcher.start() + 1, matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		
//		((TextView) view.findViewById(R.id.ortaggio_bl_specs_title)).setText(title);
		((TextView) view.findViewById(R.id.ortaggio_bl_specs_value)).setText(valueFormatted);
		((ImageView) view.findViewById(R.id.ortaggio_bl_specs_icon)).setImageResource(icon);
	}
	
	public Boolean isLarge() {
		return large;
	}
	
}
