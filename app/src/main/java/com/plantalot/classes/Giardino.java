package com.plantalot.classes;

import androidx.annotation.NonNull;

import com.plantalot.R;
import com.plantalot.components.CircleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Giardino {

    public enum Color {
        GREEN, BLUE, RED, YELLOW, ORANGE, PURPLE, PINK,
    }

    private final String nome;
    private final int xPos;
    private final int yPos;
    private final int zPos;
    public ArrayList<Orto> orti;
//    Color c;

    public Giardino(String name, int xPos, int yPos, int zPos){
        this.nome = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;

        orti = new ArrayList<>();
        orti.add(new Orto("Orto 1", 100, 100));
        orti.add(new Orto("Orto 2", 100, 100));
        orti.add(new Orto("Orto 3", 100, 100));
    }

    @NonNull
    @Override
    public String toString() {
        return nome + ": (" + xPos + " " + yPos + ")";// [" + c + "]";
    }

    public String getNome() {
        return nome;
    }
}
