package com.yan.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 */

public class Change extends Activity implements View.OnClickListener {

    private Button retu;
    private Button change;
    private EditText changeusername;
    private EditText changePassword;
    private EditText changeAge;
    private EditText changePhone;
    private Button relogin;
    private EditText loginName;
    private EditText loginUser;
    private EditText loginPassword;
    private EditText loginPassword1;
    private EditText loginPhone;
    private EditText loginAge;
    //private String age;
    private String[] str = null;
    private TextView textView;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private static final String URLCHANGE = "http:///user/modify/data";
    //private static final String URLREGISTER = "http:///user/register/data";
    private Intent intent;
    private String str_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //控件绑定
        init();
        intent= getIntent();
        str_num = intent.getStringExtra("num");
        System.out.println("1");
        System.out.println(str_num);
        System.out.println("2");
        //注册按钮
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = "";
                String telephone = "";
                String age = "";
                String password = "";
                username = username+changeusername.getText().toString();
                telephone = telephone+changePhone.getText().toString();
                age = age+changeAge.getText().toString();
                password = password+changePassword.getText().toString();
                System.out.println(telephone);
                System.out.println(age);
                System.out.println(password);
                String num = str_num;
                System.out.println(num);
                StringBuffer sb =new StringBuffer();
                try{
                    MessageDigest digest = MessageDigest.getInstance("MD5");

                    byte[] bytes = password.getBytes();

                    byte[] digest2 = digest.digest(bytes);

                    for(byte b : digest2){
                        String hex = Integer.toHexString(b&0xff);
                        if(hex.length() == 1){
                            sb.append("0"+hex);
                        }else{
                            sb.append(hex);
                        }
                    }
                    password = sb.toString();

                }catch(Exception e){
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(username)) {
                    //用户名为空
                    Toast.makeText(Change.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                    str = new String[]{username,password,telephone,age,num};

                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case 0:
                                    Toast.makeText(Change.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1: Toast.makeText(Change.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    startActivity( new Intent(Change.this, LoginActivity.class));
                                    Change.this.finish();
                                    break;
                                case 2:
                                    Toast.makeText(Change.this, "修改失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Log.e("input error", "url为空");
                                    break;
                                case 4:Toast.makeText(Change.this, "连接超时", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                            }
                        }
                    };
                    OperateData operateData = new OperateData();
                    String jsonString = operateData.stringTojson3(str);
                    URL url = null;
                    try {
                        url = new URL(URLCHANGE);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    operateData.sendData(jsonString, handler, url);
                }

        });
        retu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Change.this, page.class);
                startActivity(intent);
            }
        });

    }


    /**
     *
     * 字符数组转json
     */

    /**
     * 绑定控件
     */
    private void init() {
        change = findViewById(R.id.change);
        retu = findViewById(R.id.L_relogin2);
        changePassword = findViewById(R.id.L_password1);
        changePhone = findViewById(R.id.L_tele1);
        changeAge = findViewById(R.id.L_age1);
        changeusername = findViewById(R.id.L_username1);
    }


    @Override
    public void onClick(View v) {

    }

}






