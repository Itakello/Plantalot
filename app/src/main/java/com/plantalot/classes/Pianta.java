package com.plantalot.classes;

import java.util.ArrayList;

public class Pianta {
	
	private int concimazione_mensile;
	private String concimazione_organica;
	private int concimazione_trapianto;
	private ArrayList<String> consociazioni_pos;
	private int distanze_file;
	private int distanze_piante;
	private String famiglia;
	private String irrigazione_attecchimento;
	private int irrigazione_riduzione;
	private int irrigazione_sospensione;
	private String mezzombra;
	private String pianta;
	private int rotazioni_anni;
	private ArrayList<String> rotazioni_neg;
	private ArrayList<String> rotazioni_pos;
	
	
	public Pianta() {
	}
	
	
	public int getConcimazione_mensile() {
		return concimazione_mensile;
	}
	
	public String getConcimazione_organica() {
		return concimazione_organica;
	}
	
	public int getConcimazione_trapianto() {
		return concimazione_trapianto;
	}
	
	public ArrayList<String> getConsociazioni_pos() {
		return consociazioni_pos;
	}
	
	public int getDistanze_file() {
		return distanze_file;
	}
	
	public int getDistanze_piante() {
		return distanze_piante;
	}
	
	public String getFamiglia() {
		return famiglia;
	}
	
	public String getIrrigazione_attecchimento() {
		return irrigazione_attecchimento;
	}
	
	public int getIrrigazione_riduzione() {
		return irrigazione_riduzione;
	}
	
	public int getIrrigazione_sospensione() {
		return irrigazione_sospensione;
	}
	
	public String getMezzombra() {
		return mezzombra;
	}
	
	public String getPianta() {
		return pianta;
	}
	
	public int getRotazioni_anni() {
		return rotazioni_anni;
	}
	
	public ArrayList<String> getRotazioni_neg() {
		return rotazioni_neg;
	}
	
	public ArrayList<String> getRotazioni_pos() {
		return rotazioni_pos;
	}
	
}
