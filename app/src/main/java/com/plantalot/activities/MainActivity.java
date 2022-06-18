package com.plantalot.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.plantalot.R;
import com.plantalot.database.DbStrings;

public class MainActivity extends AppCompatActivity {//implements NavigationHost {
	private static final String TAG = "MainActivity";
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
		signInAnonymously();
	}

	private void signInAnonymously() {
//		showProgressBar();
		mAuth.signInAnonymously()
				.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInAnonymously:success");
							FirebaseUser user = mAuth.getCurrentUser();
//							updateUI(user);
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInAnonymously:failure", task.getException());
							Toast.makeText(getApplicationContext(), "Authentication failed.",
									Toast.LENGTH_SHORT).show();
//							updateUI(null);
						}

//						hideProgressBar();
					}
				});
	}

}
