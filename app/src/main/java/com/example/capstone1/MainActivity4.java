package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity4 extends AppCompatActivity {
    private Button createBtn;
    private EditText input_QR_user;
    private Button qr_go_main;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        input_QR_user = findViewById(R.id.input_QR_user);
        qr_go_main = findViewById(R.id.qr_go_main);
        qr_go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                startActivity(intent); //실제 화면 이동
            }
        });

        createBtn = findViewById(R.id.create_QR);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity4.this, CreateQR.class);
//                intent.putExtra("input_QR_user",input_QR_user.getText().toString());
//                startActivity(intent); //실제 화면 이동

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    // text == qr에 등록되는 정보 (현재는 사용자 이름만 되어있음)
                    BitMatrix bitMatrix = multiFormatWriter.encode(input_QR_user.getText().toString(), BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix); // 이걸 db에 저장..?
                    //iv.setImageBitmap(bitmap);

                    Toast.makeText(MainActivity4.this, bitmap.toString(), Toast.LENGTH_SHORT).show();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("user").child(input_QR_user.getText().toString()).child("qr_code");
                    databaseReference.setValue(bitmap);
                }catch (Exception e){}
            }
        });
    }
}