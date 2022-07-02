package com.plantalot.database;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.fragments.HomeFragment;
import com.plantalot.utils.Consts;

import java.util.HashMap;
import java.util.Map;

public class DbUsers {

    private static final String TAG = "Users_DB";
    private static DatabaseReference dbUsers;

    public static void init(){
        dbUsers = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public static User writeNewUser(String username, String email){
        Log.d(TAG, "Writing new user");
        User user = new User(username, email);
        dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
        return user;
    }

    public static void updateGiardinoSelected(String nome_giardino){
        HashMap<String,Object> u_map = new HashMap<>();
        u_map.put("nome_giardino_selected", nome_giardino);
        dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(u_map);
    }

    public static void writeNewGiardino(String nome_giardino, LatLng location){
        Log.d(TAG, "Writing new giardino");
        HashMap<String,Object> g_map = new HashMap<>();
        g_map.put(nome_giardino, new Giardino(nome_giardino, location));
        DatabaseReference dbUser = dbUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbUser.child("giardini").updateChildren(g_map);
        HashMap<String,Object> u_map = new HashMap<>();
        u_map.put("nome_giardino_selected", nome_giardino);
        dbUser.updateChildren(u_map);
    }
}
