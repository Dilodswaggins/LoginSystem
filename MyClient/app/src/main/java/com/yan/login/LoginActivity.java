package com.yan.login;

import java.security.MessageDigest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 */
public class LoginActivity extends Activity {
    private EditText user;
    private EditText password;
    private String[] usernamex = new String[2];
    private Button login;
    private Button register;
    private Button forget;
    private Button bt_change_mode;
    private SharedPreferences pref;
    private CheckBox rembemberPass;
    String username;
    String password1;
    public static final String TAG = "LoginActivity";
    private static final String URLLOGIN = "http://user/login/data";
    //private static final String URLLOGIN = "http:///user/login/data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //绑定控件
        init();
        output();
        /*boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String user1 = pref.getString("user", "");
            String password1 = pref.getString("password", "");
            user.setText(user1);
            password.setText(password1);
            rembemberPass.setChecked(true);
        }*/


        bt_change_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getInputType() == 128) {//如果现在是显示密码模式
                    password.setInputType(129);//设置为隐藏密码
                } else {
                    password.setInputType(128);//设置为显示密码
                }
                password.setSelection(password.getText().length());//设置光标的位置到末尾
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override

            //登录按键的响应
            public void onClick(View v) {

                String[] data = null;
                String inputUser = user.getText().toString();
                String inputPassword = password.getText().toString();
                //String[] store = null;
                //int temp = 0;
                //store[temp] = inputUser;
                //Intent intent2 = new Intent(LoginActivity.this,page.class);
                //intent2.putExtra("store",store);
                //startActivity(intent2);
                StringBuffer sb =new StringBuffer();
                try{
                    MessageDigest digest = MessageDigest.getInstance("MD5");

                    byte[] bytes = inputPassword.getBytes();

                    byte[] digest2 = digest.digest(bytes);

                    for(byte b : digest2){
                        String hex = Integer.toHexString(b&0xff);
                        if(hex.length() == 1){
                            sb.append("0"+hex);
                        }else{
                            sb.append(hex);
                        }
                    }
                    inputPassword = sb.toString();

                }catch(Exception e){
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(inputUser)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(inputPassword)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    data = new String[]{inputUser, inputPassword};
                    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case 0:
                                    Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    input();

                                    StringBuilder response = new StringBuilder();
                                    //System.out.println("1");
                                    /*Intent intent = new Intent(LoginActivity.this, page.class);
                                    intent.putExtra("username",username);
                                    intent.putExtra("password",password1);
                                    startActivity(intent);*/
                                    //System.out.println("1");
                                    //System.out.println(usernamex[0]);
                                    //System.out.println(usernamex[1]);
                                    //System.out.println("2");
                                    Intent intent = new Intent(LoginActivity.this, page.class);
                                    intent.putExtra("username",usernamex[0]);
                                    intent.putExtra("password",usernamex[1]);
                                    //startActivity(new Intent(LoginActivity.this, page.class));
                                    startActivity(intent);
                                    LoginActivity.this.finish();

                                    break;
                                case 2:
                                    Toast.makeText(LoginActivity.this, "登录失败，请检查你的账号或密码", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Log.e("input error", "url为空");
                                    break;
                                case 4:
                                    Toast.makeText(LoginActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                            }
                        }
                    };
                    OperateData operateData = new OperateData();
                    String jsonString = operateData.stringTojson(data);
                    URL url = null;
                    try {
                        url = new URL(URLLOGIN);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    usernamex=operateData.sendData(jsonString, handler, url);

                }

            }
        });


        /**
         * 跳转到注册页面
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Forget.class);
                startActivity(intent);
            }
        });

        /**
         * 初始化
         */

    }

    private void input() {
//第一个参数是文件名，第二个参数是模式（不明白可以去补习一下SharedPreferences的知识）
        SharedPreferences.Editor edit = getSharedPreferences("mypsd", MODE_PRIVATE).edit();
        //判断选择框的状态   被选中isChecked或……
        if (rembemberPass.isChecked()) {
            edit.putString("nam", user.getText().toString());
            edit.putString("psd", password.getText().toString());
            edit.putBoolean("isChecked", true);
        } else {
//            edit.clear();              //若选择全部清除就保留这行代码，注释以下三行
            edit.putString("nam", user.getText().toString());//只存用户名
            edit.putString("psd", "");
            edit.putBoolean("isChecked", false);
        }
        edit.commit();
    }


    private void output() {
//第一个参数是文件名，第二个参数是模式
        SharedPreferences shared = getSharedPreferences("mypsd", MODE_PRIVATE);
        //第一个参数就是关键字，第二个参数为默认值，意思是说如果没找到值就用默认值代替
        String name1 = shared.getString("nam", "");//同上，若没找到就让它为空""
        String psd1 = shared.getString("psd", "");
        boolean ischecked1 = shared.getBoolean("isChecked", false);
        user.setText(name1);
        password.setText(psd1);
        rembemberPass.setChecked(ischecked1);
    }

    private void init() {
        login = findViewById(R.id.login);
        forget = findViewById(R.id.forget);
        bt_change_mode = findViewById(R.id.bt_change_mode);
        register = findViewById(R.id.register);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        rembemberPass = findViewById(R.id.remember);
    }
}
