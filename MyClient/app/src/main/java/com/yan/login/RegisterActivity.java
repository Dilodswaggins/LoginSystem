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

public class RegisterActivity extends Activity implements View.OnClickListener {
    private boolean isChinese(String name)
    {
        int n = 0;
        for(int i = 0; i < name.length(); i++) {
            n = (int)name.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }
    private Button loginRegister;
    private Button relogin;
    private EditText loginName;
    private EditText loginUser;
    private EditText loginPassword;
    private EditText loginPassword1;
    private EditText loginPhone;
    private EditText loginAge;
    //private String age;
    private String[] usernamey = new String[2];
    private String[] str = null;
    private TextView textView;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private static final String URLREGISTER = "http:///user/register/data";
    private static final String URLLOGIN = "http:/user/login/data";
    //private static final String URLREGISTER = "http://user/register/data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

       //控件绑定
        init();

        //注册按钮
        loginRegister.setOnClickListener(new View.OnClickListener() {
            String[] data = null;
            int flag=0;
            @Override

            public void onClick(View v) {
                String name = loginName.getText().toString();
                String user = loginUser.getText().toString();
                String password = loginPassword.getText().toString();
                String password1 = loginPassword1.getText().toString();
                String telephone = loginPhone.getText().toString();
                String age = loginAge.getText().toString();
                String pattern2 = "^^[a-zA-z]+$";
                boolean match2 = Pattern.matches(pattern2, name);
                String pattern3 = "^[a-zA-Z_][a-zA-Z0-9_]*$";
                for(int i=0;i<user.length();i++){
                    if(user.charAt(i)>='A'&&user.charAt(i)<='Z'){
                        flag++;
                    }
                }
                boolean match10;
                if(flag>0){
                    match10=true;
                }
                else{
                    match10=false;
                }
                boolean match3 = Pattern.matches(pattern3, user);
                String pattern5 = "^(13|15|18)\\d{9}$";
                String pattern4 = "^([1-9]\\d|\\d)$";
                boolean match4 = Pattern.matches(pattern4, age);
                boolean match5 = Pattern.matches(pattern5, telephone);
                        if (TextUtils.isEmpty(user)) {
                            //用户名为空
                            Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(name)) {
                            Toast.makeText(RegisterActivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(password)) {
                            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(password1)) {
                            Toast.makeText(RegisterActivity.this, "请确认密码",Toast.LENGTH_SHORT).show();
                        } else if (!password.equals(password1)) {
                            Toast.makeText(RegisterActivity.this, "两次密码不一样，请验证", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(telephone)) {
                            Toast.makeText(RegisterActivity.this, "请输入电话", Toast.LENGTH_SHORT).show();
                        } else if(age.length()>2){
                            Toast.makeText(RegisterActivity.this, "年龄不能超过两位，请验证", Toast.LENGTH_SHORT).show();
                        } else if (name.length()> 8) {
                            Toast.makeText(RegisterActivity.this, "姓名不能超过八位，请验证", Toast.LENGTH_SHORT).show();
                        } else if (user.length()>10) {
                            Toast.makeText(RegisterActivity.this, "用户名不能超过10位，请验证", Toast.LENGTH_SHORT).show();
                        }else if (user.length()<5) {
                            Toast.makeText(RegisterActivity.this, "用户名不能少于5位，请验证", Toast.LENGTH_SHORT).show();
                        } else if(!match10){
                            Toast.makeText(RegisterActivity.this, "用户名至少一个大写，请验证", Toast.LENGTH_SHORT).show();
                        }else if(!match3){
                            Toast.makeText(RegisterActivity.this, "用户名只能含有字母和数字，请验证", Toast.LENGTH_SHORT).show();
                        } else if(!match4){
                            Toast.makeText(RegisterActivity.this, "年龄只能包含数字，请验证", Toast.LENGTH_SHORT).show();
                        } else if(!match5){
                            Toast.makeText(RegisterActivity.this, "请输入正确的电话号码（中国内陆形式）", Toast.LENGTH_SHORT).show();
                        } else {
                            StringBuffer sb =new StringBuffer();
                            try{
                                MessageDigest digest = MessageDigest.getInstance("MD5");

                                byte[] bytes = password1.getBytes();

                                byte[] digest2 = digest.digest(bytes);

                                for(byte b : digest2){
                                    String hex = Integer.toHexString(b&0xff);
                                    if(hex.length() == 1){
                                        sb.append("0"+hex);
                                    }else{
                                        sb.append(hex);
                                    }
                                }
                                password1 = sb.toString();

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            str = new String[]{name, user, password1, telephone, age};
                            data = new String[]{user, password1};
                            Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    switch (msg.what) {
                                        case 0:
                                            Toast.makeText(RegisterActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 1: Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                            //注册成功跳转到登录页面

                                            Intent intent = new Intent(RegisterActivity.this, page.class);
                                            Handler handler1 = new Handler();
                                            OperateData operateData = new OperateData();
                                            String jsonString = operateData.stringTojson(data);
                                            URL url = null;
                                            try {
                                                url = new URL(URLLOGIN);
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }
                                            usernamey=operateData.sendData(jsonString, handler1, url);
                                            intent.putExtra("username",usernamey[0]);
                                            intent.putExtra("password",usernamey[1]);
                                            //startActivity(new Intent(LoginActivity.this, page.class));
                                            startActivity(intent);
                                            //LoginActivity.this.finish();
                                            //startActivity( new Intent(RegisterActivity.this, LoginActivity.class));
                                            RegisterActivity.this.finish();
                                            break;
                                        case 2:
                                            Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 3:
                                            Log.e("input error", "url为空");
                                            break;
                                        case 4:Toast.makeText(RegisterActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                    }
                                }
                            };
                            OperateData operateData = new OperateData();
                            String jsonString = operateData.stringTojson2(str);
                            URL url = null;
                            try {
                                url = new URL(URLREGISTER);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            usernamey=operateData.sendData(jsonString, handler, url);
                        }

                //保存数据到SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences("register", MODE_PRIVATE).edit();
                editor.putString("Name",name);
                editor.putString("User", user);
                editor.putString("password", password);
                editor.putString("password1", password1);
                editor.putString("telephone", telephone);
                editor.putString("Age", age);
                editor.commit();
            }
        });
        /**
         * 跳转回登录页面
         */
        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     *
     * 字符数组转json
     */
    private String stringArraytoJson(String[] strings) {

        if (strings == null){return "";}
        String js = "[{"+"name"+strings[0]+"username:"+strings[1]+"password:"+strings[2]+"phone"+strings[3]+"age"+strings[4];
        return js;
    }
    
    /**
     * 绑定控件
     */
    private void init() {
        loginName = findViewById(R.id.L_name);
        loginRegister = findViewById(R.id.L_register);
        loginUser = findViewById(R.id.L_user);
        loginPassword = findViewById(R.id.L_password);
        loginPassword1 = findViewById(R.id.L_password1);
        loginPhone = findViewById(R.id.L_phone);
        loginAge = findViewById(R.id.L_age);
        relogin = findViewById(R.id.L_relogin);
    }


    @Override
    public void onClick(View v) {

    }

}





