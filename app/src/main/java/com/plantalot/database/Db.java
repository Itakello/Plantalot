package com.plantalot.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;

public class Db {
	
	public static HashMap<String, String> icons;  // FIXME !!!!!
	
	public Db() {
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		DatabaseReference db = FirebaseDatabase.getInstance().getReference("ortomio");
		db.keepSynced(true);
		DatabaseReference dbRefIcons = FirebaseDatabase.getInstance().getReference("ortomio/icons");
		dbRefIcons.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot taskSnapshot) {
				icons = (HashMap<String, String>) taskSnapshot.getValue();
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e("firebase", "onCancelled " + error.getMessage());
			}
		});
		
		FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
				.setPersistenceEnabled(true)
				.build();
		FirebaseFirestore.getInstance().setFirestoreSettings(settings);
	}
	
}
