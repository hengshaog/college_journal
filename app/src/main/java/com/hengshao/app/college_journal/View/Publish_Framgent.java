package com.hengshao.app.college_journal.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.HTTP.Info;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.Model.Postinfo;
import com.hengshao.app.college_journal.R;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.data;
import static android.R.attr.switchMinWidth;

/**
 * Created by Heng on 17/5/27.
 */

public class Publish_Framgent  extends Fragment implements View.OnClickListener {
    private View  view;
    private EditText title;
    private EditText content;
    private CardView succeed;
    private String title_;
    private String content_;
    private int id;
    private String school;
    private int userid;
    private String username;
    private String userphone;
    private String praise;
    private ExecutorService fix;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getContext(),msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getContext(), "服务器错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fix.shutdown();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.publish_viewpager, container, false);
        title = (EditText) view.findViewById(R.id.title);
        content = (EditText) view.findViewById(R.id.content);
        succeed = (CardView) view.findViewById(R.id.succeed);
        fix = Executors.newFixedThreadPool(1);

        succeed.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.succeed:
                if (Checkone()){
                    if (Checkdb()){
                        Sendnetwork();
                    }else {
                        Toast.makeText(getContext(), "您的身份信息有误，请重新登陆！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"请先填写要发布的信息！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void Sendnetwork() {
        fix.execute(new Runnable() {
            @Override
            public void run() {

                Postinfo postinfo = new Postinfo();
                postinfo.setId(0);
                postinfo.setTitle(title_);
                postinfo.setSchool(school);
                postinfo.setContent(content_);
                postinfo.setUserid(userid);
                postinfo.setUsernickname(username);
                postinfo.setContent(content_);
                postinfo.setPraise("0");
                postinfo.setUsertelephone(userphone);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Info service = retrofit.create(Info.class);
                Call<JsonObject> call = service.Postinfo(postinfo);
                try {
                    Response<JsonObject> response = call.execute();
                    Message msg = new Message();
                    if (response.code() == 403){
                        msg.what = 1;
                        msg.obj = response.errorBody().string();
                    }else {
                        msg.what = 2;
                    }
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean Checkdb() {
        SharedPreferences db = getContext().getSharedPreferences("LOGIN", Context.MODE_APPEND);
        userid = db.getInt("id",0);
        school = db.getString("school","school");
        username = db.getString("nickname","name");
        userphone = db.getString("telephone", "phone");
        if (userid == 0 | school.equals("school") | username.equals("name") | userphone.equals("phone")){
            return false;
        }else {
            return true;
        }

    }

    private boolean Checkone() {
        title_ = title.getText().toString();
        content_ = content.getText().toString();
        if (title_ != null & content_ != null){
            return  true;
        }else {
            return false;
        }
    }
}
