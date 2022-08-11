package com.plantalot.classes;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Giardino {

    public enum Color {
        GREEN, BLUE, RED, YELLOW, ORANGE, PURPLE, PINK,
    }

    private String name;
    LatLngGiardino pos;
    public ArrayList<Orto> orti;  // FIXME !!!!
//    Color c;

    public Giardino(){
        orti = new ArrayList<>();
    }

    public Giardino(String name, LatLng pos){
        this();
        this.name = name;
        this.pos = new LatLngGiardino(pos);
    }

    public String getName() {
        return name;
    }

    public LatLngGiardino getPos() {
        return pos;
    }

    public ArrayList<Orto> getOrti() {
        return orti;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("name", name);
        result.put("position", pos);
        result.put("orti", orti);
        return result;
    }
    
    public void addOrto(Orto orto) {
        orti.add(orto);
    }
}
