package com.plantalot.classes;

import android.util.Pair;

import com.plantalot.database.Db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
	private final String TAG = "User";
	private String username;
	private String email;
	private Map<String, Giardino> giardini;
	
	public static HashMap<String, HashMap<String, Integer>> carriola = new HashMap<>();  // FIXME !!!!!
	
//	static {
//		carriola.put("Barbabietola", new HashMap<>());
//		carriola.put("Peperoncino", new HashMap<>());
//		carriola.put("Aglio", new HashMap<>());
//		carriola.put("Zucca", new HashMap<>());
//		carriola.put("Bietola", new HashMap<>());
//
//		carriola.get("Barbabietola").put("Detroit", 6);
//		carriola.get("Peperoncino").put("Fatalii", 1);
//		carriola.get("Peperoncino").put("Habanero arancione", 2);
//		carriola.get("Aglio").put("Rosso", 12);
//		carriola.get("Zucca").put("Delica tonda", 4);
//		carriola.get("Bietola").put("Multicolore", 6);
//	}
	
	public User() {
		giardini = new HashMap<>();
	}
	
	public User(String username, String email) {
		this();
		this.username = username;
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public Giardino getGiardino(String nome) {
		return giardini.get(nome);
	}
	
	public Giardino getFirstGiardino() {
		if (giardini.size() == 0)
			return null;
		return giardini.entrySet().iterator().next().getValue();
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void addGiardino(Giardino giardino) {
		giardini.put(giardino.getName(), giardino);
	}
	
	public List<String> getGiardiniNames() {
		return new ArrayList<>(giardini.keySet());
	}
	
	public Map<String, Giardino> getGiardini() {
		return giardini;
	}
	
}
