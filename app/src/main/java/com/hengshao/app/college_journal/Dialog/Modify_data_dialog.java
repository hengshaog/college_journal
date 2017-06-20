package com.hengshao.app.college_journal.Dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.HTTP.User;
import com.hengshao.app.college_journal.Model.PutUserSign;
import com.hengshao.app.college_journal.PercentLinearLayout;
import com.hengshao.app.college_journal.R;
import com.hengshao.app.college_journal.View.MainActivity;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Heng on 2017/6/3.
 */

public class Modify_data_dialog extends AlertDialog implements View.OnClickListener {
    private Context context;
    private PercentLinearLayout modify_sig_text;
    private String sig;
    private EditText signature;
    private ExecutorService fix;
    private int id;
    private MainActivity mainActivity;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    SharedPreferences.Editor db = getContext().getSharedPreferences("LOGIN",Context.MODE_APPEND).edit();
                    db.putString("signature",signature.getText().toString());
                    db.commit();
                    Toast.makeText(getContext(),"更新签名成功!",Toast.LENGTH_LONG).show();
                    mainActivity.Lisente_back(1);
                    cancel();
                    break;
                case 2:
                    Toast.makeText(getContext(),"服务器错误，请稍后重新！",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    public Modify_data_dialog(@NonNull Context context, MainActivity mainActivity) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
        this.mainActivity = mainActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.signature);
        modify_sig_text = (PercentLinearLayout) findViewById(R.id.modify_sig_text);
        signature = (EditText) findViewById(R.id.signature);
        fix = Executors.newFixedThreadPool(1);

        setCanceledOnTouchOutside(true);
        modify_sig_text.setOnClickListener(this);



    }

    @Override
    public void dismiss() {
        super.dismiss();
        fix.shutdown();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.modify_sig_text:
                sig = signature.getText().toString();
                if (sig.length() >= 2){
                    Retrofits();
                }else {
                    Toast.makeText(getContext(),"请先输入签名!",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void Retrofits() {
        SharedPreferences db = context.getSharedPreferences("LOGIN",context.MODE_APPEND);
        id = db.getInt("id",0);
        if (id != 0){
            final PutUserSign putUserSign = new PutUserSign();
            putUserSign.setSignature(sig);
            fix.execute(new Runnable() {
                @Override
                public void run() {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL.url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    User service = retrofit.create(User.class);
                    Call<JsonObject> call = service.PutUser(putUserSign,id);
                    try {
                        Response<JsonObject> response = call.execute();
                        Message msg = new Message();
                        if (response.code() == 403){
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
            Toast.makeText(context,"您的账号信息有误，请重新登录！",Toast.LENGTH_LONG).show();
        }
    }

}
