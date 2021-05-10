package com.cburdeyron.questions.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class QuestionModel {

    private DatabaseReference mDatabase;

    String question;
    String reponse;

    public QuestionModel(){
        // Default constructor required for calls to DataSnapshot.getValue(QuestionModel.class)
    }

    public QuestionModel( String question, String response ){
        this.question = question;
        this.reponse = response;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String Question) {
        this.question = Question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String Reponse) {
        this.reponse = Reponse;
    }
}
