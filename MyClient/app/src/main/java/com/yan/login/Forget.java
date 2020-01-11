package com.yan.login;


import com.mob.MobSDK;
import cn.smssdk.EventHandler;
import cn.smssdk.OnDialogListener;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.CommonDialog;
import cn.smssdk.gui.ContactsPage;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.gui.util.Const;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;


import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Forget extends AppCompatActivity {

    private Button getCode;
    private String phoneNumber;
    private String checkCode;
    private ProgressDialog dialog;
    private Button loginForget;
    private Button relogin;
    private EditText loginTelephone;
    private EditText loginCode;
    private EditText loginPassword;
    private EditText loginPassword1;
    private EditText loginUsername;
    private String[] str = null;
    private int flag=0;
    private static final String URLForget= "http:///user/forget/data";

    private EventHandler ev = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                //验证码验证成功
                flag=1;
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    toast("验证成功");
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    toast("验证码已经发送");
                } else{
                    ((Throwable) data).printStackTrace();
                    String str = data.toString();
                    toast(str);
                }
            }
            if(result==SMSSDK.RESULT_ERROR) {
                flag=0;
                toast("验证码错误");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //控件绑定
        init();
        SMSSDK.registerEventHandler(ev);

        //注册按钮
        loginForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("sendCode");
                sendCheckCode();
                System.out.println("12138");
                System.out.println(flag);
                if(flag==1) {
                    checkCode = loginCode.getText().toString().trim();
                    String checkcode = checkCode;
                    String tele = loginTelephone.getText().toString();
                    String password = loginPassword.getText().toString();
                    String password1 = loginPassword1.getText().toString();
                    String username = loginUsername.getText().toString();
                    System.out.println(tele);
                    System.out.println(password);
                    System.out.println(username);
                    if (TextUtils.isEmpty(tele)) {
                        //用户名为空
                        Toast.makeText(Forget.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password)) {
                        Toast.makeText(Forget.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password1)) {
                        Toast.makeText(Forget.this, "请确认密码", Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(password1)) {
                        Toast.makeText(Forget.this, "两次密码不一样，请验证", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(checkcode)) {
                        Toast.makeText(Forget.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    } else {
                        StringBuffer sb = new StringBuffer();
                        try {
                            MessageDigest digest = MessageDigest.getInstance("MD5");

                            byte[] bytes = password1.getBytes();

                            byte[] digest2 = digest.digest(bytes);

                            for (byte b : digest2) {
                                String hex = Integer.toHexString(b & 0xff);
                                if (hex.length() == 1) {
                                    sb.append("0" + hex);
                                } else {
                                    sb.append(hex);
                                }
                            }
                            password1 = sb.toString();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        str = new String[]{username, tele, password1};
                        Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case 0:
                                        Toast.makeText(Forget.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(Forget.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        //注册成功跳转到登录页面
                                        startActivity(new Intent(Forget.this, LoginActivity.class));
                                        break;
                                    case 2:
                                        Toast.makeText(Forget.this, "登录失败，请检查你的账号或密码", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        Log.e("input error", "url为空");
                                        break;
                                    case 4:
                                        Toast.makeText(Forget.this, "连接超时", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                }
                            }
                        };
                        OperateData operateData = new OperateData();
                        String jsonString = operateData.stringTojson4(str);
                        URL url = null;
                        try {
                            url = new URL(URLForget);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        operateData.sendData(jsonString, handler, url);
                    }
                }else if(flag==0){
                    Toast.makeText(Forget.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 跳转回登录页面
         */
        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forget.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("getCode");
                getCheckCode();
            }
        });
    }


    private void showDailog(String text) {
        new AlertDialog.Builder(this)
                .setTitle(text)
                .setPositiveButton("确定", null)
                .show();
    }
    /**
     * 获取验证码
     */
    public void getCheckCode() {
        phoneNumber = loginTelephone.getText().toString();
        //发送短信，传入国家号和电话号码
        if (TextUtils.isEmpty(phoneNumber)) {
            toast("号码不能为空！");
        } else {
            SMSSDK.getVerificationCode("+86", phoneNumber);
            toast("发送成功!");
        }
    }

    /**
     * 向服务器提交验证码，在监听回调中监听是否验证
     */
    private void sendCheckCode() {
        checkCode = loginCode.getText().toString();
        if (!TextUtils.isEmpty(checkCode)) {
            //dialog = ProgressDialog.show(this, null, "正在验证...", false, true);
            //提交短信验证码
            SMSSDK.submitVerificationCode("+86", phoneNumber, checkCode);//国家号，手机号码，验证码
            Toast.makeText(this, "提交了注册信息:" + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
        }
    }


    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Forget.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(ev);
        super.onDestroy();
    }

    /**
     * 绑定控件
     */
    private void init() {
        loginTelephone = findViewById(R.id.L_telephone2);
        loginCode = findViewById(R.id.L_code2);
        loginPassword = findViewById(R.id.L_password12);
        loginPassword1 = findViewById(R.id.L_password22);
        loginForget = findViewById(R.id.L_Forget);
        relogin = findViewById(R.id.L_relogin2);
        getCode = findViewById(R.id.L_code);
        loginUsername = findViewById(R.id.L_username2);
    }

}
