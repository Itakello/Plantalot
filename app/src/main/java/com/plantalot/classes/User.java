package com.plantalot.classes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class User extends LinkedHashMap<String, Giardino> {
    String name;
    String email;
    String password;
    public User(String name){
        this.name = name;
    }

    public List<String> getGiardini(){
        List<String> ret = new ArrayList<>(this.keySet());
        return ret;
    }
}
