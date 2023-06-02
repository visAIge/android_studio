package com.example.capstone1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;


// 도어록 비밀번호 입력

public class MainActivity2 extends AppCompatActivity {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    private String login_user_id; // 로그인된 유저 아이디
    private EditText input_door_pwd;
    private Button open_door_btn; // 도어록 잠금 해제
    private String real_doorLock_pwd; // db에 등록된 실제 도어록 비밀번호
    private String input_doorLock_pwd; // 사용자가 입력한 도어록 비밀번호 값
    private Button go_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");
        input_door_pwd = findViewById(R.id.input_door_pwd);
        open_door_btn = findViewById(R.id.input_door_pwd_btn);
        go_main = findViewById(R.id.door_pwd_go_main);

        // 1. 로그인된 유저 아이디에 저장된 실제 비밀번호 값 가져오기
        databaseReference.child("user").child(login_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList group = dataSnapshot.getValue(userList.class);
                real_doorLock_pwd = group.getLock_pwd();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity2.this, "에러", Toast.LENGTH_SHORT).show();
            }
        });

        open_door_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2. edit text에서 입력된 비밀번호와 로그인된 유저 아이디에 저장된 비밀번호를 비교
                input_doorLock_pwd = input_door_pwd.getText().toString();

                if(input_doorLock_pwd.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "값을 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else {
                    if(input_doorLock_pwd.equals(real_doorLock_pwd)) {
                        // 3. 2번이 true이면 도어록 잠금 해제 신호 db로 전송
                        databaseReference.child("check_pwd").setValue("true");
                        Toast.makeText(MainActivity2.this, "도어록 잠금을 해제합니다.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        // 4. 2번이 false이면 에러 메시지 출력
                        Toast.makeText(MainActivity2.this, "비밀번호를 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent);
            }
        });
    }
}