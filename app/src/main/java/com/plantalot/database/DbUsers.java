package com.plantalot.database;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;

import java.util.HashMap;


public class DbUsers {
	
	private static final String TAG = "Users_DB";
	private static DatabaseReference dbAllUsers;
	private static DatabaseReference dbUser;
	
	public static void init() {
		dbAllUsers = FirebaseDatabase.getInstance().getReference().child("users");
		dbUser = dbAllUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
	}
	
	@NonNull
	public static User writeNewUser(String username, String email) {
		Log.d(TAG, "Writing new user");
		User user = new User(username, email);
		dbAllUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
		return user;
	}
	
	public static void updateNomeGiardinoCorrente(String nomeGiardino) {
		HashMap<String, Object> u_map = new HashMap<>();
		u_map.put("nome_giardino_corrente", nomeGiardino);
		dbUser.updateChildren(u_map);
	}
	
//	public static void addNewGiardino(String nomeGiardino, LatLng location) {
//		Log.d(TAG, "Writing new giardino " + nomeGiardino);
//		HashMap<String, Object> g_map = new HashMap<>();
//		g_map.put(nomeGiardino, new Giardino(nomeGiardino, location));
//		dbUser.child("giardini").updateChildren(g_map);
//		HashMap<String, Object> u_map = new HashMap<>();
//		u_map.put("nome_giardino_corrente", nomeGiardino);
//		dbUser.updateChildren(u_map);
//	}
	
	public static void updateGiardino(@NonNull Giardino giardino) {  // FIXME
		Log.d(TAG, "Updating giardino " + giardino.getNome());
		HashMap<String, Object> g_map = new HashMap<>();
		g_map.put(giardino.getNome(), giardino);
		dbUser.child("giardini").updateChildren(g_map);
	}

//	public static void updateGiardino(String nomeGiardino) {  // FIXME
//		Log.d(TAG, "Updating giardino " + nomeGiardino);
//		dbUser.child("giardini").child(nomeGiardino).addListenerForSingleValueEvent(new ValueEventListener() {
//			@Override
//			@RequiresApi(api = Build.VERSION_CODES.N)
//			public void onDataChange(@NonNull DataSnapshot snapshot) {
//				Giardino giardino = snapshot.getValue(Giardino.class);
//				HashMap<String, Object> g_map = new HashMap<>();
//				g_map.put(nomeGiardino, giardino);
//				dbUser.child("giardini").updateChildren(g_map);
//			}
//
//			@Override
//			public void onCancelled(@NonNull DatabaseError error) {
//			}
//		});
//	}
	
	public static void editNomeGiardino(String oldName, String newName, String currentName) {  // FIXME
		Log.d(TAG, "Updating giardino " + oldName);
		dbUser.child("giardini").child(oldName).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			@RequiresApi(api = Build.VERSION_CODES.N)
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				Giardino giardino = snapshot.getValue(Giardino.class);
				giardino.setNome(newName);
				HashMap<String, Object> g_map = new HashMap<>();
				g_map.put(newName, giardino);
				g_map.put(oldName, null);
				dbUser.child("giardini").updateChildren(g_map);
				if (oldName.equals(currentName)) updateNomeGiardinoCorrente(newName);
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		});
	}

//	public static void _updateGiardinoCorrente(Object obj, Giardino.Method method) {  // FIXME put nome_giardino_corrente in a Bundle !!!!!!!!!!!!!!!!??
//		Log.d(TAG, "Updating giardino corrente");
//		dbUser.child("nome_giardino_corrente").addListenerForSingleValueEvent(new ValueEventListener() {
//			@Override
//			public void onDataChange(@NonNull DataSnapshot snapshot) {
//				String nome_giardino_corrente = (String) snapshot.getValue();
//				dbUser.child("giardini").child(nome_giardino_corrente).addListenerForSingleValueEvent(new ValueEventListener() {
//					@Override
//					@RequiresApi(api = Build.VERSION_CODES.N)
//					public void onDataChange(@NonNull DataSnapshot snapshot) {
//						Giardino giardino = snapshot.getValue(Giardino.class);
//						giardino.update(obj, method);  // FIXME ??
//						HashMap<String, Object> g_map = new HashMap<>();
//						g_map.put(giardino.getNome(), giardino);
//						dbUser.child("giardini").updateChildren(g_map);
//					}
//
//					@Override
//					public void onCancelled(@NonNull DatabaseError error) {
//					}
//				});
//			}
//
//			@Override
//			public void onCancelled(@NonNull DatabaseError error) {
//			}
//		});
//	}

}
