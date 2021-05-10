package com.cburdeyron.questions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;

    TextInputLayout champEmail, champMotDePasse;
    Button btnConnexion, btnMotDePasseOublie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnexion = findViewById(R.id.btnConnexion);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                champEmail = findViewById(R.id.champEmail);
                champMotDePasse = findViewById(R.id.champMotDePasse);

                if ( champEmail.getEditText().getText().toString().isEmpty() || champMotDePasse.getEditText().getText().toString().isEmpty() ) {
                    Toast.makeText(MainActivity.this, "Connexion échouée", Toast.LENGTH_SHORT).show();
                } else {

                    String email = champEmail.getEditText().getText().toString();
                    String motDePasse = champMotDePasse.getEditText().getText().toString();
                    Connexion(email, motDePasse);
                }
            }
        });

        btnMotDePasseOublie = findViewById(R.id.btnMotDePasseOublie);
        btnMotDePasseOublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                champEmail = findViewById(R.id.champEmail);
                MotDePasseOublie(champEmail.getEditText().getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void Connexion(String email, String motDePasse) {
        mAuth.signInWithEmailAndPassword(email, motDePasse)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            Intent i = new Intent(MainActivity.this, QuestionActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(MainActivity.this, "Connexion échouée", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void MotDePasseOublie(String email) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Email envoyé !", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Email non envoyé", Toast.LENGTH_SHORT).show();
            }
        });
    }
}