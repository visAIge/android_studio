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

public class MainActivity5 extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ListView listView;
    Button goMainbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        goMainbtn = findViewById(R.id.log_go_main);
        goMainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity5.this, MainActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.lv_LogList);
        adapter = new ArrayAdapter<String>(this, R.layout.log_item_layout, arrayList);
        listView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("lock_log");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    String str = logSnapshot.getValue(String.class);
                    String time = str.substring(0,10);
                    String date = str.substring(11,19);
                    String log = time + " " + date + " 출입이 확인되었습니다.";
                    if(str == null) {
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