package com.plantalot.database;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

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
import com.plantalot.classes.User;
import com.plantalot.fragments.HomeFragment;

public class DbUsers {

    private static final String TAG = "Users_DB";
    private static FirebaseAuth mAuth;
    private static User user;

    public static void init(@NonNull View view){
        mAuth = FirebaseAuth.getInstance();
//		mAuth.useEmulator("0.0.0.0", 9099);

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users/");
//		db.setPersistenceEnabled(true);
//		db.keepSynced(true);

        mAuth.signInAnonymously()
                .addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "Signed in anonymously");
                        String userUid = mAuth.getCurrentUser().getUid();

                        dbUsers.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Log.d(TAG, "User already stored in DB");
                                    user = snapshot.getValue(User.class);
                                }else{
                                    Log.d(TAG, "Generating new user in DB");
                                    user = new User("default","default@mail");
                                    dbUsers.child(userUid).setValue(user);
                                }
                                HomeFragment.updateUI(view, user);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w(TAG, "Error on checking existence user", error.toException());
                            }
                        });
                    }
                });
    }
}
