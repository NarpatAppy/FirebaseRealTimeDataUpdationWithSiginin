package com.example.appy_sales.databasestoreretrive;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appy_sales.databasestoreretrive.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView txtDetails;
    private EditText etName,etEmail,etContact;
    private Button btnSave,btnLogout;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private  String userId;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            }
        };

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.e("create","user function call outside if===================>");

                String userName = etName.getText().toString();
                String userEmail = etEmail.getText().toString();
                String userContact = etContact.getText().toString();
              if (TextUtils.isEmpty(userId)){
                    createUser(userName,userEmail,userContact);
                    userId=null;
                    Log.e("create","user function call inside if===================>");
                }

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }
    public void init(){
        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etContact = findViewById(R.id.contact);
        btnSave = findViewById(R.id.save);
        btnLogout =findViewById(R.id.logout);
        txtDetails = findViewById(R.id.dataView);
    }
    private  void createUser(String name,String email,String contact){
        if (TextUtils.isEmpty(userId)){
            userId=mFirebaseDatabase.push().getKey();
        }

        User user = new User(name,email,contact);
        mFirebaseDatabase.child(userId).setValue(user);
        addUserChangeListner();
    }
//user data change listner

    private void addUserChangeListner() {

        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user==null){
                    Log.e("ReadData","User Data is Null");
                    return;
                }
                Log.e("UserData","User Data is"+user.name+user.email+user.contact);

                txtDetails.setText("User Name: "+user.name + "\nEmail Id: "+user.email + "\nContact Number: "+ user.contact);
                etName.setText("");
                etEmail.setText("");
                etContact.setText("");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public  void logOut(){
        auth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

}
