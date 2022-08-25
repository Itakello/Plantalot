package com.plantalot.activities;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.plantalot.R;

public class MainActivity extends AppCompatActivity {
	
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
//		NavHostFragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
//		NavController navController = navCon
//		if (navController.getBackQueue().size() > 0) {
		if(true){
			getSupportFragmentManager().popBackStack();
		} else if (!doubleBackToExitPressedOnce) {
			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, R.string.exit_toast, Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					doubleBackToExitPressedOnce = false;
				}
			}, 2000);
		} else {
			super.onBackPressed();
			return;
		}
	}
}
