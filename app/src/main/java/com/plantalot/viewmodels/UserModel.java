package com.plantalot.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.plantalot.R;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.Orto;
import com.plantalot.classes.Pianta;
import com.plantalot.classes.User;

import java.util.ArrayList;

public class UserModel extends ViewModel {

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public LiveData<User> getUser(){
        return userLiveData;
    }

    public UserModel() {
        User u = new User("Marco");
        u.put("Belluno", new Giardino("Belluno", 0,0,0));
        u.put("Trento", new Giardino("Trento", 0, 0, 0));
        u.put("Bergamo", new Giardino("Bergamo",0,0,0));
        for (Giardino g :
                u.values()) {
            g.put("Orto 1", new Orto("Orto 1", 100, 100));
            g.put("Orto 2", new Orto("Orto 2", 100, 100));
            g.put("Orto 3", new Orto("Orto 3", 100, 100));
            for (Orto o :
                    g.values()) {
                o.add(new Pianta("Carota", R.mipmap.carrot_3944093));
            }
        }
        userLiveData.setValue(u);
    }

    public void initialize(){

    }
}
