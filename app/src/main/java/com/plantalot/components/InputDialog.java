package com.plantalot.components;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.plantalot.R;
import com.plantalot.utils.Utils;

import java.util.function.Consumer;

public class InputDialog {
	
	private final MaterialAlertDialogBuilder builder;
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public InputDialog(String title, String text, Context context, Consumer<String> fun) {
		builder = new MaterialAlertDialogBuilder(context);
		builder.setTitle(title);
		
		EditText input = new EditText(context);
		input.setText(text);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		input.requestFocus();
		Utils.showKeyboard(context);
		
		FrameLayout inputLayout = new FrameLayout(context);
		int dp = Utils.dp2px(1, context);
		inputLayout.setPadding(24 * dp, 20 * dp, 24 * dp, 12 * dp);
		inputLayout.addView(input);
		builder.setView(inputLayout);
		
		builder.setPositiveButton(R.string.conferma, (dialog, which) -> {
			fun.accept(input.getText().toString());
			Utils.closeKeyboard(context);
		});
		
		builder.setNegativeButton(R.string.annulla, (dialog, which) -> {
			Utils.closeKeyboard(context);
			dialog.cancel();
		});
	}
	
	public void show() {
		builder.show();
	}
}
