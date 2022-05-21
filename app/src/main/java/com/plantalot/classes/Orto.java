package com.plantalot.classes;

import androidx.annotation.NonNull;

import com.plantalot.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Orto {
    final private String nome;
    final private int larghezza, altezza;
    final private int camminamenti_largezza, camminamenti_altezza;
    final private int colonne_aiuole, righe_aiuole;
    public ArrayList<Pianta> piante;
//    Esposizione
//    Orientamento

    public Orto(String nome, int larghezza, int altezza){
        this.nome = nome;
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.colonne_aiuole = 2;
        this.righe_aiuole = 2;
        this.camminamenti_altezza = 30;
        this.camminamenti_largezza = 30;

        // Inizializzazione statica
        piante = new ArrayList<>();
        for (int i=0; i<6; i++){
            piante.add(new Pianta("Carota", R.mipmap.plant_carrot_3944093));
        }
        System.out.println("Creato orto " + nome);
    }

    public ArrayList<Integer> getImages(){
        ArrayList<Integer> imgs = new ArrayList<>();
        for (Pianta p :
                piante) {
            imgs.add(p.image);
        }
        return imgs;
    }

    @NonNull
    @Override
    public String toString() {
        return nome + ": (" + larghezza + " " + altezza + ")";// [" + c + "]";
    }

    public String getNome() {
        return nome;
    }

    public int getLarghezza() {
        return larghezza;
    }

    public int getAltezza() {
        return altezza;
    }

    public int getCamminamenti_largezza() {
        return camminamenti_largezza;
    }

    public int getCamminamenti_altezza() {
        return camminamenti_altezza;
    }

    public int getColonne_aiuole() {
        return colonne_aiuole;
    }

    public int getRighe_aiuole() {
        return righe_aiuole;
    }

    int getAltezzaAiuole(){
        return (this.altezza / righe_aiuole) - (camminamenti_altezza * (righe_aiuole -1 ));
    }

    int getLargezzaAiuole(){
        return (this.larghezza / colonne_aiuole) - (camminamenti_largezza * (colonne_aiuole -1 ));
    }
}
