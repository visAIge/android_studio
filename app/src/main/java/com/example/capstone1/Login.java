package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Button login_btn;
    private EditText login_ID;
    private EditText login_password;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_ID = findViewById(R.id.login_ID);
        login_password = findViewById(R.id.login_password);

        login_btn = findViewById(R.id.login);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(login_ID.getText().toString(),login_password.getText().toString());
            }
        });
    }

    private void signIn(String id, String password) {

        if (!id.isEmpty() && !password.isEmpty()) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("user");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot logSnapshot : snapshot.getChildren()) {
//                        String id = logSnapshot.getValue(String.class);
//                        if(id == null) {
//                            Toast.makeText(Login.this, "null", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            Toast.makeText(Login.this, id, Toast.LENGTH_SHORT).show();
//                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Login.this, "error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}