package com.hengshao.app.college_journal.View;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.HTTP.User;
import com.hengshao.app.college_journal.Model.Datauser;
import com.hengshao.app.college_journal.PercentLinearLayout;
import com.hengshao.app.college_journal.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Heng on 2017/6/1.
 */

public class Register extends Activity implements View.OnClickListener {
    private EditText telephone;
    private EditText password;
    private CardView card_male;
    private CardView card_female;
    private String sex;
    private ImageView male_img;
    private ImageView female_img;
    private TextView female_text;
    private TextView male_text;
    private PercentLinearLayout register_succeed;
    private EditText school;
    private String phone;
    private String pwd;
    private String school_;
    private ExecutorService fix;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext(),"恭喜你，注册成功！",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 2:
                    if (msg.obj.equals("注册成功！")){
                        finish();
                    }
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),"服务器错误，请稍后重试!",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        telephone = (EditText) findViewById(R.id.edittelephone);
        password = (EditText) findViewById(R.id.editpassword);
        card_male = (CardView) findViewById(R.id.card_male);
        card_female = (CardView) findViewById(R.id.card_female);
        male_img = (ImageView) findViewById(R.id.male_img);
        female_img = (ImageView) findViewById(R.id.female_img);
        male_text = (TextView) findViewById(R.id.male_text);
        female_text = (TextView) findViewById(R.id.female_text);
        school = (EditText) findViewById(R.id.editschool);
        register_succeed = (PercentLinearLayout) findViewById(R.id.register_succeed);

        fix = Executors.newFixedThreadPool(1);

        register_succeed.setOnClickListener(this);
        card_male.setOnClickListener(this);
        card_female.setOnClickListener(this);

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
            case R.id.register_succeed:
                phone = telephone.getText().toString();
                pwd = password.getText().toString();
                school_ = school.getText().toString();
                if (phone.length() == 11 ){
                    if (pwd.length() >= 6 & pwd.length() <=16){
                        if (school.length() >= 2){
                            if (sex.equals("男") | sex.equals("女")){
                                retrofit();
                            }else {
                                Toast.makeText(getApplicationContext(),"请选择性别！",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"请输入正确学校!",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"请输入6到16为密码！",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"请输入正确电话号码！",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void retrofit() {
        fix.execute(new Runnable() {
            @Override
            public void run() {
                Datauser datauser = new Datauser();
                datauser.setId(0);
                datauser.setTelephone(phone);
                datauser.setSchool(school_);
                datauser.setSex(sex);
                datauser.setCreatetime("0");
                datauser.setSignature("你现在还没有签名！");
                datauser.setAuthority("0");
                datauser.setPraise("0");
                datauser.setNickname("刊友"+new Random().nextInt(1000));
                datauser.setPassword(password.getText().toString());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                User service = retrofit.create(User.class);
                Call<String> call = service.PostUser(datauser);
                try {
                    Response<String> response = call.execute();
                    Message msg = new Message();
                    if (response.code() == 200){
                        msg.what = 1;
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