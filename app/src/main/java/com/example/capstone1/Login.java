package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("user");

        if (!id.isEmpty() && !password.isEmpty()) {
            databaseReference.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userList group = dataSnapshot.getValue(userList.class);

                    if(password.equals(group.getPassword())) {
                        // 메인화면으로 이동
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("login_user_id", id);
                        startActivity(intent); //실제 화면 이동
                    }
                    else {

                        Toast.makeText(Login.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Login.this, "에러", Toast.LENGTH_SHORT).show();
                    //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }
            });
        }
    }

//    void showDialog() {
//        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Login.this)
//                .setTitle("로그인 실패")
//                .setMessage("비밀번호가 틀렸습니다.")
//                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//        AlertDialog msgDlg = msgBuilder.create();
//        msgDlg.show();
//    }
}