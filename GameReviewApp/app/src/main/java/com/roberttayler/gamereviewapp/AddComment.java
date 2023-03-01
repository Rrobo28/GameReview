package com.roberttayler.gamereviewapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddComment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        setTitle("Add Comment");
    }

    public void StopActivity(View view) {
        //Gets the edit texts
        EditText comment = findViewById(R.id.AddComment);
        EditText rating = findViewById(R.id.AddRating);

        //checks to make sure the information is correct
        if(rating.getText().toString().isEmpty()|| comment.getText().toString().isEmpty()){
            Toast toast = Toast.makeText(this,"Please fill out both fields", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(Integer.parseInt(String.valueOf(rating.getText())) > 10 || Integer.parseInt(String.valueOf(rating.getText())) < 0) {
            Toast toast = Toast.makeText(this,"The rating needs to be between 0 and 10", Toast.LENGTH_SHORT);
            toast.show();
        }
        //Creates a new intent with the content and rating of the comment
        // This allows information to be passed back to the previous page
        else {
            Log.d("ERROR", String.valueOf(rating.getText()));
            Intent returnIntent = new Intent();
            returnIntent.putExtra("comment", comment.getText().toString());
            returnIntent.putExtra("rating", rating.getText().toString());
            setResult(0, returnIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }
}
