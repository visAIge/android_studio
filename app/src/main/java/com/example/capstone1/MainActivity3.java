package com.example.capstone1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity3 extends AppCompatActivity {
    private TextView otp_key;
    private EditText otp_input;
    private Button main_btn2;
    private Button input_otp_btn;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("check_otp");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        otp_key = findViewById(R.id.otp_key);
        otp_input = findViewById(R.id.otp_input);
        main_btn2 = findViewById(R.id.main_btn2);
        input_otp_btn = findViewById(R.id.input_otp_btn);

        main_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                startActivity(intent); //실제 화면 이동
            }
        });

        // 회원가입 할 때 otp key를 db에 저장해두기 때문에 generate를 계속 호출할 필요가 없음
        // db에서 otp key를 가져와서 비교하는 식으로 수정해야 함
        HashMap<String, String> map = MainActivity3.generate("name", "host");
        String otpkey = map.get("encodedKey");
        String url = map.get("url");

        otp_key.setText(otpkey);

        input_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = checkCode(otp_input.getText().toString(), otpkey); //EditText에 입력한 otp 코드와 생성된 otp 코드 비교
                if(check) {
                    Toast.makeText(getApplicationContext(), "도어락 키패드가 활성화됩니다.", Toast.LENGTH_SHORT).show();
                    conditionRef.setValue("true");
                }
                else {
                    Toast.makeText(getApplicationContext(), "otp 번호를 다시 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 이 함수가 otp key를 생성하는 부분이라서 회원가입할 때 한번 호출하기만 하면 됨 그 이후에는 호출 필요없음
    public static HashMap<String, String> generate(String userName, String hostName) {
        HashMap<String, String> map = new HashMap<String, String>();
        String userId = "bae0000"; // 회원가입 할 때 입력한 아이디

        byte[] userId_byte = userId.getBytes();
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(userId_byte, 10);
        byte[] bEncodedKey = codec.encode(secretKey);

        String encodedKey = new String(bEncodedKey);
        String url = getQRBarcodeURL(userName, hostName, encodedKey);
        // Google OTP 앱에 userName@hostName 으로 저장됨
        // key를 입력하거나 생성된 QR코드를 바코드 스캔하여 등록

        // map에 저장할 필요 없음 db에서 가져올거임
        map.put("encodedKey", encodedKey);
        map.put("url", url);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(userId).child("otp_key");
        databaseReference.setValue(encodedKey);

        return map;
    }

    public boolean checkCode(String userCode, String otpkey) {
        long otpnum = Integer.parseInt(userCode); // Google OTP 앱에 표시되는 6자리 숫자
        long wave = new Date().getTime() / 30000; // Google OTP의 주기는 30초
        boolean result = false;
        try {
            Base32 codec = new Base32();
            byte[] decodedKey = codec.decode(otpkey);
            int window = 3;
            for (int i = -window; i <= window; ++i) {
                long hash = verify_code(decodedKey, wave + i);
                if (hash == otpnum) result = true;
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[20 - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }

    public static String getQRBarcodeURL(String user, String host, String secret) {
        // QR코드 주소 생성
        String format2 = "http://chart.apis.google.com/chart?cht=qr&chs=200x200&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s&chld=H|0";
        return String.format(format2, user, host, secret);
    }
}