package com.plantalot.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.plantalot.R;
import com.plantalot.database.DbPlants;
import com.plantalot.database.DbUsers;

public class Splash extends Activity {
	
	private final static int SPLASH_DISPLAY_LENGTH = 1600;
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		DbPlants.init(this);
		DbUsers.init();
		
		new Handler().postDelayed(() -> {
			Intent mainIntent = new Intent(this, MainActivity.class);
			startActivity(mainIntent);
			finish();
		}, SPLASH_DISPLAY_LENGTH);
	}
}
