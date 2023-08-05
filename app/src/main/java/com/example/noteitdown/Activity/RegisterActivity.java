package com.example.noteitdown.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteitdown.Functions.ValidationFunctions;
import com.example.noteitdown.Model.User;
import com.example.noteitdown.R;
import com.example.noteitdown.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        EditText Name,Email,Password1,Password2;
        Name = binding.NameRegister;
        Email = binding.emailRegister;
        Password1 = binding.passwordRegister;
        Password2 = binding.confirmPasswordRegister;

        binding.SubmitButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidationFunctions.UserRegistrationValidation(Name,Email,Password1,Password2,getApplicationContext())){
                    auth.createUserWithEmailAndPassword(Email.getText().toString(),Password1.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = task.getResult().getUser();
                                User user = new User(firebaseUser.getUid(),Name.getText().toString(),Email.getText().toString(),Password1.getText().toString());
                                database.getReference().child("Users").child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"You have been Registered",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"User info not saved",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"Oops our servers could not be reached, please check if you are connected to the internet!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        binding.GoBackButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }
}