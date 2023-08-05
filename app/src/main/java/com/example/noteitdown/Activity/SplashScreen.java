package com.example.noteitdown.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.noteitdown.Model.User;
import com.example.noteitdown.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(firebaseUser != null || User.initializedCloudState){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            },1500);
        }

        }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseUser != null || User.initializedCloudState){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}