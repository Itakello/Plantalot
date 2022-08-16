package com.plantalot.classes;

import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.plantalot.database.DbPlants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Carriola {
	
	private final HashMap<String, HashMap<String, Integer>> map;
	private final HashMap<String, Varieta> varietaMap;
	
	public Carriola() {
		this.map = new HashMap<>();
		this.varietaMap = new HashMap<>();
	}
	
	public HashMap<String, HashMap<String, Integer>> getMap() {  // used only for the DB
		return map;
	}
	
	public Varieta getVarieta(String ortaggio, String varieta) {
		return varietaMap.get(ortaggio + varieta);
	}
	
	public ArrayList<Varieta> varietaList() {
		return new ArrayList<>(varietaMap.values());
	}
	
	public int getPianteCount(String ortaggio, String varieta) {
		return map.get(ortaggio).get(varieta);
	}
	
	public void remove(String ortaggio, String varieta) {
		if (map.get(ortaggio) != null) {
			map.get(ortaggio).remove(varieta);
			if (isEmpty(ortaggio)) map.remove(ortaggio);
		}
	}
	
	public void clear() {
		map.clear();
	}
	
	public void put(String ortaggio, String varieta, Integer count) {
		if (map.get(ortaggio) == null) map.put(ortaggio, new HashMap<>());
		map.get(ortaggio).put(varieta, count);
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		db.collection("varieta")
				.whereEqualTo(DbPlants.VARIETA_CLASSIFICAZIONE_ORTAGGIO, ortaggio)
				.whereEqualTo(DbPlants.VARIETA_CLASSIFICAZIONE_VARIETA, varieta)
				.get().addOnSuccessListener(queryDocumentSnapshots -> {
					Varieta varietaObj = queryDocumentSnapshots.iterator().next().toObject(Varieta.class);
					varietaMap.put(ortaggio + varieta, varietaObj);
				});
	}
	
	public void fetchVarieta() {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		for (String ortaggio : nomiOrtaggi()) {
			List<String> varietaList = nomiVarieta(ortaggio);
			db.collection("varieta")
					.whereEqualTo(DbPlants.VARIETA_CLASSIFICAZIONE_ORTAGGIO, ortaggio)
					.whereIn(DbPlants.VARIETA_CLASSIFICAZIONE_VARIETA, varietaList)
					.get().addOnSuccessListener(queryDocumentSnapshots -> {
						for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
							Varieta varietaObj = document.toObject(Varieta.class);
							varietaMap.put(ortaggio + varietaObj.getClassificazione_varieta(), varietaObj);
						}
					});
		}
	}
	
	public boolean notEmpty() {
		return !map.isEmpty();
	}
	
	public boolean isEmpty(String ortaggio) {
		return map.get(ortaggio).isEmpty();
	}
	
	public ArrayList<String> nomiOrtaggi() {
		return new ArrayList<>(map.keySet());
	}
	
	public ArrayList<String> nomiVarieta(String ortaggio) {
		return new ArrayList<>(map.get(ortaggio).keySet());
	}
	
	public int countVarieta(String ortaggio) {
		return map.get(ortaggio).size();
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public int countPiante(String ortaggio) {
		return (new ArrayList<>(map.get(ortaggio).values())).stream().mapToInt(Integer::intValue).sum();
	}
	
	public boolean contains(String ortaggio) {
		return map.get(ortaggio) != null;
	}
	
	public boolean contains(String ortaggio, String varieta) {
		return this.contains(ortaggio) && map.get(ortaggio).get(varieta) != null;
	}
	
	public int calcArea() {
		int area = 0;
		for (Varieta varieta : varietaMap.values()) {
			area += varieta.calcArea() * getPianteCount(
					varieta.getClassificazione_ortaggio(),
					varieta.getClassificazione_varieta());
		}
		Log.d("AREAAAAAAAAAAAAAAAAAAA", ""+ area);
		return area;
	}
	
	public List<Pair<String, List<Pair<Varieta, Integer>>>> toList() {
		HashMap<String, List<Pair<Varieta, Integer>>> carriolaMap = new HashMap<>();
		for (Varieta varieta : varietaList()) {
			String ortaggioName = varieta.getClassificazione_ortaggio();
			String varietaName = varieta.getClassificazione_varieta();
			int pianteCount = getPianteCount(ortaggioName, varietaName);
			if (carriolaMap.get(ortaggioName) == null) {
				carriolaMap.put(ortaggioName, new ArrayList<>());
			}
			carriolaMap.get(ortaggioName).add(new Pair<>(varieta, pianteCount));
		}
		List<Pair<String, List<Pair<Varieta, Integer>>>> carriolaList = new ArrayList<>();
		List<String> ortaggiNames = new ArrayList<>(carriolaMap.keySet());
		Collections.sort(ortaggiNames);
		for (String ortaggio : ortaggiNames) {
			carriolaList.add(new Pair<>(ortaggio, carriolaMap.get(ortaggio)));
		}
		return carriolaList;
	}
	
}
