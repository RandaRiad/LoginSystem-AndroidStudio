package com.randa.loginsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import MainClasses.Users;

public class MainActivity extends AppCompatActivity {


    TextView emailLogin, passwordLogin;
    String emailText,passwordText;
    Button btnlogin;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference ref;
    ProgressBar progress;
    Users user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getIds();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                login();
            }

        });

    }



    public void login (){
        emailText=emailLogin.getText().toString();

        passwordText=passwordLogin.getText().toString();


         if (passwordText.isEmpty()) {
            passwordLogin.setError("Set your Password");
            passwordLogin.requestFocus();
            return;
        }else if(emailText.isEmpty() ){
            emailLogin.setError("Provide your Email First");
            emailLogin.requestFocus() ;
            return;
        } else if (emailText.isEmpty() && passwordText.isEmpty() ){
            Toast.makeText(MainActivity.this,"Fields Empty !", Toast.LENGTH_LONG).show();
            return;
        }else if (!(emailText.isEmpty() &&passwordText.isEmpty() )){
            progress.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                       Toast.makeText(MainActivity.this,"Welcome!", Toast.LENGTH_LONG).show();
                        Intent n= new Intent(MainActivity.this,HomePage.class);
                         n.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(n);
                                } else {
                                    Toast.makeText(MainActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
        }
    }

    public void getIds(){
        emailLogin=findViewById(R.id.emailLogin);
        passwordLogin=findViewById(R.id.passwordLogin);

        btnlogin=findViewById(R.id.btnLogin);
        progress=findViewById(R.id.progressBar2);
    }

    public  void signUp(View v){
        TextView register =findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(R.id.register,Register.class);

            }
        });

    }
    protected void goToActivity(int btn, Class act){
        View tx=findViewById(btn);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getBaseContext(), act));
            }
        });
    }
}