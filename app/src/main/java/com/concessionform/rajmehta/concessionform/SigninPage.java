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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

public class SigninPage extends AppCompatActivity implements View.OnClickListener{

    private Button loginbutton;
    private EditText emailfield;
    private EditText sapfield;
    private EditText passwordfield;
    private TextView alreadyreg;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_page);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),HomePage.class));
        }

        emailfield = findViewById(R.id.emailfield);
        sapfield = findViewById(R.id.sapfield);
        passwordfield = findViewById(R.id.passwordfield);
        alreadyreg = findViewById(R.id.alreadyreg);
        loginbutton = findViewById(R.id.loginbutton);
        progressDialog = new ProgressDialog(this);

        loginbutton.setOnClickListener(this);
        alreadyreg.setOnClickListener(this);

    }

    private void userLogin(){
        String email = emailfield.getText().toString().trim();
        String sapid = sapfield.getText().toString().trim();
        String password = passwordfield.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Logging In Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
