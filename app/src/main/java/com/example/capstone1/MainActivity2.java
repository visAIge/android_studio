package com.example.capstone1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;


// 도어록 비밀번호 입력

public class MainActivity2 extends AppCompatActivity {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("input_password"); //앱에서 비밀번호 입력
    DatabaseReference conditionRef2 = mRootRef.child("check_qr"); //지문 인식 성공하면 도어락 잠금 해제

    private Button pass_btn0;
    private Button pass_btn1;
    private Button pass_btn2;
    private Button pass_btn3;
    private Button pass_btn4;
    private Button pass_btn5;
    private Button pass_btn6;
    private Button pass_btn7;
    private Button pass_btn8;
    private Button pass_btn9;
    private Button pass_clear;
    private Button pass_enter;

    private Button fingerPrint_btn;
    private Button back_btn;

    private EditText etPasscode1;
    private EditText etPasscode2;
    private EditText etPasscode3;
    private EditText etPasscode4;

    private String password;

    private String login_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");

        pass_btn0 = findViewById(R.id.pass_btn0);
        pass_btn1 = findViewById(R.id.pass_btn1);
        pass_btn2 = findViewById(R.id.pass_btn2);
        pass_btn3 = findViewById(R.id.pass_btn3);
        pass_btn4 = findViewById(R.id.pass_btn4);
        pass_btn5 = findViewById(R.id.pass_btn5);
        pass_btn6 = findViewById(R.id.pass_btn6);
        pass_btn7 = findViewById(R.id.pass_btn7);
        pass_btn8 = findViewById(R.id.pass_btn8);
        pass_btn9 = findViewById(R.id.pass_btn9);
        pass_clear = findViewById(R.id.pass_clear);
        pass_enter = findViewById(R.id.pass_enter);

        fingerPrint_btn = findViewById(R.id.fingerPrint_btn);
        fingerPrint_btn.setOnClickListener(fingerBtnListener);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        etPasscode1 = findViewById(R.id.etPasscode1);
        etPasscode2 = findViewById(R.id.etPasscode2);
        etPasscode3 = findViewById(R.id.etPasscode3);
        etPasscode4 = findViewById(R.id.etPasscode4);

        ArrayList buttonArray = new ArrayList<Button>();
        buttonArray.add(pass_btn0);
        buttonArray.add(pass_btn1);
        buttonArray.add(pass_btn2);
        buttonArray.add(pass_btn3);
        buttonArray.add(pass_btn4);
        buttonArray.add(pass_btn5);
        buttonArray.add(pass_btn6);
        buttonArray.add(pass_btn7);
        buttonArray.add(pass_btn8);
        buttonArray.add(pass_btn9);
        buttonArray.add(pass_clear);
        buttonArray.add(pass_enter);

        for(Object button : buttonArray) {
            Button btn = (Button) button;
            btn.setOnClickListener(btnListener);
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "성공했습니다.", Toast.LENGTH_SHORT).show();
                conditionRef2.setValue("true");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("지문 인증")
                .setSubtitle("기기에 등록된 지문을 이용하여 지문을 인증해주세요.")
                .setNegativeButtonText("취소")
                .setDeviceCredentialAllowed(false)
                .build();
    }

    View.OnClickListener fingerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            biometricPrompt.authenticate(promptInfo);
        }
    };

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int currentValue = -1;
            switch (view.getId()) {
                case R.id.pass_btn0:
                    currentValue = 0;
                    break;
                case R.id.pass_btn1:
                    currentValue = 1;
                    break;
                case R.id.pass_btn2:
                    currentValue = 2;
                    break;
                case R.id.pass_btn3:
                    currentValue = 3;
                    break;
                case R.id.pass_btn4:
                    currentValue = 4;
                    break;
                case R.id.pass_btn5:
                    currentValue = 5;
                    break;
                case R.id.pass_btn6:
                    currentValue = 6;
                    break;
                case R.id.pass_btn7:
                    currentValue = 7;
                    break;
                case R.id.pass_btn8:
                    currentValue = 8;
                    break;
                case R.id.pass_btn9:
                    currentValue = 9;
                    break;
                case R.id.pass_clear:
                    onClear();
                    break;
                case R.id.pass_enter:
                    onEnter();
                    break;
            }

            String strCurrentValue = Integer.toString(currentValue);
            if(currentValue != -1) {
                if(etPasscode1.isFocusable()) {
                    etPasscode1.setText(strCurrentValue);
                    etPasscode1.setFocusable(false);
                    etPasscode2.setFocusable(true);
                    etPasscode2.requestFocus();
                }
                else if(etPasscode2.isFocusable()) {
                    etPasscode2.setText(strCurrentValue);
                    etPasscode2.setFocusable(false);
                    etPasscode3.setFocusable(true);
                    etPasscode3.requestFocus();
                }
                else if(etPasscode3.isFocusable()) {
                    etPasscode3.setText(strCurrentValue);
                    etPasscode3.setFocusable(false);
                    etPasscode4.setFocusable(true);
                    etPasscode4.requestFocus();
                }
                else {
                    etPasscode4.setText(strCurrentValue);
                }
            }

            if(etPasscode1.length() != 0 && etPasscode2.length() != 0 && etPasscode3.length() != 0 && etPasscode4.length() != 0) {
                password = etPasscode1.getText().toString() + etPasscode2.getText().toString() + etPasscode3.getText().toString() + etPasscode4.getText().toString();
            }
        }

        private void onClear() {
            etPasscode1.setText("");
            etPasscode2.setText("");
            etPasscode3.setText("");
            etPasscode4.setText("");

            if(etPasscode2.isFocusable()) {
                etPasscode2.clearFocus();
            }
            else if(etPasscode3.isFocusable()) {
                etPasscode3.clearFocus();
            }
            else if(etPasscode4.isFocusable()) {
                etPasscode4.clearFocus();
            }
            etPasscode1.setFocusable(true);
            etPasscode1.requestFocus();
        }

        private void onEnter() {
            //파이어베이스로 비밀번호 전송
            if(etPasscode1.length() != 0 && etPasscode2.length() != 0 && etPasscode3.length() != 0 && etPasscode4.length() != 0) {
                Toast.makeText(getApplicationContext(), "비밀번호 입력 완료", Toast.LENGTH_SHORT).show();
                conditionRef.setValue(password);
            }
            else {
                Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}