package com.example.yasmina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button registerBtn;
    EditText nameEditText,emailEditText,passwordEditText,repasswordEditText;
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        registerBtn = (Button) findViewById(R.id.btn_register);
        nameEditText = (EditText) findViewById(R.id.et_name);
        emailEditText = (EditText) findViewById(R.id.et_email);
        passwordEditText = (EditText) findViewById(R.id.et_password);
        repasswordEditText = (EditText) findViewById(R.id.et_repassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFunction();
            }
        });

    }

    public void registerFunction()
    {
        progressBar.setVisibility(View.VISIBLE);
        final String name,email,password;
        name=nameEditText.getText().toString();
        email=emailEditText.getText().toString();
        password=passwordEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                             @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        NewUser user=new NewUser(email,name,password,firebaseUser.getUid());
                        mDatabase.child("UserData").child(firebaseUser.getUid()).push().setValue(user);
                        Intent intent = new Intent(RegisterActivity.this, book.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });



    }
   @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       // FirebaseUser currentUser = mAuth.getCurrentUser();
      //  updateUI(currentUser);
     FirebaseAuth.getInstance().signOut();
    }

    private void updateUI(FirebaseUser currentUser) {
    }

}
