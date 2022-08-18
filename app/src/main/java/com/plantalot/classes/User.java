package com.plantalot.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// FIXME un po' di casino nei metodi

public class User implements Serializable {
	private final String TAG = "User";
	
	private String username;
	private String email;
	private Map<String, Giardino> giardini;
	private String nome_giardino_corrente;
	
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
	
	public Giardino getGiardinoCorrente() {
		return giardini.get(getNome_giardino_corrente());
	}
	
	public Giardino getFirstGiardino() {
		if (giardini == null || giardini.size() == 0) return null;
		return giardini.entrySet().iterator().next().getValue();
	}
	
	public String getFirstGiardinoName() {
		if (giardini == null || giardini.size() == 0) return null;
		return giardini.entrySet().iterator().next().getKey();
	}
	
	public Map<String, Giardino> getGiardini() {
		return giardini;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void addGiardino(Giardino giardino) {
		giardini.put(giardino.getNome(), giardino);
	}
	
	public List<String> getGiardiniNames() {
		return new ArrayList<>(giardini.keySet());
	}
	
	public void setGiardini(Map<String, Giardino> giardini) {
		this.giardini = giardini;
	}
	
	public String getNome_giardino_corrente() {
		if (nome_giardino_corrente == null) return nome_giardino_corrente = getFirstGiardinoName();
		return nome_giardino_corrente;
	}
	
	public void setNome_giardino_corrente(String nomeGiardino) {
		this.nome_giardino_corrente = nomeGiardino;
	}
	
	public void editNomeGiardino(String oldName, String newName) {
		Giardino giardino = giardini.get(oldName);
		giardino.setNome(newName);
		giardini.put(newName, giardino);
		giardini.remove(oldName);
		if (nome_giardino_corrente.equals(oldName) || nome_giardino_corrente == null) {
			setNome_giardino_corrente(newName);
		}
	}
	
}
