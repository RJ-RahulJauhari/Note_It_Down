package com.example.noteitdown.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteitdown.Functions.ValidationFunctions;
import com.example.noteitdown.Model.User;
import com.example.noteitdown.R;
import com.example.noteitdown.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        EditText Email = binding.EmailAddressLogin;
        EditText Password = binding.PasswordLogin;


        binding.SignInButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidationFunctions.UserLoginValidationPass(Email,Password,getApplicationContext())){
                    String EmailText = Email.getText().toString();
                    String PasswordText = Password.getText().toString();
                    auth.signInWithEmailAndPassword(EmailText,PasswordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = task.getResult().getUser();
                                database.getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User userinfo = snapshot.getValue(User.class);
                                        Toast.makeText(getApplicationContext(),"Welcome back "+userinfo.getName(),Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        User.CloudState = true;
                                        intent.putExtra("CURRENT_USER",user.getUid());
                                        startActivity(intent);
                                        finish();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(),"Oops!! Something went wrong: \n"+error.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"User does not Exist!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });



        binding.NoLoginButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.CloudState = false;
                User.initializedCloudState = true;
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                finish();
            }
        });

        binding.NoSignInIconLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.CloudState = false;
                User.initializedCloudState = true;
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null ){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}