package com.yan.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class page extends AppCompatActivity {
    private Intent intent;
    private TextView username;
    private String str_username;
    private String str_name;
    private String str_password;
    private String str_phone;
    private int int_age;
    private int int_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        username = (TextView) this.findViewById(R.id.mypage_username);
        intent= getIntent();
        str_username = intent.getStringExtra("username");
        str_name = intent.getStringExtra("password");
        //int_age = intent.getIntExtra("age",0);
        //int_num = intent.getIntExtra("num",0);
        username.setText(str_username);

    }



    public void showInfo(View v){
        Intent intent1 = new Intent(this, com.yan.login.SuccessActivity.class);
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
    public void cancel(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
