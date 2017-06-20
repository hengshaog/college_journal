package com.hengshao.app.college_journal.View;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.HTTP.User;
import com.hengshao.app.college_journal.Model.Datauser;
import android.support.v7.widget.CardView;
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

public class Modify_Data extends Activity implements View.OnClickListener {
    private EditText nickname;
    private EditText school;
    private EditText old_phone;
    private EditText new_phone;
    private CheckBox checkBox;
    private CardView card_male;
    private CardView card_female;
    private String sex;
    private ImageView male_img;
    private ImageView female_img;
    private TextView female_text;
    private TextView male_text;
    private CardView modify_success;
    private int check_ = 1;
    private String phone;
    private String name;
    private String school_;
    private String old_phone_;
    private String new_phone_;
    private ExecutorService fix;
    private int id;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    finish();
                    Toast.makeText(getApplicationContext(),"资料获取成功！",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"服务器错误，请稍后重试！",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_data);
        nickname = (EditText) findViewById(R.id.nickname);
        school = (EditText) findViewById(R.id.school);
        old_phone = (EditText) findViewById(R.id.old_phone);
        new_phone = (EditText) findViewById(R.id.new_phone);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        card_male = (CardView) findViewById(R.id.card_male);
        card_female = (CardView) findViewById(R.id.card_female);
        male_img = (ImageView) findViewById(R.id.male_img);
        female_img = (ImageView) findViewById(R.id.female_img);
        male_text = (TextView) findViewById(R.id.male_text);
        female_text = (TextView) findViewById(R.id.female_text);
        modify_success = (CardView) findViewById(R.id.modify_data);

        fix = Executors.newFixedThreadPool(1);
        modify_success.setOnClickListener(this);
        card_male.setOnClickListener(this);
        card_female.setOnClickListener(this);
        checkBox.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fix.shutdown();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_male:
                initCardBGM();
                card_male.setCardBackgroundColor(Color.rgb(196,45,57));
                male_img.setVisibility(View.VISIBLE);
                male_text.setTextColor(Color.rgb(255,255,255));
                sex = "男";
                break;
            case R.id.card_female:
                initCardBGM();
                card_female.setCardBackgroundColor(Color.rgb(196,45,57));
                female_img.setVisibility(View.VISIBLE);
                female_text.setTextColor(Color.rgb(255,255,255));
                sex = "女";
                break;
            case R.id.modify_data:
                name = nickname.getText().toString();
                school_ = school.getText().toString();
                old_phone_ = old_phone.getText().toString();
                new_phone_ = new_phone.getText().toString();
                SharedPreferences db = getSharedPreferences("LOGIN",MODE_PRIVATE);
                phone = db.getString("telephone","telephone");
                id = db.getInt("id",0);
                if (checkBox.isChecked() == true){
                    if (name != null & school_ != null &  sex != null){
                        if (old_phone_.equals(new_phone_)){
                            Toast.makeText(getApplicationContext(),"两次输入电话号码一样,不用修改手机",Toast.LENGTH_LONG).show();
                        }else {
                            if (phone.equals(old_phone_)){
                                fix.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        Datauser datauser = new Datauser();
                                        datauser.setId(id);
                                        datauser.setTelephone(new_phone_);
                                        datauser.setSchool(school_);
                                        datauser.setNickname(name);
                                        datauser.setSex(sex);
                                        datauser.setSignature("");
                                        datauser.setCreatetime("");
                                        datauser.setPraise("0");
                                        datauser.setAuthority("");
                                        datauser.setPassword("");
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(URL.url)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        User service = retrofit.create(User.class);
                                        Call<JsonObject> call = service.putUserdata(datauser);
                                        try {
                                            Response<JsonObject> response = call.execute();
                                            Message msg = new Message();
                                            if (response.code() == 403){
                                                msg.what = 1;
                                                SharedPreferences.Editor db = getSharedPreferences("LOGIN",MODE_APPEND).edit();
                                                db.putString("telephone",new_phone_);
                                                db.putString("school",school_);
                                                db.putString("nickname",name);
                                                db.putString("sex",sex);
                                                db.commit();
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
                                Toast.makeText(getApplicationContext(),"输入旧手机错误！",Toast.LENGTH_LONG).show();
                            }
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"你还有信息没有输入！",Toast.LENGTH_LONG).show();
                    }
                }else {
                    if (phone.equals("telephone")){
                        Toast.makeText(getApplicationContext(),"您的账号信息有误，请重新登录！",Toast.LENGTH_LONG).show();
                    }else {
                        if (name != null & school_ != null &  sex != null){
                            fix.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Datauser datauser = new Datauser();
                                    datauser.setId(id);
                                    datauser.setTelephone(old_phone_);
                                    datauser.setSchool(school_);
                                    datauser.setNickname(name);
                                    datauser.setSex(sex);
                                    datauser.setSignature("");
                                    datauser.setCreatetime("");
                                    datauser.setPraise("0");
                                    datauser.setAuthority("");
                                    datauser.setPassword("");
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(URL.url)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    User service = retrofit.create(User.class);
                                    Call<JsonObject> call = service.putUserdata(datauser);
                                    try {
                                        Response<JsonObject> response = call.execute();
                                        Message msg = new Message();
                                        if (response.code() == 403){
                                            SharedPreferences.Editor db = getSharedPreferences("LOGIN",MODE_APPEND).edit();
                                            db.putString("telephone",old_phone_);
                                            db.putString("school",school_);
                                            db.putString("nickname",name);
                                            db.putString("sex",sex);
                                            db.commit();
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
                            Toast.makeText(getApplicationContext(),"你还有信息没有输入！",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.checkBox:
                if (check_ == 1){
                    check_++;
                }else {
                    check_--;
                    old_phone.setText("");
                    new_phone.setText("");
                }
                break;
        }
    }

    private void initCardBGM() {
        card_female.setCardBackgroundColor(Color.rgb(255,255,255));
        card_male.setCardBackgroundColor(Color.rgb(255,255,255));
        male_img.setVisibility(View.GONE);
        female_img.setVisibility(View.GONE);
        female_text.setTextColor(Color.rgb(102,102,102));
        male_text.setTextColor(Color.rgb(102,102,102));

    }
}
