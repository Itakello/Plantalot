package com.plantalot.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.plantalot.MyApplication;
import com.plantalot.R;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.database.DbPlants;
import com.plantalot.database.DbUsers;

public class Splash extends Activity {
	
	private final static int SPLASH_DISPLAY_LENGTH = 0;
	private final String TAG = "Splash";
	private MyApplication app;
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash);
		app = (MyApplication) this.getApplication();
		DbPlants.init(this);
		
		FirebaseAuth mAuth = FirebaseAuth.getInstance();
		FirebaseUser currentUser = mAuth.getCurrentUser();

		if (currentUser == null) {  // User not signed in
			mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
				Log.d(TAG, "Signed in anonymously");
				getUserFromFirebase(mAuth.getCurrentUser());
			});
		} else {  // User signed in
			getUserFromFirebase(currentUser);
		}
		// Disk persistence user -> firebase
		FirebaseDatabase.getInstance().getReference("users/" + mAuth.getUid()).keepSynced(true);
		// Disk persistence ortomio -> firestore
		FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
				.setPersistenceEnabled(true)
				.build();
		FirebaseFirestore.getInstance().setFirestoreSettings(settings);
	}
	
	private void getUserFromFirebase(FirebaseUser firebaseUser) {
		DbUsers.init();
		String uid = firebaseUser.getUid();
		DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
		DatabaseReference userRef = rootRef.child("users").child(uid);
		userRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (!snapshot.exists()) {
					app.user = DbUsers.writeNewUser("default_username", "default_email");
				} else {
					app.user = snapshot.getValue(User.class);
					if (app.user.getGiardinoCorrente() != null) app.user.getGiardinoCorrente().fetchVarieta();  // FIXME wait
				}
				new Handler().postDelayed(Splash.this::startMainActivity, SPLASH_DISPLAY_LENGTH);
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e(TAG, "Error on checking existence user", error.toException());
			}
		});
	}
	
	private void startMainActivity() {
		Intent mainIntent = new Intent(this, MainActivity.class);
		startActivity(mainIntent);
		finish();
	}
	
}
