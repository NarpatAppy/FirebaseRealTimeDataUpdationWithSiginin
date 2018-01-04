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

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private EditText etEmail,etPassword;
    private TextView tvSiginup;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get Firebase auth Instance;
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Intent i=new Intent(Login.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_login);
        init();

        tvSiginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Login.this, Siginup.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Enter email address!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter password!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //authenticate user
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()){
                                    if (password.length() <6){
                                        etPassword.setError(getString(R.string.minimum_password));
                                    }else {
                                        Toast.makeText(Login.this,"authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Intent i=new Intent(Login.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
    public void init(){
        auth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.emailid);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.log);
        tvSiginup =findViewById(R.id.siginupLink);
    }
}
