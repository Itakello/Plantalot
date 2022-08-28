package com.plantalot.components;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.plantalot.R;
import com.plantalot.utils.Utils;

import java.util.function.Consumer;

public class InputDialog {
	
	private final MaterialAlertDialogBuilder builder;
	private final Context context;
	private final EditText input;
	private final FrameLayout inputLayout;
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public InputDialog(String title, String text, Context context) {
		this.context = context;
		int dp = Utils.dp2px(1, context);
		
		input = new EditText(context);
		input.setText(text);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		
		inputLayout = new FrameLayout(context);
		inputLayout.setPadding(24 * dp, 20 * dp, 24 * dp, 12 * dp);
		inputLayout.addView(input);
		
		builder = new MaterialAlertDialogBuilder(context)
				.setTitle(title)
				.setView(inputLayout)
				.setNegativeButton(R.string.annulla, (dialog, which) -> dialog.cancel())
				.setOnDismissListener(dialogInterface -> Utils.hideKeyboard(context))
				.setOnCancelListener(dialogInterface -> Utils.hideKeyboard(context));
	}
	
	public void show() {
		Utils.showKeyboard(context);
		input.requestFocus();
		builder.show();
	}
	
	public void setError(String error) {
		if (error != null) {
			if (inputLayout.getParent() != null) {
				((ViewGroup) inputLayout.getParent()).removeView(inputLayout);
			}
			inputLayout.removeView(input);
			input.setError(error);
			inputLayout.addView(input);
			builder.setView(inputLayout);
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public void setOnConfirm(Consumer<String> fun) {
		builder.setPositiveButton(R.string.conferma, (dialog, which) -> fun.accept(input.getText().toString().trim()));
	}
	
	public void setOnCancelListener(@Nullable DialogInterface.OnCancelListener onCancelListener) {
		builder.setOnCancelListener(onCancelListener);
	}
}
