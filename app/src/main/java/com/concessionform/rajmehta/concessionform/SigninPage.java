package com.concessionform.rajmehta.concessionform;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SigninPage extends AppCompatActivity implements View.OnClickListener{

    private String tag = "YEP";
    private Button loginbutton;
    //private EditText emailfield;
    private EditText sapfield;
    private EditText passwordfield;
    private TextView alreadyreg;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener authStateListener;
    String userid;
    String email1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_page);

        firebaseAuth = FirebaseAuth.getInstance();
        //emailfield = findViewById(R.id.emailfield);
        sapfield = findViewById(R.id.sapfield);
        passwordfield = findViewById(R.id.passwordfield);
        alreadyreg = findViewById(R.id.alreadyreg);
        loginbutton = findViewById(R.id.loginbtn);
        progressDialog = new ProgressDialog(this);

        loginbutton.setOnClickListener(this);
        alreadyreg.setOnClickListener(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                }
            }
        };


    }

    private void userLogin(){
       //String email = emailfield.getText().toString().trim();
        final String sapid = sapfield.getText().toString();
        final String password = passwordfield.getText().toString().trim();

        if(TextUtils.isEmpty(sapid)){
            Toast.makeText(this,"Please enter sap ID", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        Query mdatabase = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("SAPID").equalTo(sapid);
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dschild : dataSnapshot.getChildren()){
                    userid = dschild.getKey();
                    email1 = dschild.child("email").getValue().toString();
                }

                progressDialog.setMessage("Logging In Please Wait...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email1,password)
                        .addOnCompleteListener(SigninPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v){
        if(v==loginbutton){
            userLogin();
        }
        if(v==alreadyreg){
            finish();
            startActivity(new Intent(this,LoginPage.class));
        }
    }
}
