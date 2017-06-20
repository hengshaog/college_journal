package com.hengshao.app.college_journal.View;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.Model.Postsuggest;
import com.hengshao.app.college_journal.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hengshao on 2017/6/20.
 */

public class Suggest extends Activity implements View.OnClickListener {
    private String content_;
    private EditText content;
    private CardView submit;
    private int userid = 0;
    private ExecutorService fix;
    private String school;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    finish();
                    Toast.makeText(getApplicationContext(),msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(Suggest.this, "服务器错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fix.shutdown();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest);
        content = (EditText) findViewById(R.id.content);
        submit = (CardView) findViewById(R.id.submit);
        fix = Executors.newFixedThreadPool(1);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                if (Checkone()){
                    if (CheckId()){
                        SendNetwork();
                    }else {
                        Toast.makeText(getApplicationContext(),"您的账户信息有误，请重新登陆！",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"请先填写建议!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void SendNetwork() {
        fix.execute(new Runnable() {
            @Override
            public void run() {

                Postsuggest postsuggest = new Postsuggest();
                postsuggest.setId(0);
                postsuggest.setContent(content_);
                postsuggest.setCreatetime("0");
                postsuggest.setUserid(userid);
                postsuggest.setSchool(school);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                com.hengshao.app.college_journal.HTTP.Suggest service = retrofit.create(com.hengshao.app.college_journal.HTTP.Suggest.class);
                Call<JsonObject> call = service.Postsuggest(postsuggest);
                try {
                    Response<JsonObject> response = call.execute();
                    Message msg = new Message();
                    if (response.code() == 403){
                        msg.what = 1;
                        msg.obj = response.errorBody().string();
                    } else {
                        msg.what = 2;
                    }
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private boolean CheckId() {
        SharedPreferences db = getSharedPreferences("LOGIN",MODE_APPEND);
        userid = db.getInt("id",0);
        school = db.getString("school","school");
        if (userid == 0 & school == "school"){
            return false;
        }else {
            return  true;
        }
    }

    private boolean Checkone() {
        content_ = content.getText().toString();
        if (content_ != null ){
            return true;
        }else {
            return false;
        }

    }
}
