package com.plantalot.classes;

import androidx.annotation.NonNull;

import java.util.LinkedHashMap;

public class Lawn extends LinkedHashMap<String, Garden> {

    public enum Color {
        GREEN, BLUE, RED, YELLOW, ORANGE, PURPLE, PINK,
    };

    String name;
    int xPos;
    int yPos;
    Color c;

    public Lawn(String name, int xPos, int yPos, Color c){
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.c = c;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": (" + xPos + " " + yPos + ") [" + c + "]";
    }
}
