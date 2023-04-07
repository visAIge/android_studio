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

import java.io.ByteArrayOutputStream;

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
                // 'create' 버튼 클릭했을 때 QR 생성 후 DB에 저장만 출력은 따로 UI 추가 예정
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    // text == qr에 등록되는 정보 (현재는 사용자 이름만 되어있음)
                    BitMatrix bitMatrix = multiFormatWriter.encode(input_QR_user.getText().toString(), BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix); // bitmap 이미지 db에 저장

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] reviewImage = stream.toByteArray();
                    String simage = byteArrayToBinaryString(reviewImage);

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("user").child(input_QR_user.getText().toString()).child("qr_code");
                    databaseReference.setValue(simage);
                }catch (Exception e){}
            }
        });
    }

    public static String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for(int bit = 0; bit<8; bit++) {
            if(((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }
}