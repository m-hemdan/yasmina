package com.example.yasmina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btnLogin;
    EditText emailEditText, passwordEditText;
    ProgressBar progressBar;
    TextView registerNow;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerNow = (TextView) findViewById(R.id.register_now);
        btnLogin = (Button) findViewById(R.id.btn_login);
        emailEditText = (EditText) findViewById(R.id.et_email);
        passwordEditText = (EditText) findViewById(R.id.et_password);
        progressBar = (ProgressBar) findViewById(R.id.progressbarLogin);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAcount();
            }
        });
    }

    public void loginUserAcount() {
        progressBar.setVisibility(View.VISIBLE);
        final String email, password;
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
                            mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        String dataemail = dataSnapshot1.child("email").getValue().toString();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            progressBar.setVisibility(View.GONE);

                            Intent intent2 = new Intent();
                            String msg = intent2.getStringExtra("checkOut");
                            if (msg != "") {
                                Intent intent = new Intent(LoginActivity.this, checkOut.class);
                                 startActivity(intent);
                            } else {

                                Intent intent = new Intent(LoginActivity.this, book.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
