package com.example.capstone1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * 1. 사용자가 특정 id를 editText에 입력하면
 * 2. 해당 아이디로 db에서 모든 정보를 가져온 후 qr 코드를 생성한다
 * 3. 생성된 qr코드 정보를 db에 저장한다.
 * 3-1. 현재 qr코드에 사용자 이름을 넣어서 db에 qr정보-사용자 이름을 넣는다 -> 이름 + 아이디 + 이메일 ?? 등등 추가적인 정보 등록
 * 3-2. rpi에서 qr코드를 인식한 후 인식된 이름이 db에 저장되어 있는지 판별 -> 이건 어떻게 수정...?
 * **/

public class CreateQR extends AppCompatActivity {
    private ImageView iv;
    private String text;
    private Button main_btn;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        Intent intent = getIntent();

        iv = (ImageView)findViewById(R.id.qrcode);
        text = intent.getStringExtra("input_QR_user");
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            // text == qr에 등록되는 정보 (현재는 사용자 이름만 되어있음)
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix); // 이걸 db에 저장..?
            iv.setImageBitmap(bitmap);

            //파이어베이스로 QR 정보 전송
            String user = "user" + Integer.toString(count.count);
            count.count = count.count + 1;
            databaseReference.child("qr_info").child(user).setValue(text);
        }catch (Exception e){}

        main_btn = findViewById(R.id.main_button);
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateQR.this, MainActivity.class);
                startActivity(intent); //실제 화면 이동
            }
        });
    }
}