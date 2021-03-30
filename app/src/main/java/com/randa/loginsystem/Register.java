package com.randa.loginsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import MainClasses.Users;

public class Register extends AppCompatActivity {

    TextView emailLogin, passwordLogin, phoneLogin, usernameLogin;
    Button btnRegister;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String emailText,passwordText, phoneText, usernameText;
    Users user;
    ProgressBar progress;
     FirebaseDatabase database ;
    DatabaseReference ref ;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

          getIds();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }

        });

    }


    public void register (){
    emailText=emailLogin.getText().toString();
    usernameText=usernameLogin.getText().toString();
    phoneText=phoneLogin.getText().toString();
    passwordText=passwordLogin.getText().toString();

    if (usernameText.isEmpty()) {
        usernameLogin.setError("Set your User Name");
        usernameLogin.requestFocus();
        return;
    }else if (phoneText.isEmpty()) {
        phoneLogin.setError("Set your Phone");
        phoneLogin.requestFocus();
        return;
    } else if (passwordText.isEmpty()) {
        passwordLogin.setError("Set your Password");
        passwordLogin.requestFocus();
        return;
    }else if(emailText.isEmpty() ){
        emailLogin.setError("Provide your Email First");
        emailLogin.requestFocus() ;
        return;
    } else if (emailText.isEmpty() && passwordText.isEmpty() && phoneText.isEmpty() && usernameText.isEmpty()){
        Toast.makeText(Register.this,"Fields Empty !", Toast.LENGTH_LONG).show();
        return;
    }else if (!(emailText.isEmpty() &&passwordText.isEmpty()  && phoneText.isEmpty() && usernameText.isEmpty())){
        progress.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    currentUser=auth.getCurrentUser();
                    uid=currentUser.getUid();
                    ref=FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    user=new Users(usernameText,passwordText,emailText,phoneText);
                    ref.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progress.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this,"User has been registered Successfully !", Toast.LENGTH_LONG).show();
                                 Intent n= new Intent(Register.this,MainActivity.class);
                                 n.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(n);
                            } else {
                                Toast.makeText(Register.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(Register.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}

    public void getIds(){
    emailLogin=findViewById(R.id.email);
    passwordLogin=findViewById(R.id.password);
    phoneLogin=findViewById(R.id.phone);
    usernameLogin=findViewById(R.id.username);
        btnRegister=findViewById(R.id.btnRegister);
        progress=findViewById(R.id.progressBar);
    }

}