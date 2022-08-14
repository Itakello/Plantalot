package com.plantalot.classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;

public class Carriola {
	
	private final HashMap<String, HashMap<String, Integer>> map;
	
	public Carriola() {
		this.map = new HashMap<>();
	}
	
	public HashMap<String, HashMap<String, Integer>> getMap() {  // used only for the DB
		return map;
	}
	
	public Integer get(String ortaggio, String varieta) {
		return map.get(ortaggio).get(varieta);
	}
	
	public void remove(String ortaggio) {
		map.remove(ortaggio);
	}
	
	public void remove(String ortaggio, String varieta) {
		map.get(ortaggio).remove(varieta);
	}
	
	public void put(String ortaggio, String varieta, Integer count) {
		if (map.get(ortaggio) != null) map.get(ortaggio).put(varieta, count);
	}
	
	public void add(String ortaggio) {
		map.put(ortaggio, new HashMap<>());
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
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
	
}
