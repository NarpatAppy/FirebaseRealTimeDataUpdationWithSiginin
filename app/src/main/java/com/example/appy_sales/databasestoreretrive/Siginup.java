package com.example.appy_sales.databasestoreretrive;

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

public class Siginup extends AppCompatActivity {
    private EditText etEmail,etPassword;
    private Button btnSiginup;
    private TextView tvLogin;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siginup);
        init();
        btnSiginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Siginup.this, "Enter email address!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Siginup.this, "Enter Password!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(Siginup.this, "Password should be more than 6 character!", Toast.LENGTH_SHORT).show();
                    return;
                }


                //Creare new User
                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(Siginup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Siginup.this, "CreateUserWithEmail:onComplete:"+task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()){
                                    Toast.makeText(Siginup.this, "Authentication Failed"+task.getException(), Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Intent intent = new Intent(Siginup.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Siginup.this,Login.class);
                startActivity(i);
            }
        });

    }

    private void init() {
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.email);
        etPassword =findViewById(R.id.password);
        btnSiginup = findViewById(R.id.signup_button);
        tvLogin = findViewById(R.id.loginLink);
    }

}
