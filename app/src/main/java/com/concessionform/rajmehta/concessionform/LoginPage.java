package com.concessionform.rajmehta.concessionform;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private EditText emailfield;
    private EditText sapfield;
    private EditText passwordfield;
    private Button registerbutton;
    private TextView alreadyreg;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Boolean exists = false;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    String SAPID, email, password, uid;
    String TAG = "Worked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailfield = findViewById(R.id.emailfield);
        sapfield = findViewById(R.id.sapfield);
        passwordfield = findViewById(R.id.passwordfield);
        registerbutton = findViewById(R.id.registerbutton);
        alreadyreg = findViewById(R.id.alreadyreg);
        progressDialog = new ProgressDialog(this);
        registerbutton.setOnClickListener(this);
        alreadyreg.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        /*if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),HomePage.class));
        }*/
        /*authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                    finish();
                }
            }
        };*/
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

    private void registerUser(){

        Log.d(TAG,"Reached Inside Function");
        email = emailfield.getText().toString().trim();
        password = passwordfield.getText().toString().trim();
        SAPID = sapfield.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email ID", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter a password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(SAPID)){
            Toast.makeText(this,"Please enter a SAP ID", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG,"Reached Progresss");
        progressDialog.setMessage("Registering");
        progressDialog.show();
        Log.d(TAG,"Reached before query");
        Query mDatabase = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("SAPID").equalTo(SAPID);
        Log.d(TAG,"Reached after query");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"Reached beforefor");
                for (DataSnapshot dschild : dataSnapshot.getChildren()) {
                    Map<String, Object> model = (Map<String, Object>) dschild.getValue();
                    Log.d(TAG,"Reached after map");
                    if(model.get("SAPID").equals(SAPID)){
                        exists = true;
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });


                firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginPage.this,"Successfully Registered", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),UserDetails.class));
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
            Log.d(TAG, "Before function call");
            registerUser();
            Log.d(TAG, "After function call");

            uid = FirebaseAuth.getInstance().getUid();

            String userID = mDatabase.push().getKey();
            Toast.makeText(getApplicationContext(),userID,Toast.LENGTH_LONG).show();
            UserSAP user = new UserSAP(SAPID,email,password,uid);
            mDatabase.child(userID).setValue(user);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences.Editor editor = prefs.edit();

            editor.putString(SAPID,"SAPID");
            editor.putString(email,"email");
            editor.putString(password,"password");
            editor.putString(uid,"uid");
            editor.commit();

            startActivity(new Intent(getApplicationContext(),UserDetails.class));
        }

        if(v==alreadyreg){
            startActivity(new Intent(getApplicationContext(),SigninPage.class));
        }
    }
}
