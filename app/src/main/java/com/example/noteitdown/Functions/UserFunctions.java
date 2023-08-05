package com.example.noteitdown.Functions;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.noteitdown.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserFunctions {
    static FirebaseAuth auth;
    static FirebaseDatabase database;
    static User user;

    // Used to get the instance of the current user
    public static FirebaseUser getCurrentUser(){
        auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser();
    }

    public static User getUserData(Context context){
        if(getCurrentUser() == null){
            return null;
        }else{
            database = FirebaseDatabase.getInstance();
            database.getReference().child("Users").child(getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    UserFunctions.user = user;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        return UserFunctions.user;
    }

}
