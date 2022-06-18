package com.plantalot.database;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.plantalot.R;
import com.plantalot.adapters.OrtaggioCardListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Db {
	
	public static HashMap<String, String> icons;  // FIXME !!!!!
	public static List<Pair<String, List<String>>> cards = new ArrayList();  // FIXME !!!!!
	public static List<String> famiglie = new ArrayList();  // FIXME !!!!!
	public static HashMap<String, Object> ortaggi;  // FIXME !!!!!
	
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
		
		FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
		FirebaseFirestore.getInstance().setFirestoreSettings(settings);
		FirebaseFirestore.getInstance().collection("ortomio").document("ortaggi").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					ortaggi = (HashMap<String, Object>) task.getResult().getData();
					HashMap<String, List<String>> famiglieMap = new HashMap<>();
					for (Object o : ortaggi.values()) {
						HashMap<String, Object> ortaggio = (HashMap) o;
						String famiglia = (String) ortaggio.get(DbStrings.VARIETA_TASSONOMIA_FAMIGLIA);
						if (famiglieMap.get(famiglia) == null) {
							famiglieMap.put(famiglia, new ArrayList<>());
							famiglie.add(famiglia);
						}
						famiglieMap.get(famiglia).add((String) ortaggio.get(DbStrings.VARIETA_CLASSIFICAZIONE_ORTAGGIO));
					}
					Collections.sort(famiglie);
					List<String> sorted = new ArrayList<>(famiglieMap.keySet());
					Collections.sort(sorted);
					sorted.add("Altro");
					famiglieMap.put("Altro", new ArrayList<>());
					for (String famiglia : sorted) {
						if (Objects.equals(famiglia, "Altro") || famiglieMap.get(famiglia).size() > 1) {
							Collections.sort(famiglieMap.get(famiglia));
							if (Objects.equals(famiglia, "Crucifere")) {
								famiglieMap.get(famiglia).remove("Altri cavoli");
								famiglieMap.get(famiglia).add("Altri cavoli");
							}
							cards.add(new Pair<>(famiglia, famiglieMap.get(famiglia)));
						} else {
							famiglieMap.get("Altro").add(famiglieMap.get(famiglia).get(0));
						}
					}
				} else {
					System.out.println("firebase ERROR ======================================================");
				}
			}
		});
	}
	
}
