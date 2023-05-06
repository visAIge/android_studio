package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity4 extends AppCompatActivity {
    private Button createBtn;
    private EditText input_QR_user;
    private Button qr_go_main;
    private String login_user_id;
    private HashMap<String, String> qr_info = new HashMap<String, String>();

    private static final String SECRET_KEY = "1234567890123456"; // qr 문자열 암호화할 때 사용할 키

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference().child("user");

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");

        input_QR_user = findViewById(R.id.input_QR_user);
        qr_go_main = findViewById(R.id.qr_go_main);
        qr_go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent);
            }
        });

        createBtn = findViewById(R.id.create_QR);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(input_QR_user.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList group = dataSnapshot.getValue(userList.class);
                        qr_info.put("id",input_QR_user.getText().toString());
                        qr_info.put("name", group.getName()); //여기서 오류가 발생해야 하는데 그냥 존재하지 않는 아이디로 만들어버림..뭐지?
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity4.this, "에러", Toast.LENGTH_SHORT).show();
                    }
                });

                // 'create' 버튼 클릭했을 때 QR 생성 후 DB에 저장
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    String qr_info_str = qr_info.toString();

                    // qr에 담을 문자열 암호화 과정
                    qr_encryption crypto = new qr_encryption(SECRET_KEY);
                    String encryptText = crypto.encrypt(qr_info_str);

                    BitMatrix bitMatrix = multiFormatWriter.encode(encryptText, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix); // bitmap 이미지 db에 저장

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] reviewImage = stream.toByteArray();
                    String simage = byteArrayToBinaryString(reviewImage);

                    // qr 이미지를 db에 저장
                    databaseReference.child(input_QR_user.getText().toString()).child("qr_code").child("img").setValue(simage);

                    // qr이 생성된 날짜 저장
                    databaseReference.child(input_QR_user.getText().toString()).child("qr_code").child("date").setValue(getTime());

                    // 만들어진 qr에 담긴 아이디를 저장
                    databaseReference.child(login_user_id).child("create_qr").child(Integer.toString(count.count)).setValue(input_QR_user.getText().toString());

                    Toast.makeText(MainActivity4.this, "QR코드 생성 성공", Toast.LENGTH_SHORT).show();
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

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}