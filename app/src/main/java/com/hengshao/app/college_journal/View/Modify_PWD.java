package com.hengshao.app.college_journal.View;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.HTTP.Pwd;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.PercentLinearLayout;
import com.hengshao.app.college_journal.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Heng on 2017/6/5.
 */

public class Modify_PWD extends Activity implements View.OnClickListener {
    private EditText old_pwd;
    private EditText new_pwd;
    private EditText affirm_pwd;
    private PercentLinearLayout modify_pwd;
    private String old_pwd_;
    private String new_pwd_;
    private String affirm_pwd_;
    private int decide = 0;
    private String query_pwd;
    private int id = 0;
    private ExecutorService fix;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    finish();
                    Toast.makeText(getApplicationContext(),"密码更新成功！",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"服务器错误，请稍后重试!",Toast.LENGTH_LONG);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_pwd);
        old_pwd = (EditText) findViewById(R.id.old_pwd);
        new_pwd = (EditText) findViewById(R.id.new_pwd);
        affirm_pwd = (EditText) findViewById(R.id.affirm_pwd);
        modify_pwd = (PercentLinearLayout) findViewById(R.id.modify_pwd);
        fix = Executors.newFixedThreadPool(1);

        modify_pwd.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fix.shutdown();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.modify_pwd:
                old_pwd_ = old_pwd.getText().toString();
                new_pwd_ = new_pwd.getText().toString();
                affirm_pwd_ = affirm_pwd.getText().toString();
                if (old_pwd_.length() >= 6 & old_pwd_.length() <=16 & new_pwd_.length() >=6 & new_pwd_.length() <=16 & affirm_pwd_.length()>=6 & affirm_pwd_.length() <=16){
                    if (old_pwd_.equals(new_pwd_) &old_pwd_.equals(affirm_pwd_)){
                        Toast.makeText(getApplicationContext(),"您三次输入的密码是一样的！",Toast.LENGTH_LONG).show();
                    }else {
                        if (new_pwd_.equals(affirm_pwd_)){
                            SharedPreferences db = getSharedPreferences("LOGIN",MODE_PRIVATE);
                            query_pwd = db.getString("password","_");
                            id = db.getInt("id",0);
                            if (query_pwd.equals(old_pwd_)){
                                fix.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(URL.url)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        Pwd service = retrofit.create(Pwd.class);
                                        Call<JsonObject> call = service.PutPwd(affirm_pwd_,id);
                                        try {
                                            Response<JsonObject> response = call.execute();
                                            Message msg = new Message();
                                            if (response.code() == 403){
                                                SharedPreferences.Editor db1 = getSharedPreferences("LOGIN",MODE_PRIVATE).edit();
                                                db1.putString("password",affirm_pwd_);
                                                db1.commit();
                                                msg.what = 1;
                                            }else {
                                                msg.what = 2;
                                            }
                                            handler.sendMessage(msg);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }else {
                                Toast.makeText(getApplicationContext(), "旧密码错误，无法更改密码！", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(),"确认密码与新密码不同！",Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"您输入的密码长度有误！",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
