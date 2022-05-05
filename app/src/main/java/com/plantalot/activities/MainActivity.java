package com.plantalot.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.plantalot.interfaces.NavigationHost;
import com.plantalot.R;
import com.plantalot.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationHost {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		// Activity never existed before
		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.container, new HomeFragment())
					.commit();
		}
	}
	
	/**
	 * Navigate to the given fragment.
	 *
	 * @param fragment       Fragment to navigate to.
	 * @param addToBackstack Whether or not the current fragment should be added to the backstack.
	 */
	@Override
	public void navigateTo(Fragment fragment, boolean addToBackstack) {
		FragmentTransaction transaction =
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.container, fragment);
		
		if (addToBackstack) {
			transaction.addToBackStack(null);
		}
		
		transaction.commit();
	}
}