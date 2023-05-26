package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// 출입이력 출력

public class MainActivity5 extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private String login_user_id;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ListView listView;
    Button goMainbtn;

    Button pwd_log_btn;
    Button face_log_btn;
    Button qr_log_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");

        goMainbtn = findViewById(R.id.log_go_main);

        // 메인화면으로 이동
        goMainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity5.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.lv_LogList);
        adapter = new ArrayAdapter<String>(this, R.layout.log_item_layout, arrayList);
        listView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // 각 버튼에 따라 비밀번호 입력, 얼굴 인식, qr 코드를 이용한 출입 이력으로 로그가 나누어짐
        pwd_log_btn = findViewById(R.id.pwd_log);
        pwd_log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = firebaseDatabase.getReference().child("door_open").child(login_user_id).child("pwd_lock"); //pwd_lock : 선택한 버튼에 따라서 유동적으로
                output_log();
            }
        });

        face_log_btn = findViewById(R.id.face_log);
        face_log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = firebaseDatabase.getReference().child("door_open").child(login_user_id).child("face_lock"); //pwd_lock : 선택한 버튼에 따라서 유동적으로
                output_log();
            }
        });

        qr_log_btn = findViewById(R.id.qr_log);
        qr_log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = firebaseDatabase.getReference().child("door_open").child(login_user_id).child("qr_lock"); //pwd_lock : 선택한 버튼에 따라서 유동적으로
                output_log();
            }
        });

    }

    // 선택한 버튼에 해당하는 데이터를 db에서 가져옴
    public void output_log() {
        arrayList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    logList group = logSnapshot.getValue(logList.class);

                    String user_id = group.getUser_id();
                    boolean success = group.isSuccess();

                    String time_date = group.getDate();
                    String time = time_date.substring(0,10);
                    String date = time_date.substring(11,19);
                    String log = user_id + "님이" + time + " " + date + "에 출입이 확인되었습니다.";

                    if(group == null) {
                        Toast.makeText(MainActivity5.this, "null", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        arrayList.add(log);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity5.this, "error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}