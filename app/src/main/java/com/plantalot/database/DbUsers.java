package com.plantalot.database;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;

import java.util.HashMap;


public class DbUsers {
	
	private static final String TAG = "Users_DB";
	private static DatabaseReference dbUsers;
	
	public static void init() {
		dbUsers = FirebaseDatabase.getInstance().getReference().child("users");
	}
	
	public static User writeNewUser(String username, String email) {
		Log.d(TAG, "Writing new user");
		User user = new User(username, email);
		dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
		return user;
	}
	
	public static void updateGiardinoCorrente(String nomeGiardino) {
		HashMap<String, Object> u_map = new HashMap<>();
		u_map.put("nome_giardino_selected", nomeGiardino);
		dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(u_map);
	}
	
	public static void writeNewGiardino(String nomeGiardino, LatLng location) {
		Log.d(TAG, "Writing new giardino");
		HashMap<String, Object> g_map = new HashMap<>();
		g_map.put(nomeGiardino, new Giardino(nomeGiardino, location));
		DatabaseReference dbUser = dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		dbUser.child("giardini").updateChildren(g_map);
		HashMap<String, Object> u_map = new HashMap<>();
		u_map.put("nome_giardino_selected", nomeGiardino);
		dbUser.updateChildren(u_map);
	}
}
