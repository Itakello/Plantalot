package com.plantalot.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.plantalot.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class User{// implements Parcelable {
    private final String TAG = "User";
    private String username;
    private String email;
    private int giardino_selected = -1;
    private static ArrayList<Giardino> giardini;

    public User(){
        giardini = new ArrayList<>();
    }

    public User(String username, String email){
        giardini = new ArrayList<>();
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Giardino getCurrentGiardino(){
        if(giardino_selected == -1)
            return null;
        return giardini.get(giardino_selected);
    }

    public Giardino getGiardinoByName(String name){
        for (int i=0; i<giardini.size(); i++) {
            Giardino g = giardini.get(i);
            if (g.getNome().equals(name)){
                giardino_selected = i;
                return g;
            }
        }
        return null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addGiardino(Giardino giardino) {
        giardini.add(giardino);
        giardino_selected = giardini.size()-1;
    }

    //    protected User(Parcel in) {
//        name = in.readString();
//        email = in.readString();
//        password = in.readString();
//    }

    public List<String> getGiardiniNames(){
        List<String> names = new ArrayList<>();
        for (Giardino g :
                giardini) {
            names.add(g.getNome());
        }
        return names;
    }

//    public static final Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(name);
//        dest.writeString(email);
//        dest.writeString(password);
//    }
}
