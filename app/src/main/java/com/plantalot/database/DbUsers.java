package com.plantalot.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.Orto;
import com.plantalot.classes.User;

import java.util.HashMap;


public class DbUsers {
	
	private static final String TAG = "Users_DB";
	private static DatabaseReference dbUsers;
	private static DatabaseReference dbUser;
	
	public static void init() {
		dbUsers = FirebaseDatabase.getInstance().getReference().child("users");
		dbUser = dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
	}
	
	public static User writeNewUser(String username, String email) {
		Log.d(TAG, "Writing new user");
		User user = new User(username, email);
		dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
		return user;
	}
	
	public static void updateGiardinoCorrente(String nomeGiardino) {
		HashMap<String, Object> u_map = new HashMap<>();
		u_map.put("nome_giardino_corrente", nomeGiardino);
		dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(u_map);
	}
	
	public static void writeNewGiardino(String nomeGiardino, LatLng location) {
		Log.d(TAG, "Writing new giardino " + nomeGiardino);
		HashMap<String, Object> g_map = new HashMap<>();
		g_map.put(nomeGiardino, new Giardino(nomeGiardino, location));
		dbUser.child("giardini").updateChildren(g_map);
		HashMap<String, Object> u_map = new HashMap<>();
		u_map.put("nome_giardino_corrente", nomeGiardino);
		dbUser.updateChildren(u_map);
	}
	
	public static void updateGiardino(Giardino giardino) {
		Log.d(TAG, "Updating giardino " + giardino.getNome());
		HashMap<String, Object> g_map = new HashMap<>();
		g_map.put(giardino.getNome(), giardino);
		dbUser.child("giardini").updateChildren(g_map);
	}
	
	public static void addOrto(Orto orto) {
		Log.d(TAG, "Adding orto " + orto.getNome());
		dbUser.child("nome_giardino_corrente").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				String nome_giardino_corrente = (String) snapshot.getValue();
				
				dbUser.child("giardini").child(nome_giardino_corrente).addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot snapshot) {
						Giardino giardino = snapshot.getValue(Giardino.class);
						giardino.addOrto(orto);
						HashMap<String, Object> g_map = new HashMap<>();
						g_map.put(giardino.getNome(), giardino);
						dbUser.child("giardini").updateChildren(g_map);
					}
					
					@Override
					public void onCancelled(@NonNull DatabaseError error) {
					}
				});
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		});
		
	}
}
