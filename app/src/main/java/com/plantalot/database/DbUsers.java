package com.plantalot.database;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

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
    private static FirebaseAuth mAuth;
    private static User user;
    private static DatabaseReference dbUser;

    public static void init(@NonNull Activity activity){
        SharedPreferences sharedPref = activity.getSharedPreferences(Consts.PREF_FILENAME, Consts.PREF_MODE);
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "Signed in anonymously");
                        String userUid = mAuth.getCurrentUser().getUid();
                        dbUser = FirebaseDatabase.getInstance().getReference("users/"+userUid).getRef();

                        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Log.d(TAG, "User already stored in DB");
                                    user = snapshot.getValue(User.class);
                                }else{
                                    Log.d(TAG, "Generating new user in DB");
                                    writeNewUser("default_username", "default_email");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "Error on checking existence user", error.toException());
                            }
                        });
                    }
                });
    }

    private static void writeNewUser(String username, String email){
        user = new User(username, email);
        dbUser.setValue(user);
    }

    public static void writeNewGiardino(String nome_giardino, LatLng location){
        Log.d(TAG, "Writing new giardino");
        HashMap<String,Object> g_map = new HashMap<>();
        g_map.put(nome_giardino, new Giardino(nome_giardino, location));
        dbUser.child("giardini").updateChildren(g_map);
    }
}
