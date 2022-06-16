package com.plantalot.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.plantalot.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class User implements Parcelable {
    private String name;
    private String email;
    private String password;
    public ArrayList<Giardino> giardini;

    public User(String name){
        this.name = name;
        System.out.println("Creato un nuovo user: " + name);
        giardini = new ArrayList<>();
        giardini.add(new Giardino("Belluno", 0,0,0));
        giardini.add(new Giardino("Trento", 0, 0, 0));
        giardini.add(new Giardino("Bergamo",0,0,0));
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public List<String> getGiardiniNames(){
        List<String> names = new ArrayList<>();
        for (Giardino g :
                giardini) {
            names.add(g.getNome());
        }
        return names;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
    }

    public Giardino getGiardinoByName(String name){
        for (Giardino g :
                giardini) {
            if (g.getNome().equals(name))
                return g;
        }
        return null;
    }
}
