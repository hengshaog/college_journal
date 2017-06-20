package com.hengshao.app.college_journal.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.hengshao.app.college_journal.R;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hengshao on 2017/6/20.
 */

public class Welcome extends Activity{
    private Handler handler  = new Handler();
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JPushInterface.setDebugMode(true);
                JPushInterface.init(Welcome.this);
                Intent intent = new Intent("android.intent.main");
                startActivity(intent);
                finish();
            }
        },3000);

    }


    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
//            System.exit(1);
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this,"再按一次退出校刊!",Toast.LENGTH_LONG).show();

        }
        mBackPressed = System.currentTimeMillis();
    }
}
