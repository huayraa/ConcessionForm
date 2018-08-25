package com.concessionform.rajmehta.concessionform;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private EditText emailfield;
    private EditText sapfield;
    private EditText passwordfield;
    private Button registerbutton;
    private TextView alreadyreg;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebaseAuth = FirebaseAuth.getInstance();
        emailfield = findViewById(R.id.emailfield);
        sapfield = findViewById(R.id.sapfield);
        passwordfield = findViewById(R.id.passwordfield);
        registerbutton = findViewById(R.id.registerbutton);
        alreadyreg = findViewById(R.id.alreadyreg);
        progressDialog = new ProgressDialog(this);
        registerbutton.setOnClickListener(this);
        alreadyreg.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),HomePage.class));
        }
    }

    private void registerUser(){

        String email = emailfield.getText().toString().trim();
        String password = passwordfield.getText().toString().trim();
        String sapid = sapfield.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email ID", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter a password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(sapid)){
            Toast.makeText(this,"Please enter a SAP ID", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginPage.this,"Successfully Registered", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),HomePage.class));
                        }else{
                            Toast.makeText(LoginPage.this,"Registration Unsuccessful",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v==registerbutton){
            registerUser();
        }

        if(v==alreadyreg){
            startActivity(new Intent(getApplicationContext(),SigninPage.class));
        }
    }
}
