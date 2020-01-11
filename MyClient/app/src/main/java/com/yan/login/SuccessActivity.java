package com.yan.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import com.yan.login.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

public class SuccessActivity extends Activity {
    private static final String URLCHANGE = "http:///user/change/data";
    private Button change;
    private Button ret2;
    private Intent intent;
    private TextView username;
    private TextView name;
    private TextView age;
    private TextView num;
    private String str_username;
    private String str_name;
    private String str_phone;
    private int int_age;
    private int int_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        username = (TextView) this.findViewById(R.id.username_info);
        name = (TextView) this.findViewById(R.id.name_info);
        //age = this.findViewById(R.id.age_info);
        //num = this.findViewById(R.id.num_info);
        init();
        intent= getIntent();
        str_username = intent.getStringExtra("username");
        str_name = intent.getStringExtra("name");
        //int_age = intent.getIntExtra("age",0);
        //int_num = intent.getIntExtra("num",0);
        username.setText(str_username);
        name.setText(str_name);
        //age.setText(int_age);
        //num.setText(int_num);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(str_name);
                Intent intent2 = new Intent(SuccessActivity.this, Change.class);
                intent2.putExtra("num",str_name);
                //startActivity(new Intent(LoginActivity.this, page.class));
                startActivity(intent2);
                SuccessActivity.this.finish();
            }
        });

        ret2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(SuccessActivity.this, page.class);
                intent3.putExtra("password",str_name);
                intent3.putExtra("username",str_username);
                //startActivity(new Intent(LoginActivity.this, page.class));
                startActivity(intent3);
                SuccessActivity.this.finish();
            }
        });

    }


    public void cancel(View v){
        Intent intent1 = new Intent(this,page.class);
        try {
            intent1.putExtra("username", str_username);
            intent1.putExtra("name", str_name);
            //intent1.putExtra("age",int_age);
            //intent1.putExtra("num",int_num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(intent1);
    }

    private void init(){
        ret2 = findViewById(R.id.return3);
        change = findViewById(R.id.change);
    }
}
