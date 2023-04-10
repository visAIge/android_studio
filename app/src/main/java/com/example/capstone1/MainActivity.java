package com.example.capstone1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class MainActivity extends AppCompatActivity {

    private Button btn_password;
    private Button btn_otp;
    private Button btn_QR;
    private Button btn_log;
    private ImageView imageView;

    String login_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_password = findViewById(R.id.input_password);
        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent); //실제 화면 이동
            }
        });

        btn_otp = findViewById(R.id.button3);
        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        btn_QR = findViewById(R.id.button4);
        btn_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                startActivity(intent); //실제 화면 이동
            }
        });

        btn_log = findViewById(R.id.button5);
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, MainActivity5.class);
//                startActivity(intent); //실제 화면 이동
                Intent intent = new Intent(MainActivity.this, CreateQR.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        imageView = (ImageView) findViewById(R.id.main_image_view);
        registerForContextMenu(imageView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.user_info_item:
                Intent intent = new Intent(MainActivity.this, user_info.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
                break;
        }
        return super.onContextItemSelected(item);
    }
}