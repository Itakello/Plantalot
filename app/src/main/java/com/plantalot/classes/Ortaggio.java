package com.plantalot.classes;

import java.util.ArrayList;

public class Ortaggio {
	
	private double altro_f1;
	private int altro_pack;
	private double altro_perenne;
	private String altro_tollera_mezzombra;
	private String classificazione_ortaggio;
	private String classificazione_pianta;
	private int distanze_file;
	private int distanze_piante;
	private int distanze_piante_range;
	private int produzione_peso;
	private int produzione_range;
	private String produzione_udm;
	private int raccolta_avg;
	private int raccolta_max;
	private int raccolta_min;
	private String raccolta_udm;
	private String tassonomia_famiglia;
	private ArrayList<String> trapianti_mesi;
	
	
	public Ortaggio() {
	}
	
	
	public double getAltro_f1() {
		return altro_f1;
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
	
	public int getDistanze_file() {
		return distanze_file;
	}
	
	public int getDistanze_piante() {
		return distanze_piante;
	}
	
	public int getDistanze_piante_range() {
		return distanze_piante_range;
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
	
	public ArrayList<String> getTrapianti_mesi() {
		return trapianti_mesi;
	}
	
	
	public Object get(String field) {  // FIXME !!??
		switch (field) {
			case "altro_f1":
				return getAltro_f1();
			case "altro_pack":
				return getAltro_pack();
			case "altro_perenne":
				return getAltro_perenne();
			case "altro_tollera_mezzombra":
				return getAltro_tollera_mezzombra();
			case "classificazione_ortaggio":
				return getClassificazione_ortaggio();
			case "classificazione_pianta":
				return getClassificazione_pianta();
			case "distanze_file":
				return getDistanze_file();
			case "distanze_piante":
				return getDistanze_piante();
			case "distanze_piante_range":
				return getDistanze_piante_range();
			case "produzione_peso":
				return getProduzione_peso();
			case "produzione_range":
				return getProduzione_range();
			case "produzione_udm":
				return getProduzione_udm();
			case "raccolta_avg":
				return getRaccolta_avg();
			case "raccolta_max":
				return getRaccolta_max();
			case "raccolta_min":
				return getRaccolta_min();
			case "raccolta_udm":
				return getRaccolta_udm();
			case "tassonomia_famiglia":
				return getTassonomia_famiglia();
			case "trapianti_mesi":
				return getTrapianti_mesi();
		}
		return null;
	}
	
}
