package com.hengshao.app.college_journal.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hengshao.app.college_journal.HTTP.HTTPLogin;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.Model.PostUserLogin;
import com.hengshao.app.college_journal.Model.ReturnPostLogin;
import com.hengshao.app.college_journal.PercentLinearLayout;
import com.hengshao.app.college_journal.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hengshao.app.college_journal.R.id.editpassword;

/**
 * Created by Heng on 2017/6/1.
 */

public class Login extends Activity implements View.OnClickListener {
    private EditText password;
    private EditText telephone;
    private PercentLinearLayout login_succeed;
    private String pwd;
    private String phone;
    private ExecutorService fix;
    private TextView no_register;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    finish();
                    Toast.makeText(getApplicationContext(),"登录成功！",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    password.setHint(msg.obj.toString());
                    password.setText("");
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),"服务器错误，请稍后重试！",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        password = (EditText) findViewById(editpassword);
        telephone = (EditText) findViewById(R.id.edittelephone);
        login_succeed = (PercentLinearLayout) findViewById(R.id.login_succeed);
        no_register = (TextView) findViewById(R.id.no_resgister);
        fix = Executors.newFixedThreadPool(1);

        no_register.setOnClickListener(this);
        login_succeed.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fix.shutdown();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_succeed:
                pwd = password.getText().toString();
                phone =  telephone.getText().toString();
                if (phone.length() == 11){
                    if (pwd.length() >= 6 & pwd.length() <=16){
                        final PostUserLogin postUserLogin = new PostUserLogin();
                        postUserLogin.setPassword(pwd);
                        postUserLogin.setTelephone(phone);
                        fix.execute(new Runnable() {
                            @Override
                            public void run() {
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(URL.url)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                HTTPLogin service = retrofit.create(HTTPLogin.class);
                                Call<ReturnPostLogin> call = service.PostLogin(postUserLogin);
                                try {
                                    Response<ReturnPostLogin> response = call.execute();
                                    Message msg = new Message();
                                    if (response.code() ==200){
                                        ReturnPostLogin data = response.body();
                                        msg.what = 1;
                                        SharedPreferences.Editor db = getSharedPreferences("LOGIN",MODE_PRIVATE).edit();
                                        db.putInt("id",data.getId());
                                        db.putString("telephone",data.getTelephone());
                                        db.putString("school",data.getSchool());
                                        db.putString("nickname",data.getNickname());
                                        db.putString("sex",data.getSex());
                                        db.putString("signature",data.getSignature());
                                        db.putString("createtime",data.getCreatetime());
                                        db.putString("password",data.getPassword());
                                        db.putString("authority",data.getAuthority());
                                        db.putString("praise",data.getPraise());
                                        db.commit();
                                    }else if (response.code() == 403){
                                        msg.what = 2;
                                        msg.obj = response.errorBody().string();
                                    }else {
                                        msg.what = 3;
                                    }
                                    handler.sendMessage(msg);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }else {
                        Toast.makeText(getApplicationContext(),"请输入6到16为密码。",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"请输入正确电话号码。",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.no_resgister:
                Intent intent = new Intent("android.intent.register");
                startActivity(intent);
                break;
        }
    }
}
//379260
