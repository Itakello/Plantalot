package com.plantalot.classes;

import android.content.Context;

import com.plantalot.R;
import com.plantalot.database.DbPlants;
import com.plantalot.utils.IntPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Orto {
	
	public enum Esposizione {SOLE, MEZZOMBRA, OMBRA}
	
	public enum Orientamento {N, NE, E, SE, S, SO, O, NO}
	
	private String nome;
	private IntPair aiuoleDim;
	private IntPair aiuoleCount;
	private Esposizione esposizione;
	private Orientamento orientamento;
	private Carriola ortaggi;
	
	public Orto() {
		this.aiuoleDim = new IntPair(120, 200);
		this.aiuoleCount = new IntPair(3, 2);
		this.esposizione = Esposizione.SOLE;
		this.orientamento = Orientamento.N;
		this.ortaggi = new Carriola();
	}
	
	public Orto(String nome) {
		this();
		this.nome = nome;
	}
	
//	public Orto(String nome, IntPair aiuoleDim, IntPair aiuoleCount, Esposizione esposizione, Orientamento orientamento) {
//		this(nome);
//		this.aiuoleDim = aiuoleDim;
//		this.aiuoleCount = aiuoleCount;
//		this.esposizione = esposizione;
//		this.orientamento = orientamento;
//	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setAiuoleDim(int x, int y) {
		aiuoleDim.x = x;
		aiuoleDim.y = y;
	}
	
	public void setAiuoleCount(int x, int y) {
		aiuoleCount.x = x;
		aiuoleCount.y = y;
	}
	
	public void setEsposizione(Esposizione esposizione) {
		this.esposizione = esposizione;
	}
	
	public void setOrientamento(Orientamento orientamento) {
		this.orientamento = orientamento;
	}
	
	public String getNome() {
		return nome;
	}
	
	public IntPair getAiuoleDim() {
		return aiuoleDim;
	}
	
	public IntPair calcOrtoDim() {
		return new IntPair(aiuoleDim.x * aiuoleCount.x, aiuoleDim.y * aiuoleCount.y);
	}
	
	public int calcArea() {
		return calcOrtoDim().x * calcOrtoDim().y;
	}
	
	public int plantedArea() {
		return ortaggi.calcArea();
	}
	
	public IntPair getAiuoleCount() {
		return aiuoleCount;
	}
	
	public ArrayList<Integer> getImages() {  // TODO
		HashSet<Integer> imgSet = new HashSet<>();
		for (String ortaggio : ortaggi.nomiOrtaggi()) {
			imgSet.add(DbPlants.getImageId(ortaggio));
		}
		return new ArrayList<>(imgSet);
	}
	
	public Esposizione getEsposizione() {
		return esposizione;
	}
	
	public Orientamento getOrientamento() {
		return orientamento;
	}
	
	public Carriola getOrtaggi() {
		return ortaggi;
	}
	
	public void addVarieta(String ortaggio, String varieta, Integer count) {
		ortaggi.put(ortaggio, varieta, count);
	}
	
	public void fetchVarieta() {
		ortaggi.fetchVarieta();
	}
	
}
