package com.plantalot.classes;

import android.content.Context;

import com.plantalot.R;
import com.plantalot.utils.IntPair;

import java.util.ArrayList;

public class Orto {
	
	public enum Esposizione {SOLE, MEZZOMBRA, OMBRA}
	
	public enum Orientamento {N, NE, E, SE, S, SO, O, NO}
	
	private String nome;
	private IntPair aiuoleDim;
	private IntPair aiuoleCount;
	private ArrayList<Pianta> piante;
	private Esposizione esposizione;
	private Orientamento orientamento;
	
	public Orto(Context context) {
		this.nome = context.getResources().getString(R.string.nuovo_orto);
		this.aiuoleDim = new IntPair(120, 200);
		this.aiuoleCount = new IntPair(3, 2);
		this.esposizione = Esposizione.SOLE;
		this.orientamento = Orientamento.N;
	}
	
	public Orto(Context context, String nome, IntPair aiuoleDim, IntPair aiuoleCount, Esposizione esposizione, Orientamento orientamento) {
		this(context);
		this.nome = nome;
		this.aiuoleDim = aiuoleDim;
		this.aiuoleCount = aiuoleCount;
		this.esposizione = esposizione;
		this.orientamento = orientamento;
	}
	
	
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
	
	public IntPair getAiuoleCount() {
		return aiuoleCount;
	}
	
	public ArrayList<Pianta> getPiante() {
		return piante;
	}
	
	public ArrayList<Integer> getImages() {  // TODO
		return null;
	}
	
	public Esposizione getEsposizione() {
		return esposizione;
	}
	
	public Orientamento getOrientamento() {
		return orientamento;
	}
}
