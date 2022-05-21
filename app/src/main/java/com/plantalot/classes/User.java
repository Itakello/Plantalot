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
    public LinkedHashMap<String, Giardino> giardini;

    public User(String name){
        this.name = name;
        System.out.println("Creato un nuovo user: " + name);
        giardini = new LinkedHashMap<>();
        giardini.put("Belluno", new Giardino("Belluno", 0,0,0));
        giardini.put("Trento", new Giardino("Trento", 0, 0, 0));
        giardini.put("Bergamo", new Giardino("Bergamo",0,0,0));
        giardini.put("Roma", new Giardino("Bergamo",0,0,0));
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
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

    public List<String> getGiardini(){
        return new ArrayList<>(giardini.keySet());
    }

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
}
