package com.cburdeyron.questions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cburdeyron.questions.models.QuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<QuestionModel> questions = new ArrayList<>();
    List<Map<String, String>> mesQuestions = new ArrayList<>();

    FloatingActionButton btnAjout;
    Button btnsupprimer;
    ListView listeQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        btnAjout = findViewById(R.id.btnAjout);
        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuestionActivity.this, NouvelleQuestionActivity.class);
                startActivity(i);
            }
        });

        // btnsupprimer = findViewById(R.id.btnsupprimer);
        // btnsupprimer.setOnClickListener(new View.OnClickListener() {
            // @Override
            // public void onClick(View v) {
                // SupprimerQuestion();
            // }
        // });

        RecupererQuestions();

        listeQuestions = findViewById(R.id.listeQuestions);

        listeQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionModel maQuestion = questions.get(position);

                Intent i = new Intent(QuestionActivity.this, NouvelleQuestionActivity.class);

                i.putExtra("question", maQuestion.getQuestion());
                i.putExtra("reponse", maQuestion.getReponse());

                startActivity(i);
            }
        });
    }

    private void RecupererQuestions() {
        db.collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                QuestionModel question = new QuestionModel(document.getString("question"),document.getString("reponse"));
                                questions.add(question);

                                Map<String, String> ListItem = new HashMap<>();

                                ListItem.put("question",question.getQuestion());
                                ListItem.put("reponse",question.getReponse());

                                mesQuestions.add(ListItem);
                            }

                            SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), mesQuestions,
                                    android.R.layout.simple_list_item_2,
                                    new String[] {"question", "reponse" },
                                    new int[] {android.R.id.text1, android.R.id.text2 });

                            listeQuestions.setAdapter(simpleAdapter);

                        } else {
                            Toast.makeText(QuestionActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void modifier(Map<String, Object> question) {
        db.collection("taches")
                .document("")
                .set(question)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(QuestionActivity.this, "Question modifiée !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SupprimerQuestion( QueryDocumentSnapshot document ){
        db.collection("questions").document(document.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(QuestionActivity.this, "Question supprimée !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionActivity.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
