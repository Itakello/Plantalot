package com.plantalot.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.plantalot.R;
import com.plantalot.database.DbStrings;

public class MainActivity extends AppCompatActivity {//implements NavigationHost {

	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		mAuth = FirebaseAuth.getInstance();

	}

	@Override
	public void onStart() {
		super.onStart();
		// Check if user is signed in (non-null) and update UI accordingly.
		FirebaseUser currentUser = mAuth.getCurrentUser();
	}
	
	/**
	 * Navigate to the given fragment.
	 *
	 * @param fragment       Fragment to navigate to.
	 * @param addToBackstack Whether or not the current fragment should be added to the backstack.
	 */
//	@Override
//	public void navigateTo(Fragment fragment, boolean addToBackstack) {
//		FragmentTransaction transaction =
//				getSupportFragmentManager()
//						.beginTransaction()
//						.replace(R.id.container, fragment);
//
//		if (addToBackstack) {
//			transaction.addToBackStack(null);
//		}
//
//		transaction.commit();
//	}
}
