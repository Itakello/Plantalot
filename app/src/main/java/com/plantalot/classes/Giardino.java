package com.plantalot.classes;

import com.google.android.gms.maps.model.LatLng;
import com.plantalot.database.DbUsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Giardino {

//	public enum Color {  // TODO
//		GREEN, BLUE, RED, YELLOW, ORANGE, PURPLE, PINK,
//	}
	
	private String nome;
	private LatLngGiardino pos;
	private ArrayList<Orto> orti;  // FIXME !!!!
	private Carriola carriola;  // FIXME !!!!!
//    Color c;
	
	public Giardino() {
		orti = new ArrayList<>();
		carriola = new Carriola();
	}
	
	public Giardino(String nome, LatLng pos) {
		this();
		this.nome = nome;
		this.pos = new LatLngGiardino(pos);
	}
	
	public String getNome() {
		return nome;
	}
	
	public LatLngGiardino getPos() {
		return pos;
	}
	
	public ArrayList<Orto> getOrti() {
		return orti;
	}
	
	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("name", nome);
		result.put("position", pos);
		result.put("orti", orti);
		return result;
	}
	
	public int calcArea() {
		int area = 0;
		for (Orto orto : orti) area += orto.calcArea();
		return area;
	}
	
	public Carriola getCarriola() {
		return carriola;
	}

//	public void addOrto(Orto orto) {
//		orti.add(orto);
//	}

//	public void updateCarriola(Carriola carriola) {
//		this.carriola = carriola;
//	}
	
	public void update(Object obj, int method) {  // FIXME ??
		switch (method) {
			
			case DbUsers.UPDATE:
				if (obj instanceof Carriola) {
					carriola = (Carriola) obj;
				} else if (obj instanceof Orto) {
					orti.add((Orto) obj);
				}
				break;
			
			case DbUsers.DELETE:
				
				break;
		}
	}
	
}
