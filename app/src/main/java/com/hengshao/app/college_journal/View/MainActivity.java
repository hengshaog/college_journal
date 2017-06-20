package com.hengshao.app.college_journal.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hengshao.app.college_journal.Dialog.Lisenter_back;
import com.hengshao.app.college_journal.Dialog.Modify_data_dialog;
import com.hengshao.app.college_journal.PercentLinearLayout;
import com.hengshao.app.college_journal.R;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;

/**
 * Created by Heng on 17/5/27.
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener ,Lisenter_back{
    private ViewPager viewpager;
    private TextView publish_text;
    private TextView obtain_text;
    private TextView nearby_text;
    private List<Fragment> flist;
    private FragmentPagerAdapter mAdapter;
    private TableLayout publish_table;
    private TableLayout obtain_table;
    private TableLayout nearby_table;
    private TextView nickname;
    private TextView signature;
    private PercentLinearLayout modify_signature;
    private PercentLinearLayout modify_data;
    private PercentLinearLayout modify_passwrod;
    private TextView praise_count;
    private PercentLinearLayout loginout;
    private Modify_data_dialog sig_dialog;
    private String name;
    private PercentLinearLayout suggest_linear;


    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;


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


    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.main_viewpager);
        publish_text = (TextView) findViewById(R.id.publish_text);
        nearby_text = (TextView) findViewById(R.id.nearby_text);
        obtain_text = (TextView) findViewById(R.id.obtain_text);
        publish_table = (TableLayout) findViewById(R.id.publish_table);
        obtain_table = (TableLayout) findViewById(R.id.obtain_table);
        nearby_table = (TableLayout) findViewById(R.id.nearby_table);
        nickname = (TextView) findViewById(R.id.nickname);
        signature = (TextView) findViewById(R.id.signature);
        modify_signature = (PercentLinearLayout) findViewById(R.id.modify_signature);
        modify_data = (PercentLinearLayout) findViewById(R.id.modify_data);
        modify_passwrod = (PercentLinearLayout) findViewById(R.id.modify_password);
        praise_count = (TextView) findViewById(R.id.praise_count);
        loginout = (PercentLinearLayout) findViewById(R.id.loginout);
        suggest_linear = (PercentLinearLayout) findViewById(R.id.suggest_linear);


        suggest_linear.setOnClickListener(this);
        nickname.setOnClickListener(this);

        loginout.setOnClickListener(this);
        modify_data.setOnClickListener(this);
        modify_signature.setOnClickListener(this);
        modify_passwrod.setOnClickListener(this);

        nearby_table.setOnClickListener(this);
        publish_table.setOnClickListener(this);
        obtain_table.setOnClickListener(this);

        Publish_Framgent publish_fg = new Publish_Framgent();
        Obtain_Fragment obtain_fg = new Obtain_Fragment();
        final Nearby_Fragment nearby_fg = new Nearby_Fragment();
        flist = new ArrayList<Fragment>();
        flist.add(publish_fg);
        flist.add(obtain_fg);
        flist.add(nearby_fg);

        sig_dialog = new Modify_data_dialog(MainActivity.this,MainActivity.this);
        initData();

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return flist.get(position);
            }

            @Override
            public int getCount() {
                return flist.size();
            }
        };

        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(mAdapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        resetcolor();
                        publish_text.setTextColor(rgb(199,0,0));
                        break;
                    case 1:
                        resetcolor();
                        obtain_text.setTextColor(rgb(199,0,0));
                        break;
                    case 2:
                        resetcolor();
                        nearby_text.setTextColor(rgb(199,0,0));
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        resetcolor();
                        publish_text.setTextColor(rgb(199,0,0));
                        break;
                    case 1:
                        resetcolor();
                        obtain_text.setTextColor(rgb(199,0,0));
                        break;
                    case 2:
                        resetcolor();
                        nearby_text.setTextColor(rgb(199,0,0));
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {
        SharedPreferences db = getSharedPreferences("LOGIN",MODE_PRIVATE);
        String name = db.getString("nickname","no");
        String sign = db.getString("signature","no");
        String praise = db.getString("praise","no");
        if (name.equals("no")){
            nickname.setText("登录");
            signature.setText("你现在还没有签名哦！");
            praise_count.setText("0");

        }else {
            nickname.setText(name);
            signature.setText(sign);
            praise_count.setText(praise);
        }

    }

    private void resetcolor() {
        publish_text.setTextColor(Color.rgb(134,134,134));
        nearby_text.setTextColor(Color.rgb(134,134,134));
        obtain_text.setTextColor(Color.rgb(134,134,134));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nearby_table:
                resetcolor();
                viewpager.setCurrentItem(2,true);
                nearby_text.setTextColor(Color.rgb(199,0,0));
                break;
            case R.id.obtain_table:
                resetcolor();
                viewpager.setCurrentItem(1,true);
                obtain_text.setTextColor(Color.rgb(199,0,0));
                break;
            case R.id.publish_table:
                resetcolor();
                viewpager.setCurrentItem(0,true);
                publish_text.setTextColor(Color.rgb(199,0,0));
                break;
            case R.id.nickname:
                name = nickname.getText().toString();
                if (name.equals("登录")){
                    Intent intent_login = new Intent("android.intent.login");
                    startActivity(intent_login);
                }
                break;
            case R.id.loginout:
                name = nickname.getText().toString();
                if (name.equals("登录")) {
                    Toast.makeText(getApplicationContext(),"请先登录！",Toast.LENGTH_LONG).show();
                }else {
                    this.getSharedPreferences("LOGIN", MODE_PRIVATE).edit().clear().commit();
                    initData();
                }
                break;
            case R.id.modify_data:
                name = nickname.getText().toString();
                if (name.equals("登录")) {
                    Toast.makeText(getApplicationContext(),"请先登录！",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent_data = new Intent("android.intent.modify_data");
                    startActivity(intent_data);
                }
                break;
            case R.id.modify_password:
                name = nickname.getText().toString();
                if (name.equals("登录")) {
                    Toast.makeText(getApplicationContext(),"请先登录！",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent_pwd = new Intent("android.intent.modify_pwd");
                    startActivity(intent_pwd);
                }
                break;
            case R.id.modify_signature:
                name = nickname.getText().toString();
                if (name.equals("登录")) {
                    Toast.makeText(getApplicationContext(),"请先登录！",Toast.LENGTH_LONG).show();
                }else {
                    sig_dialog.show();
                    Window window = sig_dialog.getWindow();
                    android.view.WindowManager.LayoutParams lp = window.getAttributes();
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(lp);
                    window.setBackgroundDrawableResource(android.R.color.transparent);
                    sig_dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    sig_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
                break;
            case R.id.suggest_linear:
                name = nickname.getText().toString();
                if (name.equals("登录")) {
                    Toast.makeText(getApplicationContext(),"请先登录！",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent_pwd = new Intent("android.intent.suggest");
                    startActivity(intent_pwd);
                }
                break;
        }
    }

    @Override
    public void Lisente_back(int i) {
        initData();
    }
}
