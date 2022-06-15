package com.plantalot.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Db {
	
	public static DatabaseReference mDatabase;  // FIXME !!!!!
	public static HashMap<String, String> icons;  // FIXME !!!!!
	
	public Db() {
		mDatabase = FirebaseDatabase.getInstance().getReference();
		DatabaseReference dbRefIcons = mDatabase.child("ortomio").child("icons");
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
	}
	
}
