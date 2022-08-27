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
		Log.d(TAG, "Writing new user: " + username);
		User user = new User(username, email);
		dbAllUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
		return user;
	}
	
	public static void updateNomeGiardinoCorrente(String nomeGiardino) {
		Log.d(TAG, "Updating nome giardino corrente in " + nomeGiardino);
		HashMap<String, Object> u_map = new HashMap<>();
		u_map.put("nome_giardino_corrente", nomeGiardino);
		dbUser.updateChildren(u_map);
	}
	
	public static void updateGiardino(@NonNull Giardino giardino) {  // FIXME
		Log.d(TAG, "Updating giardino " + giardino.getNome());
		HashMap<String, Object> g_map = new HashMap<>();
		g_map.put(giardino.getNome(), giardino);
		dbUser.child("giardini").updateChildren(g_map);
	}

	public static void updateNomeGiardino(String oldName, String newName, String currentName) {  // FIXME
		Log.d(TAG, "Updating nome giardino from " + oldName + " to " + newName);
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
				if (oldName.equals(currentName))
					updateNomeGiardinoCorrente(newName);
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		});
	}

}
