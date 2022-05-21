package com.plantalot.classes;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Giardino extends LinkedHashMap<String, Orto> {

    public enum Color {
        GREEN, BLUE, RED, YELLOW, ORANGE, PURPLE, PINK,
    };

    String nome;
    int xPos;
    int yPos;
    int zPos;
//    Color c;

    public Giardino(String name, int xPos, int yPos, int zPos){
        this.nome = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    @NonNull
    @Override
    public String toString() {
        return nome + ": (" + xPos + " " + yPos + ")";// [" + c + "]";
    }
}
