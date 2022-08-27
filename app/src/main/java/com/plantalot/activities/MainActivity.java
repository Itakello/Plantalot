package com.plantalot.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.plantalot.R;

public class MainActivity extends AppCompatActivity {

	private String TAG = "MainActivity";
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}

	private boolean doubleBackToExitPressedOnce = false;
	@Override
	public void onBackPressed() {
		//Checking for fragment count on backstack
		NavHostFragment navHostFragment =(NavHostFragment)getSupportFragmentManager()
				.findFragmentById(R.id.nav_host_fragment);
		NavController navController = navHostFragment.getNavController();
		Log.d(TAG, "Pressing back");
		if(!navController.popBackStack()) {
			if (!doubleBackToExitPressedOnce) {
				this.doubleBackToExitPressedOnce = true;
				Toast.makeText(this, R.string.exit_toast, Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
			} else {
				super.onBackPressed();
			}
		}
	}
}
