package com.cburdeyron.questions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cburdeyron.questions.models.QuestionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class NouvelleQuestionActivity extends AppCompatActivity {
    FirebaseFirestore db;

    TextInputEditText champQuestion, champReponse;
    Button btnValiderNouvelleQuestion, btnsupprimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_question);

        db = FirebaseFirestore.getInstance();

        champQuestion = findViewById(R.id.champQuestion);
        champReponse = findViewById(R.id.champReponse);
        btnValiderNouvelleQuestion = findViewById(R.id.btnValiderNouvelleQuestion);
        btnValiderNouvelleQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionModel question = new QuestionModel(champQuestion.getText().toString(),champReponse.getText().toString());
                Ajouter(question);
            }
        });

        btnsupprimer = findViewById(R.id.btnsupprimer);
        if ( getIntent().getStringExtra("question") != null && !getIntent().getStringExtra("question").isEmpty() ){
            champQuestion.setText(getIntent().getStringExtra("question"));
            champReponse.setText(getIntent().getStringExtra("reponse"));

            btnsupprimer.setVisibility(View.VISIBLE);
        }

    }

    private void Ajouter(QuestionModel question) {
        db.collection("questions")
                .add(question)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Question enregistrée avec succès !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Une erreur est survenue ", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void supprimer() {
        db.collection("questions")
                .document("")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Question supprimée avec succès ! ", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Une erreur est survenue ", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
