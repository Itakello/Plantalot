package com.plantalot.classes;

import java.util.ArrayList;

public class Varieta implements Comparable<Varieta> {
	
	private double altro_f1;
	private double altro_mezzombra;
	private int altro_pack;
	private double altro_perenne;
	private String altro_tollera_mezzombra;
	private String classificazione_ortaggio;
	private String classificazione_pianta;
	private String classificazione_soprannome;
	private String classificazione_varieta;
	private int distanze_file;
	private int distanze_piante;
	private int distanze_piante_range;
	private String info_codice;
	private String info_descrizione;
	private int info_idvar;
	private String info_url;
	private int numero_varieta;
	private int produzione_peso;
	private int produzione_range;
	private String produzione_udm;
	private int raccolta_avg;
	private int raccolta_max;
	private int raccolta_min;
	private String raccolta_udm;
	private String tassonomia_famiglia;
	private String tassonomia_genere;
	private String tassonomia_specie;
	private String tassonomia_varieta;
	private ArrayList<Double> trapianti_mesi;
	
	
	public Varieta() {
	}
	
	
	public double getAltro_f1() {
		return altro_f1;
	}
	
	public double getAltro_mezzombra() {
		return altro_mezzombra;
	}
	
	public int getAltro_pack() {
		return altro_pack;
	}
	
	public double getAltro_perenne() {
		return altro_perenne;
	}
	
	public String getAltro_tollera_mezzombra() {
		return altro_tollera_mezzombra;
	}
	
	public String getClassificazione_ortaggio() {
		return classificazione_ortaggio;
	}
	
	public String getClassificazione_pianta() {
		return classificazione_pianta;
	}
	
	public String getClassificazione_soprannome() {
		return classificazione_soprannome;
	}
	
	public String getClassificazione_varieta() {
		return classificazione_varieta;
	}
	
	public int getDistanze_file() {
		return distanze_file;
	}
	
	public int getDistanze_piante() {
		return distanze_piante;
	}
	
	public int getDistanze_piante_range() {
		return distanze_piante_range;
	}
	
	public String getInfo_codice() {
		return info_codice;
	}
	
	public String getInfo_descrizione() {
		return info_descrizione;
	}
	
	public int getInfo_idvar() {
		return info_idvar;
	}
	
	public String getInfo_url() {
		return info_url;
	}
	
	public int getNumero_varieta() {
		return numero_varieta;
	}
	
	public int getProduzione_peso() {
		return produzione_peso;
	}
	
	public int getProduzione_range() {
		return produzione_range;
	}
	
	public String getProduzione_udm() {
		return produzione_udm;
	}
	
	public int getRaccolta_avg() {
		return raccolta_avg;
	}
	
	public int getRaccolta_max() {
		return raccolta_max;
	}
	
	public int getRaccolta_min() {
		return raccolta_min;
	}
	
	public String getRaccolta_udm() {
		return raccolta_udm;
	}
	
	public String getTassonomia_famiglia() {
		return tassonomia_famiglia;
	}
	
	public String getTassonomia_genere() {
		return tassonomia_genere;
	}
	
	public String getTassonomia_specie() {
		return tassonomia_specie;
	}
	
	public String getTassonomia_varieta() {
		return tassonomia_varieta;
	}
	
	public ArrayList<Double> getTrapianti_mesi() {
		return trapianti_mesi;
	}
	
	public int calcArea() {
		return distanze_file * distanze_piante;
	}
	
	
	@Override
	public int compareTo(Varieta varieta) {
		int compareOrtaggio = this.classificazione_ortaggio.compareTo(varieta.classificazione_ortaggio);
		if (compareOrtaggio != 0) return compareOrtaggio;
		return this.classificazione_varieta.compareTo(varieta.classificazione_varieta);
	}
}
